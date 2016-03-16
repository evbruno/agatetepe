package demo

import agatetepe.Entity.Request._
import agatetepe.Entity.Response
import agatetepe.HttpClient

import scala.concurrent._
import scala.concurrent.duration._

object HelloWorldString extends App {

	import scala.concurrent.ExecutionContext.Implicits.global

	val client = HttpClient()

	val url = "http://jsonplaceholder.typicode.com/posts/1"
	val request = get(url)
	val future = client.asyncProcess(request)
	val result: Response = Await.result(future, 1.minutes)

	println(result.statusCode)
	println(result.headers)

	println(new String(result.body.get))
}

object HelloWorldDownloadImage extends App {

	import java.io.FileOutputStream
	import scala.concurrent.ExecutionContext.Implicits.global

	val client = HttpClient()

	val url = "http://www.scala-lang.org/resources/img/smooth-spiral.png"
	val request = get(url)
	val future = client.asyncProcess(request)
	val result: Response = Await.result(future, 1.minutes)

	val bytes: Array[Byte] = result.body.get

	val fos = new FileOutputStream("/tmp/scala.png")
	fos.write(bytes)
	fos.close

	println(s"Got image ${bytes.length} bytes")
}

object HelloWorldDownloadImageUsingHelpers extends App {

	import agatetepe.Helpers._
	import scala.concurrent.ExecutionContext.Implicits.global

	val client = HttpClient()
	val request = get("http://www.scala-lang.org/resources/img/smooth-spiral.png")
	val future = client asyncProcess (request) map downloadToTempFile
	val result = Await.result(future, 1.minutes)

	println(s"Got image ${result._1} with ${result._2} bytes")
}
