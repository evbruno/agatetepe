package agatetepe

import java.io.{BufferedReader, IOException, InputStream, InputStreamReader}
import java.net.{HttpURLConnection, URL}
import java.nio.charset.StandardCharsets

import agatetepe.Entity._
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}

object HttpClient {

	def apply() = new HttpClient
}

class HttpClient {

	lazy val logger = LoggerFactory.getLogger(this.getClass)

	def asyncProcess(req: Request)(implicit ex: ExecutionContext): Future[Response] = Future(process(req))

	def process(req: Request): Response = {

		val charset = StandardCharsets.UTF_8.name

		logger.debug("Opening connection on {}", req.url)

		val conn = new URL(req.url).openConnection().asInstanceOf[HttpURLConnection]

		conn.setRequestMethod(req.method.toString)

		conn.setDoOutput(true)
		//conn.setUseCaches(false)

		conn.setRequestProperty("Accept-Charset", charset)

		for ((key, value) <- req.headers)
			conn.setRequestProperty(key, value)

		if (!req.body.isEmpty)
			writeBody(conn, req.body.get.getBytes(charset))

		val inputStream = findInputStream(conn)

		val resp = nonEmptyResponse(conn, extractBody(conn, charset, inputStream))

		conn.disconnect

		resp
	}

	private def writeBody(conn: HttpURLConnection, payload: Array[Byte]): Unit = {
		conn.setDoInput(true)
		conn.setRequestProperty("Content-Length", payload.length.toString)
		conn.setFixedLengthStreamingMode(payload.length)

		val out = conn.getOutputStream

		try {
			out.write(payload)
			out.flush
		} finally {
			out.close
		}

	}

	private def nonEmptyResponse(conn: HttpURLConnection, responseBody: Body) = {
		val statusCode = conn.getResponseCode
		val (statusLine, responseHeaders) = extractHeaders(conn)

		conn.disconnect

		Response(
			statusCode = statusCode,
			statusLine = statusLine,
			body = Option(responseBody),
			headers = responseHeaders
		)
	}

	private def findInputStream(conn: HttpURLConnection): InputStream = {
		try {
			conn.getInputStream
		} catch {
			case _: IOException => conn.getErrorStream
		}
	}

	private def extractBody(connection: HttpURLConnection, charset: String, source: InputStream): String = {
		val reader = new BufferedReader(new InputStreamReader(source, charset))
		val str = Stream.continually(reader.readLine).takeWhile(_ != null).mkString("\n")
		source.close
		str
	}

	private def extractHeaders(conn: HttpURLConnection): (String, Headers) = {
		import scala.collection.JavaConverters._
		val headers = conn.getHeaderFields.asScala

		val statusLine = headers.find(_._1 == null) match {
			case Some((_, values)) => values.asScala.mkString(";")
			case x => "empty"
		}

		val ret = headers.filter(_._1 != null).map { in =>
			(in._1, in._2.asScala.mkString(";"))
		}

		(statusLine, ret.toSet)
	}

}
