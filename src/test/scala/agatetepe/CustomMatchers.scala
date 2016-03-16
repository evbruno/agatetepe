package agatetepe

import java.io.File

import org.scalatest.matchers.{Matcher, MatchResult}

trait CustomMatchers {

	class FileExistsMatcher extends Matcher[File] {

		def apply(left: File) = {
			val  name = left.getName
			MatchResult(
				left.exists()    ,
				s"""File $name does not exist"""",
				s"""File $name does not exist""""
			)
		}
	}

	def exists = new FileExistsMatcher

	class FileEndsWithExtensionMatcher(expectedExtension: String) extends Matcher[File] {

		def apply(left: File) = {
			val name = left.getName
			MatchResult(
				name.endsWith(expectedExtension),
				s"""File $name did not end with extension "$expectedExtension"""",
				s"""File $name ended with extension "$expectedExtension""""
			)
		}
	}

	def endWithExtension(expectedExtension: String) = new FileEndsWithExtensionMatcher(expectedExtension)

	class FileSizeMatcher(expectedSize: Int) extends Matcher[File] {

		def apply(left: File) = {
			val name = left.getName
			val size = left.length
			MatchResult(
				size == expectedSize,
				s"""File $name size isn't equal to "$expectedSize"""",
				s"""File $name has size $size""""
			)
		}
	}

	def haveSize(expectedSize: Int) = new FileSizeMatcher(expectedSize)

	class FileMD5SumMatcher(expectedHash: String) extends Matcher[File] {

		def apply(left: File) = {
			val name = left.getAbsoluteFile.toString
			val actualHash = md5sum(loadContent(left))

			MatchResult(
				expectedHash.equalsIgnoreCase(actualHash),
				s"""File $name md5sum isn't equal to "$expectedHash"""",
				s"""File $name has md5sum $actualHash""""
			)
		}
	}

	def haveMD5(expectedHash: String) = new FileMD5SumMatcher(expectedHash)

	// Helpers

	def loadContent(fileName: File): Array[Byte] = {
		println(">>>>>>>> " + fileName)
		val source = scala.io.Source.fromFile(fileName, "UTF8")
		println(">>>>>>>> " + source)
		val byteArray = source.map(_.toByte).toArray
		source.close
		byteArray
	}

	def md5sum(source: Array[Byte]) =
		java.security.MessageDigest.getInstance("MD5").digest(source).map(0xFF & _).map("%02x".format(_)).foldLeft(""){_ + _}
}

object CustomMatchers extends CustomMatchers
