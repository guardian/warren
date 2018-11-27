package utils.aws

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.file.Path

import com.amazonaws.event
import org.apache.commons.io.IOUtils
import utils.attempt.Attempt

import scala.concurrent.ExecutionContext
import scala.language.implicitConversions

class S3Client(config: S3Config)(implicit executionContext: ExecutionContext) {
  val credentials = AwsCredentials(config.accessKey, config.secretKey)

  val aws = config.endpoint match {
    case Some(minioEndpoint) => AmazonS3ClientBuilder.standard()
      .withEndpointConfiguration(new EndpointConfiguration(minioEndpoint, config.region))
      .withPathStyleAccessEnabled(true)
      .withCredentials(credentials)
      .build()

    case _ => AmazonS3ClientBuilder.standard()
      .withCredentials(credentials)
      .withRegion(config.region)
      .build()
  }

  val tm = TransferManagerBuilder.standard()
    .withS3Client(aws)
    .withMultipartUploadThreshold(100L * 1024 * 1024)
    .withMinimumUploadPartSize(100L * 1024 * 1024)
    .build()

  def attemptS3[T](f: => T): Attempt[T] = Attempt.catchNonFatal(f)(AwsErrors.exceptionToFailure)

  // Minio only works with the deprecated method on the client. This is a copy paste into our code to avoid the warnings
  def doesBucketExist(bucket: String): Boolean = try {
    aws.headBucket(new HeadBucketRequest(bucket))
    true
  } catch {
    case ase: AmazonServiceException if ase.getStatusCode == 301 || ase.getStatusCode == 403 =>
      true

    case ase: AmazonServiceException if ase.getStatusCode == 404 =>
      false
  }

  // TODO MRB: these should all be attempty
  def putObjectSync(bucket: String, key: String, contentType: Option[String], contentLength: Long, is: InputStream): PutObjectResult = {
    val metadata = createMetadata(contentType, Some(contentLength))
    val request = new PutObjectRequest(bucket, key, is, metadata)

    aws.putObject(request)
  }

  def putObjectSync(bucket: String, key: String, contentType: Option[String], file: Path): Attempt[PutObjectResult] = attemptS3 {
    val metadata = createMetadata(contentType)
    val request = new PutObjectRequest(bucket, key, file.toFile).withMetadata(metadata)

    aws.putObject(request)
  }

  def putLargeObject(bucket: String, key: String, contentType: Option[String], file: Path,
                     updateCallback: event.ProgressEvent => Unit = _ => ()): UploadResult = {
    val metadata = createMetadata(contentType)
    val request = new PutObjectRequest(bucket, key, file.toFile).withMetadata(metadata)

    val progressListener = new S3ProgressListener{
      override def onPersistableTransfer(persistableTransfer: PersistableTransfer): Unit = {}
      override def progressChanged(progressEvent: event.ProgressEvent): Unit = updateCallback(progressEvent)
    }

    tm.upload(request, progressListener).waitForUploadResult()
  }

  def putObjectSync(bucket: String, key: String, contentType: Option[String], data: Array[Byte]): PutObjectResult = {
    val metadata = createMetadata(contentType, Some(data.length))
    val request = new PutObjectRequest(bucket, key, new ByteArrayInputStream(data), metadata)

    aws.putObject(request)
  }

  def putObject(bucket: String, key: String, contentType: Option[String], data: Array[Byte]): Attempt[PutObjectResult] = attemptS3 {
    putObjectSync(bucket, key, contentType, data)
  }

  def listObjects(bucket: String, prefix: String): Attempt[ObjectListing] = attemptS3(aws.listObjects(bucket, prefix))

  def getObjectBytes(bucket: String, key: String): Attempt[Array[Byte]] = attemptS3 {
    aws.getObject(bucket, key).getObjectContent
  }.map { IOUtils.toByteArray }

  private def createMetadata(contentType: Option[String], contentLength: Option[Long] = None): ObjectMetadata = {
    val metadata = new ObjectMetadata()
    contentType.foreach(metadata.setContentType)
    contentLength.foreach(metadata.setContentLength)

    metadata
  }
}
