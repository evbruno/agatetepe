package agatetepe

import agatetepe.Entity.Request
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext

/**
  * @see http://jsonplaceholder.typicode.com/
  */

class BetamaxRequestsSpec extends FlatSpec
	with Matchers
	with CustomMatchers
	with BetamaxSuite {

	val tapeName = "MockMyApi"

	import ExecutionContext.Implicits.global

	val subject = HttpClient()

	"Client" should "fetch data for post 1" in {
		val req = Request get "http://jsonplaceholder.typicode.com/posts/1"
		val future = subject.asyncProcess(req)

		whenReady(future) { result =>
			result.statusCode should equal(200)
			result.headers should contain(("ETag", "W/\"124-yv65LoT2uMHrpn06wNpAcQ\""))
			result.headers should contain(("Vary", "Origin"))
			result.body.asString should equal ("""{
										|  "userId": 1,
										|  "id": 1,
										|  "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
										|  "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
										|}""".stripMargin)

		}
	}

	"Client" should "post new data" in {
		val postJson =
			"""{
			  |    title: 'foo',
			  |    body: 'bar',
			  |    userId: 1
			  |  }""".stripMargin
		val req = Request post "http://jsonplaceholder.typicode.com/posts" withBody postJson
		val future = subject.asyncProcess(req)
		val result = future.futureValue

		result.statusCode should equal(201)
		result.headers should contain(("ETag", "W/\"53-p6NLNQB5cit80rB1QBVY6g\""))
		result.body.asString should be ("""{
									  |  "{\n    title: 'foo',\n    body: 'bar',\n    userId: 1\n  }": "",
									  |  "id": 101
									  |}""".stripMargin)
	}

	"Client" should "delete  data" in {
		val req = Request delete "http://jsonplaceholder.typicode.com/posts/1"
		val future = subject.asyncProcess(req)
		val result = future.futureValue

		result.statusCode should equal(200)
		result.headers should contain(("ETag", "W/\"2-mZFLkyvTelC5g8XnyQrpOw\""))

		result.body.asString should equal ("{}")
	}

	"Client" should "post json data" in {
		val postJson =
			"""{
			  | "@class": "etc.bruno.MyApi",
			  |  "token": null
			  |}""".stripMargin
		val req = Request post "http://localhost:8079/api/rest" json postJson
		val future = subject.asyncProcess(req)
		val result = future.futureValue

		result.statusCode should equal(200)
		result.headers should contain(("X-Powered-By", "Servlet/2.5 (Winstone/0.9.10)"))
		result.headers should contain(("Content-Type", "application/json"))
		result.body.asString should be ("""{"token": "/kx/R1WXXTesq/NzXxQpcKwr5ZFCv9xySa0RgO7lgv5yeRipapcJq2hB+Kou8bv+6nFMVhlfx1M"}""")
	}

	"Client" should "get image from server" in {
		val req = Request get "http://localhost:8000/file.tar.gz"
		val future = subject.asyncProcess(req)
		val result = future.futureValue

		result.statusCode should equal(200)
		result.headers should contain(("Content-type", "application/octet-stream"))

		md5sum(result.body.get) should be("cb4fa2865ac06155841442ea4084564b")
	}

	"Client" should "download image to temporary file using helpers" in {
		import Helpers._

		val req = Request get "http://localhost:8000/file.tar.gz"
		val future = subject.asyncProcess(req).map(downloadToTempFile)
		val (downloaded, bytes) = future.futureValue
		println("FILE======" + downloaded)

		bytes should equal(420)

		downloaded should be a 'file
		downloaded should haveSize(420)
		downloaded should (exists and endWithExtension(".tmp") and haveMD5("cb4fa2865ac06155841442ea4084564b"))
	}

}
