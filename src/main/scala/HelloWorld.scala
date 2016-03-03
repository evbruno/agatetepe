package agatetepe

import agatetepe.Entity.{Request, Response}
import Request._

import scala.concurrent._
import scala.concurrent.duration._

object HelloWorld extends App {

	import scala.concurrent.ExecutionContext.Implicits.global

	val client = HttpClient()

	val url = "http://jsonplaceholder.typicode.com/posts/1"
	val request = get(url)
	val future = client.asyncProcess(request)
	val result: Response = Await.result(future, 1.minutes)

	println(result)

}
