package utils.controller

import play.api.mvc._
import utils.attempt.{Attempt, Failure}

import scala.concurrent.Future
import scala.language.higherKinds

protected abstract class AbstractApiController[+R[_]](actionBuilder: ActionBuilder[R, AnyContent]) extends BaseControllerHelpers {
  object AttemptAction {
    private def async[A](bodyParser: BodyParser[A])(thing: R[A] => Future[Either[Failure, Result]]): Action[A] =
      actionBuilder.async(bodyParser) { request =>
        thing(request).map(FailureToResultMapper.apply)(defaultExecutionContext)
      }

    def apply(thing: => Attempt[Result]): Action[AnyContent] =
      apply(actionBuilder.parser)(_ => thing)
    def apply(thing: R[AnyContent] => Attempt[Result]): Action[AnyContent] =
      apply(actionBuilder.parser)(thing)
    def apply[A](bodyParser: BodyParser[A])(thing: R[A] => Attempt[Result]): Action[A] =
      async(bodyParser)(thing andThen (att => att.asFuture(defaultExecutionContext)))
  }
}
