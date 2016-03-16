package agatetepe

import java.net.URL

object Entity {

	// temporary types
	// TODO type safe'm all ?

	type StatusCode = Int
	type Header = (String, String)
	type Headers = Set[Header]
	type Body = Array[Byte]

	// Request

	final case class Request(url: String,
							 method: Method = GET,
							 body: Option[String] = None,
							 headers: Headers = Set.empty) {

		require(new URL(url).getProtocol != null, "Must be a valid URL")

		// DSL / helpers

		def withHeader(key: String, value: String) = copy(headers = headers + Tuple2(key, value))

		def withBody(body: String) = copy(body = Some(body))

		def json(jsonBody: String) = copy(
											body = Some(jsonBody),
											headers = headers + Tuple2("Content-Type", "application/json"))

	}

	object Request {

		def get(url: String) = new Request(url, GET)

		def post(url: String) = new Request(url, POST)

		def put(url: String) = new Request(url, PUT)

		def delete(url: String) = new Request(url, DELETE)

		def patch(url: String) = new Request(url, PATCH)

	}

	// Response

	sealed case class Response(statusCode: StatusCode,
							   body: Option[Body] = None,
							   headers: Headers = Set.empty)

	// Request.METHOD

	sealed trait Method

	final case object GET extends Method

	final case object POST extends Method

	final case object PUT extends Method

	final case object DELETE extends Method

	final case object PATCH extends Method

}