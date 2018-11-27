package utils.aws

import utils.attempt.{AwsSdkFailure, Failure, NotFoundFailure}

object AwsErrors {
  val exceptionToFailure: PartialFunction[Throwable, Failure] = {
    case err: AmazonS3Exception if err.getStatusCode == 404 =>
      NotFoundFailure(err.toString)
    case ase: AmazonServiceException =>
      AwsSdkFailure(ase)
    case sce: SdkClientException =>
      AwsSdkFailure(sce)
  }
}
