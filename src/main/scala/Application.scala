import Main._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.RejectionHandler
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import http.HttpService
import org.slf4j.LoggerFactory

import scala.concurrent._
import scala.concurrent.duration._

class Application extends ApplicationLifecycle {
  val appLogger = Logger(LoggerFactory.getLogger("Application"))
  val applicationName = "donald-manager"

  implicit lazy val system = ActorSystem()
  implicit lazy val materializer = ActorMaterializer()
  implicit lazy val executionContext = system.dispatcher
  implicit lazy val timeout: Timeout = Timeout(10.second)

  var started: Boolean = false
  var bindingFuture: Future[Http.ServerBinding] = null

  def start(): Unit = {
    val config = ConfigFactory.load()
    val host = config.getString("http.host")
    val port = config.getInt("http.port")

    val httpService = new HttpService()
    implicit val myRejectionHandler: RejectionHandler = httpService.myRejectionHandler

    bindingFuture = Http().bindAndHandle(httpService.routes, host, port)
    bindingFuture.onFailure {
      case ex: Exception =>
        logger.error("Failed to bind to ${}:{}!", host, port)
    }

    appLogger.info(s"Server online at http://${host}:${port}/")
    started = true
  }

  def stop(): Unit = {
    logger.info(s"Stopping $applicationName Service")
    if (started) {
      bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
      started = false
    }

    materializer.shutdown()
    system.terminate()
  }
}