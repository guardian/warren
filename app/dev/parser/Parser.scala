package dev.parser

import dev.models._

import scala.io.Source
import play.api.libs.json._


object Parser {
	private def parseEntry(entry: JsValue): JsResult[Entry] = {
		val kind = (entry \ "data" \ "kind").asOpt[String]

		kind match {
			case Some("individual-person-with-significant-control") => entry.validate[PersonOfControl]
			case Some("corporate-entity-person-with-significant-control") => entry.validate[CorporateEntity]
			case Some("legal-person-person-with-significant-control") => entry.validate[LegalPerson]
			case _ => JsError("Help!")
		}
	}

	def parseFileContents(fileName: String): List[Entry] = {
		val bufferedSource = Source.fromFile(fileName)

		try {
			bufferedSource.getLines.flatMap { line =>
				parseEntry(Json.parse(line)).asOpt
			}.toList
		} finally {
			bufferedSource.close
		}
	}
}