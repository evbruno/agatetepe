package agatetepe

import java.io.{BufferedReader, IOException, InputStream, InputStreamReader}
import java.net._
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

		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
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

		val resp = transformResponse(conn, extractBody(inputStream))

		conn.disconnect

		resp
	}

	private def writeBody(conn: HttpURLConnection, payload: Array[Byte]): Unit = {
		conn.setDoInput(true)
		conn.setRequestProperty("Content-Length", payload.length.toString)
		// conn.setFixedLengthStreamingMode(payload.length)

		val out = conn.getOutputStream

		try {
			out.write(payload)
			out.flush
		} finally {
			out.close
		}

	}

	private def transformResponse(conn: HttpURLConnection, responseBody: Body) = {
		val statusCode = conn.getResponseCode
		val responseHeaders = extractHeaders(conn)

		conn.disconnect

		Response(
			statusCode = statusCode,
			body = Option(responseBody),
			headers = responseHeaders
		)
	}

	private def findInputStream(conn: HttpURLConnection): InputStream = {
		try {
			conn.getInputStream
		} catch {
			case x: IOException => conn.getErrorStream
		}
	}

	private def extractBody(source: InputStream): Body =
	if (source == null) Array()
	else Stream.continually(source.read).takeWhile(_ != -1).toArray.map(_.toByte)

	private def extractHeaders(conn: HttpURLConnection): Headers = {
		import scala.collection.JavaConverters._
		val headers = conn.getHeaderFields.asScala

		headers.filter(_._1 != null).map { in =>
			(in._1, in._2.asScala.mkString(";"))
		}.toSet
	}

}
