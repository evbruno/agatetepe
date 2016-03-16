import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8

import agatetepe.Entity._

package object agatetepe {

	implicit class RichBody(val body: Option[Body]) {

		def asString : String = asString(UTF_8)

		def asString(charset: Charset): String = body match {
			case None => ""
			case Some(x) => new String(x, charset.name)
		}
	}

	object Helpers {

		import java.io.FileOutputStream

		type DownloadedSize = Int
		type Downloaded = Response => (File, DownloadedSize)

		def downloadToTempFile: Downloaded = {
			// TODO try to guess file extension ?
			val target: File = File.createTempFile("download", ".tmp")
			downloadTo(target).andThen(size => (target, size))
		}

		def downloadTo(fileName: String): (Response) => DownloadedSize = downloadTo(new File(fileName))

		/**
		  * @return number of bytes downloaded. Returns <code>-1</code> if status is other than 200
		  */
		def downloadTo(file: File): (Response) => DownloadedSize = (response: Response) => {
			response.statusCode match {
				case 200 => download(response.body, file)
				case _ => -1
			}
		}

		private def download(body: Option[Body], file: File) =
			body match {
				case Some(bytes) => {
					val fos = new FileOutputStream(file)
					fos.write(bytes)
					fos.close
					bytes.length
				}
				case None => -1
			}
	}

}
