package services

import com.sksamuel.elastic4s.http.ElasticDsl.{indexExists, _}
import com.sksamuel.elastic4s.http._
import com.sksamuel.elastic4s.http.index.CreateIndexResponse
import com.sksamuel.elastic4s.http.index.admin.IndexExistsResponse
import com.sksamuel.elastic4s.mappings._
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.sniff.Sniffer
import utils.Logging
import utils.attempt.Attempt

import scala.concurrent.ExecutionContext

trait ElasticsearchSyntax { this: Logging =>
  def client: ElasticClient

  implicit def attemptFunctor(implicit ec: ExecutionContext): Functor[Attempt] = new Functor[Attempt] {
    override def map[A, B](fa: Attempt[A])(f: A => B): Attempt[B] = fa.map(f)
  }

  def textKeywordField(name: String): TextField = {
    textField(name: String).fields(com.sksamuel.elastic4s.http.ElasticDsl.keywordField("keyword"))
  }

  implicit def attemptExecutor(implicit ec: ExecutionContext): Executor[Attempt] =
    (client: HttpClient, request: ElasticRequest) => {
      Attempt.fromFutureBlasé(Executor.FutureExecutor(ec).exec(client, request))
    }

  def createIndexIfNotAlreadyExists(indexName: String, mappingDefinition: Option[MappingDefinition] = None)(implicit ec: ExecutionContext): Attempt[CreateIndexResponse] = {
    execute(indexExists(indexName)).flatMap {
      case IndexExistsResponse(false) =>
        execute(createIndex(indexName).mappings(mappingDefinition))

      case _ =>
        logger.info(s"Elasticsearch index $indexName already exists")
        Attempt.Right(CreateIndexResponse(acknowledged = true, shards_acknowledged = true))
    }
  }

  def execute[T, U](r: T)(implicit handler: Handler[T, U], manifest: Manifest[U], ec: ExecutionContext): Attempt[U] =
    client.execute(r).map(_.result)

  def executeNoReturn[T, U](r: T)(implicit handler: Handler[T, U], manifest: Manifest[U], ec: ExecutionContext): Attempt[Unit] =
    client.execute(r).map { _ => () }
}

object ElasticsearchSyntax {
  object NestedField {
    val key = "key"
    val values = "values"
  }
}

object ElasticsearchClient extends Logging {
  def apply(config: Config)(implicit executionContext: ExecutionContext): Attempt[ElasticClient] = apply(List(config.elasticsearch.host))

  def apply(hostnames: List[String])(implicit executionContext: ExecutionContext): Attempt[ElasticClient] = Attempt.catchNonFatalBlasé {
    val hosts = hostnames.map(HttpHost.create)
    val client = RestClient.builder(hosts: _*).setRequestConfigCallback(reqConfigCallback =>
      reqConfigCallback.setConnectionRequestTimeout(5000) // Default is 500. Needed for when we send lots of updates quickly.
    ).build()
    // Sniffer allows the client to discover the other nodes in the cluster and load balancer/route around failure
    Sniffer.builder(client).build()

    ElasticClient.fromRestClient(client)
  }
}