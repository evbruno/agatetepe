package agatetepe

//import agatetepe.HttpClient.{Response, Entity}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.{ExecutionContext, Future}

// val jsonFoo = """{ "foo" : "bar", "vector" : [ 123, "onetwothree" ], "id" : null }"""
//val url = "http://jsonplaceholder.typicode.com/posts"
//val future: Future[Entity] = client.asyncProcess(url, jsonFoo)
//val result: Entity = Await.result(future, 1.minutes)
class FetchJSONPlaceholderFlatSpec extends FlatSpec with Matchers with ScalaFutures {

	import ExecutionContext.Implicits.global

	val subject = HttpClient()

	import org.scalatest.time.{Millis, Seconds, Span}

	implicit val defaultPatience =
		PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))


//	"subject" should " fetch client data " in {
//		val expected = """{ "foo" : "bar", "vector" : [ 123, "onetwothree" ], "id" : null }"""
//		val url = "http://jsonplaceholder.typicode.com/posts/1"
//		val future: Future[Response] = subject.get(url).asyncProcess(url).mapTo[Response]
//
//		whenReady(future) { result =>
//			println("Future result===" + result)
//
//			result.statusCode should equal(200)
//			result.body should equal(200)
//			result should equal("The result")
//		}
//
//	}

//	"subject" should " create data " in {
//		val jsonFoo = """ {
//						|   "userId": 1,
//						|   "id": 1,
//						|   "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
//						|   "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
//						| } """.stripMargin
//		val url = "http://jsonplaceholder.typicode.com/posts/1"
//		val future: Future[Response] = subject.asyncProcess(url).mapTo[Response]
//
//		whenReady(future) { result =>
//			println("Future result===" + result)
//
//			result.statusCode should equal(200)
//			result.body should equal(200)
//			result should equal("The result")
//		}
//
//	}

}
