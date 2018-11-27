package test

import dev.models._
import dev.parser.Parser
import org.joda.time.{DateTime, DateTimeZone}

import org.scalatest.{FreeSpec, Matchers}

class ParserTest extends FreeSpec with Matchers {

	"Parser" - {
		"will serialize a file" in {
			val testDate = DateTime.now(DateTimeZone.UTC).minusDays(3)
			val address = Address(
				"redacted",
				None,
				None,
				None,
				None,
				None,
				"A1 2BC",
				"Nowhere",
				None
			)
			val nameElements = NameElement(
				Some("Mx"),
				Some("Red"),
				Some("Acted"),
				None,
				"Acted",
			)
			val links = Links(
				"link-redacted",
				None
			)
			val person = PersonOfControl(
				"12345677890",
				address,
				"123456778901234567789012345677890",
				"individual-person-with-significant-control",
				links,
				DateTime.now(DateTimeZone.UTC).minusDays(3),
				None,
				Set("ight-to-appoint-and-remove-directors"),
				"Redcted-nationality",
				"Redcted-residence",
				"Mx Red Acted",
				nameElements,
				DateOfBirth(None, 2, 2000)
			)

			val fileContents = Parser.importFileContents("/testData.txt")

			Parser.parseFile(fileContents) shouldEqual Iterable(person)
		}

	}

}
