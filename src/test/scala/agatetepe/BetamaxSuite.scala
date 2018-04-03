//package agatetepe
//
//import java.util
//import java.util.Comparator
//
//import co.freeside.betamax.message.Request
//import co.freeside.betamax.proxy.jetty.ProxyServer
//import co.freeside.betamax.{MatchRule, Recorder, TapeMode}
//import org.scalatest.concurrent.ScalaFutures
//import org.scalatest.time.{Millis, Seconds, Span}
//import org.scalatest.{BeforeAndAfterEach, Suite}
//
//trait BetamaxSuite extends BeforeAndAfterEach with ScalaFutures {
//
//	this: Suite =>
//
//	val tapeName: String
//
//	val tapeMode = TapeMode.READ_ONLY
//
//	private var recorder: Recorder = _
//	private var proxyServer: ProxyServer = _
//
//	implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))
//
//	override def beforeEach {
//		recorder = new Recorder
//		proxyServer = new ProxyServer(recorder)
//
//		recorder.insertTape(tapeName, rules)
//		recorder.getTape.setMode(tapeMode)
//
//		proxyServer.start()
//	}
//
//	private lazy val rules: util.Map[String, util.List[Comparator[Request]]] = {
//		import co.freeside.betamax.message.Request
//		import MatchRule._
//		import collection.JavaConverters._
//
//		// val listOfRules = Seq[Comparator[Request]](method, uri, headers).asJava
//		val listOfRules = Seq[Comparator[Request]](method, uri).asJava
//
//		Map("match" -> listOfRules).asJava
//	}
//
//	override def afterEach = {
//		recorder.ejectTape()
//		proxyServer.stop()
//	}
//
//}
