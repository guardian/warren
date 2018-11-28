package dev.models

import org.joda.time.DateTime
import play.api.libs.json.Json

sealed trait Entry

case class Proprietor(
	name: String,
	company_registration_num: Option[String],
	proprietorship_category: String,
	country_incorporated: String,
	postcodes: Set[String],
)

case class LandRegistryEntry(
	import_date: String,
	title_number: String,
	tenure: String,

	property: String,
	district: String,
	county: String,
	region: String,
	postcode: Option[String],

	multiple_addresses: Boolean,
	price_paid: Option[Number],
	proprietors: Set[Proprietor],
	date_proprietor_added: Option[DateTime],
	additional_proprietor: Boolean,
)

case class Identification(
	legal_authority: String,
	legal_form: String,
	country_registered: Option[String],
	place_registered: Option[String],
	registration_number: Option[String],
)

object Identification {
	implicit val format = Json.reads[Identification]
}

case class Address(
	address_line_1: String,
	address_line_2: Option[String],
	care_of: Option[String],
	country: Option[String],
	locality: Option[String],
	po_box: Option[String],
	postal_code: String,
	premises: String,
	region: Option[String],
)

object Address {
	implicit val format = Json.reads[Address]
}

case class DateOfBirth(
	day: Option[Int],
	month: Int,
	year: Int,
)

object DateOfBirth {
	implicit val format = Json.reads[DateOfBirth]
}

case class Links(
	 self: String,
	 statement: Option[String],
)

object Links {
	implicit val format = Json.reads[Links]
}

case class NameElement(
	title: Option[String],
	forename: Option[String],
	otherForenames: Option[String],
	middleName: Option[String],
	surname: String,
)

object NameElement {
	implicit val format = Json.reads[NameElement]
}

case class PersonOfControlData(
	address: Address,
	etag: String,
	kind: String,
	links: Links,
	notified_on: DateTime,
	ceased_on: Option[DateTime],
	natures_of_control: Set[String],
	nationality: String,
	country_of_residence: String,
	name: String,
	name_elements: NameElement,
	date_of_birth: DateOfBirth,
)

object PersonOfControlData {
	implicit val format = Json.reads[PersonOfControlData]
}

case class PersonOfControl(
	company_number: String,
	data: PersonOfControlData
) extends Entry

object PersonOfControl {
	implicit val format = Json.reads[PersonOfControl]
}

case class EntityData(
	address: Address,
	ceased_on: Option[DateTime],
	etag: String,
	identification: Identification,
	kind: String,
	links: Links,
	name: String,
	natures_of_control: Set[String],
	notified_on: DateTime,
)

object EntityData {
	implicit val format = Json.reads[EntityData]
}

case class LegalPerson(
	company_number: String,
	data: EntityData,
) extends Entry

object LegalPerson {
	implicit val format = Json.reads[LegalPerson]
}

case class CorporateEntity(
	company_number: String,
	data: EntityData,
) extends Entry

object CorporateEntity {
	implicit val format = Json.reads[CorporateEntity]
}