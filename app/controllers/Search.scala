package controllers

import play.api.libs.json.{JsNumber, JsObject, JsString, Json}
import play.api.mvc.{ControllerComponents, RequestHeader}
import services._
import utils.controller.NoAuthApiController

import scala.concurrent.ExecutionContext

class Search(index: Search, controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends NoAuthApiController(controllerComponents) {
		def search() = AttemptAction { req: RequestHeader =>
			val q = req.queryString.getOrElse("q", Seq("")).head

			index.query(q).map { searchResults =>
				Ok(Json.toJson(searchResults))
			}
		}
}
