package http

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

import scala.concurrent.ExecutionContext

/**
  * Created by ksuzuki on 2017/04/25.
  */
class HttpService()(implicit executionContext: ExecutionContext) {
  val routes = pathPrefix("api") {
    pathPrefix("v1") {
      path("hello") {
        complete("Hello")
      }
    }
  }

  implicit def myRejectionHandler: RejectionHandler = RejectionHandler.newBuilder()
    .handle { case MissingCookieRejection(cookieName) =>
      deleteCookie("donald_manager_id") {
        complete(HttpResponse(StatusCodes.Unauthorized, entity = "No cookies, no service!!!"))
      }
    }
    .handle { case AuthorizationFailedRejection =>
      complete((StatusCodes.Forbidden, "You're out of your depth!"))
    }
    .handle { case ValidationRejection(msg, _) =>
      complete((StatusCodes.InternalServerError, "That wasn't valid! " + msg))
    }
    .handleAll[MethodRejection] { methodRejections =>
    val names = methodRejections.map(_.supported.name)
    complete((StatusCodes.MethodNotAllowed, s"Can't do that! Supported: ${names mkString " or "}!"))
  }
    .handleNotFound {
      complete((StatusCodes.NotFound, "Not here!"))
    }
    .result()
}
