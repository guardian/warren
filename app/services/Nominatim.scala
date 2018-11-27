package services

import model.GeoLocation
import play.api.libs.json.JsObject
import play.api.libs.ws.WSClient
import utils.attempt.Attempt

import scala.concurrent.ExecutionContext

class Nominatim(nominatimConfig: NominatimConfig, ws: WSClient)(implicit ec: ExecutionContext) {
	def getGeoLocation(q: String): Attempt[List[GeoLocation]] = {
		val res = ws.url(nominatimConfig.host + "/search")
			.addQueryStringParameters("format" -> "jsonv2")
			.addQueryStringParameters("q" -> q).get()
  			.map { res =>
					res.json.as[List[JsObject]].map { json =>
						GeoLocation((json \ "lat").as[Float], (json \ "lon").as[Float])
					}
				}

		Attempt.fromFutureBlasé(res)
	}
}
