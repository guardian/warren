package services

import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

case class ElasticsearchConfig(host: String)

case class NominatimConfig(host: String)

case class Config(underlying: com.typesafe.config.Config, nominatim: NominatimConfig, elasticsearch: ElasticsearchConfig)

object Config {
	def apply(raw: com.typesafe.config.Config): Config = Config(
		raw,
		raw.as[NominatimConfig]("nominatim"),
    raw.as[ElasticsearchConfig]("elasticsearch")
	)
}