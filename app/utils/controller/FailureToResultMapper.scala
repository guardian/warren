package utils.controller

import java.io.{PrintWriter, StringWriter}

import play.api.http.HeaderNames
import play.api.libs.json.JsError
import play.api.mvc.{Result, Results}
import utils.Logging
import utils.attempt.{AlreadySetupFailure, AwsSdkFailure, ClientFailure, ElasticSearchQueryFailure, Failure, HiddenFailure, IllegalStateFailure, JsonParseFailure, MisconfiguredAccount, MissingPermissionFailure, MultipleFailures, Neo4JFailure, NotFoundFailure, OcrPipelineFailure, SecondFactorRequired, TransactionFailure, UnknownFailure, UnsupportedOperationFailure}

object FailureToResultMapper extends Logging {
  def failureToResult(err: Failure): Result = {
    err match {
      case hidden : HiddenFailure =>
        // don't leak the reason that the sensitive failure occurred - log it locally and return an ambiguous error
        logger.warn(s"${hidden.msg}: ${hidden.actualMessage}", hidden.cause.orNull)
        Results.Unauthorized(hidden.msg)
      case MisconfiguredAccount(msg) => Results.Forbidden(msg)
      case SecondFactorRequired(msg) => Results.Unauthorized(msg).withHeaders(HeaderNames.WWW_AUTHENTICATE -> "Pfi2fa")
      case ClientFailure(msg) => Results.BadRequest(msg)
      case NotFoundFailure(msg) => Results.NotFound(msg)
      case UnsupportedOperationFailure(msg) => Results.BadRequest(msg)
      case JsonParseFailure(errors) => Results.BadRequest(JsError.toJson(errors))
      case IllegalStateFailure(msg) => Results.InternalServerError(msg)
      case ElasticSearchQueryFailure(msg) => Results.InternalServerError(msg)
      case TransactionFailure(msg) => Results.InternalServerError(msg)
      case AlreadySetupFailure(msg) => Results.Conflict(msg)
      case neo4j: Neo4JFailure => Results.InternalServerError(neo4j.msg)
      case aws: AwsSdkFailure => Results.InternalServerError(aws.msg)
      case unknown: UnknownFailure => Results.InternalServerError(stackTraceToString(unknown.throwable))
      case MissingPermissionFailure(msg) => Results.Forbidden(msg)
      case MultipleFailures(failures) => failureToResult(failures.head)
      case OcrPipelineFailure(msg) => Results.InternalServerError(msg)
    }
  }
  def stackTraceToString(throwable: Throwable) = {
    val sw = new StringWriter()
    val pw = new PrintWriter(sw)
    throwable.printStackTrace(pw)
    sw.toString()
  }

  def apply(either: Either[Failure, Result]): Result = either.fold(failureToResult, identity)
}
