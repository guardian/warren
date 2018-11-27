package utils

import play.api.libs.json.{Format, Json}

case class EnvironmentMetadata(stack: String, stage: String, region: String)

object EnvironmentMetadata {
  implicit val format: Format[EnvironmentMetadata] = Json.format[EnvironmentMetadata]
}
