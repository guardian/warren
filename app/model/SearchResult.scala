package model

import enumeratum.EnumEntry.Snakecase
import enumeratum._
import play.api.libs.json.{Json, OWrites, Writes}

sealed trait ResultKind extends EnumEntry with Snakecase

object ResultKind extends PlayEnum[ResultKind] with PlayJsonEnum[ResultKind] {
	val values = findValues

	case object Person extends ResultKind
	case object Company extends ResultKind
	case object LandTitle extends ResultKind
}

sealed trait SearchResult {
	val kind: ResultKind
}

case class PersonResult(name: String) extends SearchResult {
	override val kind: ResultKind = ResultKind.Person
}

case class CompanyResult(name: String) extends SearchResult {
	override val kind: ResultKind = ResultKind.Company
}

case class LandTitleResult(titleNumber: String, address: String) extends SearchResult {
	override val kind: ResultKind = ResultKind.LandTitle
}

object SearchResult {
	val personWrites = Json.writes[PersonResult]
	val companyWrites = Json.writes[CompanyResult]
	val landTitleWrites = Json.writes[LandTitleResult]

	implicit val writes: OWrites[SearchResult] = {
		case r: PersonResult => personWrites.writes(r)
		case r: CompanyResult => companyWrites.writes(r)
		case r: LandTitleResult => landTitleWrites.writes(r)
	}
}

