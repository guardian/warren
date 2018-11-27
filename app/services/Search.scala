package services

import com.sksamuel.elastic4s.http.ElasticClient
import model.SearchResult
import utils.attempt.Attempt

import scala.concurrent.ExecutionContext

class Search(override val client: ElasticClient, elasticsearchConfig: ElasticsearchConfig)(implicit ec: ExecutionContext) extends ElasticsearchSyntax {
	def setup() = {
		for {
			_ <- createIndexIfNotAlreadyExists("persons")
			_ <- createIndexIfNotAlreadyExists("companies")
			_ <- createIndexIfNotAlreadyExists("landTitles")
		} yield {
			()
		}
	}

	def query(q: String): Attempt[List[SearchResult]] = {
		???
	}
}
