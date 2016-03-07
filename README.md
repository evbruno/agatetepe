[![Build Status](https://travis-ci.org/evbruno/agatetepe.svg?branch=master)](https://travis-ci.org/evbruno/agatetepe)

# Aga-te-te-pe

_(portuguese pronunciation for HTTP)_

**Yet another http client for Scala.**

 **Disclaimer**: if you're looking for something more robust (and reliable), pay a visit to the [akka-http library](http://doc.akka.io/docs/akka-stream-and-http-experimental/2.0.3/scala/http/introduction.html) or read this [blog post](https://www.implicitdef.com/2015/11/19/comparing-scala-http-client-libraries.html).

## What is?

A simple wrapper over `java.net.HttpURLConnection` using `scala.concurrent.Future`s

## How it works ?

```
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

```

## External stuff

http://jsonplaceholder.typicode.com/
