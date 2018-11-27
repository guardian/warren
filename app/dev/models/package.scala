package dev

import play.api.libs.json.JodaReads

package object models {
	implicit val yourJodaDateReads = JodaReads.jodaDateReads("yyyy-MM-dd")
}
