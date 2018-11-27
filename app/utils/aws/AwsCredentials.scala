package utils.aws

object AwsCredentials {
  def apply(accessKey: Option[String] = None, secretKey: Option[String] = None, profile: Option[String] = None): AWSCredentialsProvider = {
    new AWSCredentialsProviderChain(minioCredentials(accessKey, secretKey) ++ awsCredentials(profile) : _*)
  }

  private def awsCredentials(profile: Option[String]): List[AWSCredentialsProvider] = {
    List(
      new ProfileCredentialsProvider(profile.getOrElse("investigations")),
      InstanceProfileCredentialsProvider.getInstance()
    )
  }

  private def minioCredentials(accessKey: Option[String], secretKey: Option[String]): List[AWSCredentialsProvider] = {
    (accessKey, secretKey) match {
      case (Some(ak), Some(sk)) =>
        List(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ak, sk)))

      case _ =>
        List.empty
    }
  }
}
