package utils.aws

object S3 {
  def cleanName(name: String): String = name.replace(" ", "-")
}
