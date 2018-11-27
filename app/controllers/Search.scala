package controllers

import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import services.SearchIndex
import utils.controller.NoAuthApiController

import scala.concurrent.ExecutionContext

class Search(searchIndex: SearchIndex, cc: ControllerComponents)(implicit ec: ExecutionContext) extends NoAuthApiController(cc) {
		def search() = AttemptAction { req =>
			val q = req.queryString.getOrElse("q", Seq("")).head

			searchIndex.query(q).map { searchResults =>
				Ok(Json.toJson(searchResults))
			}
		}
}
