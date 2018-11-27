package utils

import java.util.Locale

import com.typesafe.config.ConfigValueFactory.fromAnyRef
import services.Config
import utils.aws.AwsCredentials

import scala.util.Try

case class AwsDiscovery(stack: String, stage: String, region: String, config: Config)

object AwsDiscovery extends Logging {
  private val region = readRegion()
  private val credentials = AwsCredentials()
  private val ec2Client = AmazonEC2ClientBuilder.standard().withCredentials(credentials).withRegion(region).build()
  private val ssmClient = AWSSimpleSystemsManagementClientBuilder.standard().withCredentials(credentials).withRegion(region).build()

  def apply(config: Config): AwsDiscovery = {
    val (stack, app, stage) = readStackAppAndStage()

    logger.info(s"AWS discovery stack: $stack app: $app stage: $stage region: $region")
    AwsDiscovery(stack, stage, region, build(stack, app, stage, region, config))
  }

  def build(stack: String, app: String, stage: String, region: String, config: Config): Config = {
    config.copy(
      s3 = config.s3.copy(
        region = region,
        buckets = buildBuckets(config.s3.buckets, stack, stage),
        // Invalidate all minio specific config
        endpoint = None, accessKey = None, secretKey = None
      ),
      database = config.database.copy(
        password = readSSMParameter("db/password", stack, stage)
      ),
      underlying = config.underlying
        .withValue("play.http.secret.key", fromAnyRef(readSSMParameter("app/playSecret", stack, stage)))
    )
  }

  private def buildBuckets(before: BucketConfig, stack: String, stage: String): BucketConfig = {
    val lowerCaseStage = stage.toLowerCase(Locale.UK)

    val after = BucketConfig(
      projects = s"$stack-${before.projects}-$lowerCaseStage",
      ingestions = s"$stack-${before.ingestions}-$lowerCaseStage",
    )

    after
  }

  private def readSSMParameter(name: String, stack: String, stage: String): String = {
    val request = new GetParameterRequest()
      .withName(s"/laundrette/$stack/$stage/$name")
      .withWithDecryption(true)

    val response = ssmClient.getParameter(request)
    response.getParameter.getValue
  }

  private def readStackAppAndStage(): (String, String, String) = {
    val request = new DescribeTagsRequest().withFilters(
      new Filter("resource-type").withValues("instance"),
      new Filter("resource-id").withValues(EC2MetadataUtils.getInstanceId)
    )

    val tags = ec2Client.describeTags(request).getTags.asScala

    (tags.find(_.getKey == "Stack"), tags.find(_.getKey == "App"), tags.find(_.getKey == "Stage")) match {
      case (Some(stack), Some(app), Some(stage)) =>
        (stack.getValue, app.getValue, stage.getValue)

      case _ =>
        throw new IllegalStateException("Unable to find 'Stack', 'App' and 'Stage' tags for current instance")
    }
  }

  private def readRegion(): String = {
    val instanceRegion = Try(Option(EC2MetadataUtils.getEC2InstanceRegion)).toOption.flatten
    val envRegion = sys.env.get("AWS_DEFAULT_REGION")

    (instanceRegion orElse envRegion).getOrElse {
      throw new IllegalStateException("Unable to get region for current instance")
    }
  }
}

