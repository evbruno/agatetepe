package agatetepe

import java.net.MalformedURLException

import org.scalatest.{Matchers, FlatSpec}

class RequestSpec extends FlatSpec with Matchers {

	import Entity._

	"Request" should "create a simple get" in {
		val request: Request = Request.get("http://127.0.0.1/")

		request.url should equal("http://127.0.0.1/")
		request.method should equal(GET)
		request.headers shouldBe empty
		request.body shouldBe empty
	}

	"Request" should "create a simple post" in {
		val request = Request.post("http://127.0.0.1/")

		request.url should equal("http://127.0.0.1/")
		request.method should equal(POST)
		request.headers shouldBe empty
		request.body shouldBe empty
	}

	"Request" should "create a post with body and headers" in {
		val request = Request.post("http://127.0.0.1/") withHeader("Foo", "Bar") withBody ("foo=bar") withHeader("Content-Foo", "application/bar")

		request.url should equal("http://127.0.0.1/")
		request.method should equal(POST)
		request.headers should equal(Set(("Content-Foo", "application/bar"), ("Foo", "Bar")))
		request.body shouldBe Some("foo=bar")
	}

	"Request" should "create a post with json body / headers" in {
		val request = Request.post("http://127.0.0.1/").json("{\"foo\":\"bar\"}")

		request.url should equal("http://127.0.0.1/")
		request.method should equal(POST)
		request.headers should equal(Set(("Content-Type", "application/json"), ("Accept", "application/json")))
		request.body shouldBe Some("{\"foo\":\"bar\"}")
	}

	"Request" should "create a 'put' request" in {
		val request = Request.put("http://localhost")
		request.url should equal("http://localhost")
		request.method.toString should equal("PUT")
	}

	"Request" should "create a 'patch' request" in {
		val request = Request.patch("http://127.0.0.1/")
		request.url should equal("http://127.0.0.1/")
		request.method.toString should equal("PATCH")
	}

	"Builder" should "NOT create a request with an invalid url" in {

		a[MalformedURLException] should be thrownBy {
			Request post ("httpx://127.0.0.1/") withBody ("foo=bar") withHeader("Content-Foo", "application/bar")
		}
	}

}
