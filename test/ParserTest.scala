package test

import dev.models._
import dev.parser.Parser
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.{FreeSpec, Matchers}

import scala.io.Source

class ParserTest extends FreeSpec with Matchers {

	"Parser" - {
		val notifiedOn = DateTime.parse("2018-11-11")
		val address = Address(
			"Nowhere",
			None,
			None,
			None,
			None,
			None,
			"123 ABC",
			"000",
			None
		)
		val nameElements = NameElement(
			Some("Mx."),
			Some("Red"),
			None,
			None,
			"Acted",
		)
		val links = Links(
			"link-redacted",
			None
		)
		val person = PersonOfControl(
			"01234567",
			PersonOfControlData(
				address,
				"123456778901234567789012345677890",
				"individual-person-with-significant-control",
				links,
				notifiedOn,
				None,
				Set("right-to-appoint-and-remove-directors"),
				"Redacted-nationality",
				"Redacted-residence",
				"Mx Red Acted",
				nameElements,
				DateOfBirth(None, 11, 1911)
			)
		)

		"will serialize a file" in {
			val resourcePath = getClass.getResource("/testData.txt").getPath
			println(resourcePath)
			Parser.parseFileContents(resourcePath) shouldEqual Iterable(person)
		}

	}

}
