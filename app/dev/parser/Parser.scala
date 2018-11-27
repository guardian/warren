package dev.parser

import dev.models._

import scala.io.Source
import play.api.libs.json._


object Parser {

	private def importFileContents(fileName: String): Iterator[String] = {
		val bufferedSource = Source.fromFile(fileName)
		val fileContents = bufferedSource.getLines
		bufferedSource.close

		fileContents
	}

	private def parseEntry(entry: JsValue): JsResult[Entry] = {
		val kind = (entry \ "kind").get

		kind match {
			case JsString("individual-person-with-significant-control") => entry.validate[PersonOfControl]
			case JsString("corporate-entity-person-with-significant-control") => entry.validate[CorporateEntity]
			case JsString("legal-person-with-significant-control") => entry.validate[LegalPerson]
			case _ => JsError("Help!")
		}
	}

	def parseFile(fileContents: Iterator[String]): Iterator[Entry] = {
		fileContents.flatMap { line =>
			parseEntry(Json.parse(line)).asOpt
		}
	}
}