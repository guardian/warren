package utils.controller

import play.api.libs.json.{JsError, JsSuccess}
import play.api.mvc.Security.AuthenticatedRequest
import play.api.mvc._
import utils.Logging
import utils.attempt.{AuthenticationFailure, Failure}

import scala.concurrent.{ExecutionContext, Future}


trait AuthActionBuilder extends ActionBuilder[AuthRequest, AnyContent] {
  val controllerComponents: ControllerComponents

  final implicit val executionContext: ExecutionContext = controllerComponents.executionContext
  final val parser: BodyParser[AnyContent] = controllerComponents.parsers.default
}

class DefaultAuthActionBuilder(val controllerComponents: ControllerComponents, config: AuthConfig, db: Database) extends AuthActionBuilder with Logging {

  final def invokeBlock[A](request: Request[A], block: AuthRequest[A] => Future[Result]): Future[Result] =
    invokeBlockWithTime(request, block, System.currentTimeMillis()) map FailureToResultMapper.apply

  private def invokeBlockWithTime[A](request: Request[A], block: AuthRequest[A] => Future[Result],
                                           now: Long): Future[Either[Failure, Result]] = {
    implicit val implicitRequest: RequestHeader = request

    request.jwtSession.claimData.validate[Token] match {
      case JsSuccess(token, _) =>
        if (token.loginExpiry > now) {
					val requiresReverification = token.verificationExpiry <= now
					for {
						dbUser <- db.getUser(token.user.id)
					}
          block(new AuthenticatedRequest[A, User](token.user, request)).map { result =>
            val newExpiry = now + config.maxLoginAge.toMillis
            Right(result
							.refreshJwtSession
							.addingToJwtSession(Token.Keys.LOGIN_EXPIRY, newExpiry)
						)
          }
        } else {
          Future.successful(Left(AuthenticationFailure("Login has expired")))
        }
      case JsError(error) =>
        Future.successful(Left(AuthenticationFailure(s"Failed to parse token: $error")))

    }
  }
}
