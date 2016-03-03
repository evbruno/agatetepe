package agatetepe

import java.util
import java.util.Comparator

import agatetepe.Entity.{Request, Response}
import co.freeside.betamax.{MatchRule, Recorder, TapeMode}
import co.freeside.betamax.proxy.jetty.ProxyServer

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

// borrowed from:
// http://blog.scottlogic.com/2013/07/18/betamax-in-scala.html
// https://engineering.sharethrough.com/blog/2013/07/13/integration-testing-http-requests-with-scala-and-betamax/

object BetamaxSpike extends App {

	//	implicit val ec = new CustomExecutionContext
	import scala.concurrent.ExecutionContext.Implicits.global

	val client = HttpClient()

	var req: Request = _

	req = Request get "http://jsonplaceholder.typicode.com/posts/1"
	//	process(request)


	val postJson =
		"""{
		  |    title: 'foo',
		  |    body: 'bar',
		  |    userId: 1
		  |  }""".stripMargin
	req = Request post "http://jsonplaceholder.typicode.com/posts" withBody postJson
	//process(req)

	req = Request delete "http://jsonplaceholder.typicode.com/posts/1"
	//	process(req)

	val json = """{
				 |    "@class": "objective.ng.api.atendimentounificado.autenticacao.ComandoAutenticadoRequest",
				 |    "token": null,
				 |    "comandoEncapsuladoJSON": "{\"@class\":\"objective.ng.api.atendimentounificado.autenticacao.AutenticacaoAtendimentoUnificadoRequest\", \"login\": \"teste\",  \"senha\": \"teste\",  \"ip\": \"127.0.0.1\"}"
				 |}""".stripMargin
	req = Request post "http://localhost:8078/executor/execute" withBody json withHeader("Content-Type", "application/json") withHeader("X-Token", "223311")
	process(req)

	def process(req: Request) {
		val tapeMode = TapeMode.READ_WRITE
		val recorder = new Recorder

		val proxyServer = new ProxyServer(recorder)

		recorder.insertTape("MockMyApi", rules)

		//	recorder.getTape.setMode(recorder.getDefaultMode())
		recorder.getTape.setMode(tapeMode)
		proxyServer.start()

		val future: Future[Response] = client.asyncProcess(req)
		val result: Response = Await.result(future, 1.minutes)

		import sext._

		println(result.valueTreeString)
		println(result)

		recorder.ejectTape()
		proxyServer.stop()
	}

	import co.freeside.betamax.message.{Request => BetaRequest}
	type BetaComparator = Comparator[BetaRequest]

	lazy val rules: util.Map[String, util.List[BetaComparator]] = {
		import collection.JavaConverters._
		val rules : java.util.List[BetaComparator] = Seq[BetaComparator](MatchRule.method, MatchRule.uri, MatchRule.headers).asJava
		Map("match" -> rules).asJava
	}

}

import java.util.concurrent.Executors

class CustomExecutionContext extends ExecutionContext {
	val threadPool = Executors.newFixedThreadPool(2)

	def execute(runnable: Runnable) {
		threadPool.submit(runnable)
	}

	def reportFailure(t: Throwable) {}
}