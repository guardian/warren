package test

import dev.models._
import dev.parser.Parser
import org.joda.time.DateTime
import org.scalatest.{FreeSpec, Matchers}

class ParserTest extends FreeSpec with Matchers {

	"Parser" - {
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
		val personOfControl = PersonOfControl(
			"01234567",
			PersonOfControlData(
				address,
				"123456778901234567789012345677890",
				"individual-person-with-significant-control",
				Links("link-redacted", None),
				DateTime.parse("2018-11-11"),
				None,
				Set("right-to-appoint-and-remove-directors"),
				"Redacted-nationality",
				"Redacted-residence",
				"Mx Red Acted",
				NameElement(Some("Mx."), Some("Red"), None, None, "Acted"),
				DateOfBirth(None, 11, 1911)
			)
		)
		val corporateEntity = CorporateEntity(
			"00223344",
			EntityData(
				address,
				None,
				"123456888901234568889012345688890",
				Identification("Companies Act",  "Company", Some("England"), Some("Companies House"), Some("123456")),
				"corporate-entity-person-with-significant-control",
				Links("link-redacted", None),
				"Redacted Corp Limited",
				Set("ownership-of-shares-75-to-100-percent"),
				DateTime.parse("2018-10-10"),
			)
		)
		val legalPerson = LegalPerson(
			"00887766",
			EntityData(
				address,
				None,
				"000456888901234568889012345688000",
				Identification("English Law",  "Individual", None, None, None),
				"legal-person-person-with-significant-control",
				Links("link-redacted", None),
				"Legal Person",
				Set("significant-influence-or-control"),
				DateTime.parse("2016-06-06"),
			)
		)


		"will serialize a file" in {

			val resourcePath = getClass.getResource("/testData.txt").getPath
			println(resourcePath)
			Parser.parseFileContents(resourcePath) shouldEqual Iterable(personOfControl, corporateEntity, legalPerson)
		}

	}

}
