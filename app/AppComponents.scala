import java.security.Security

import controllers.{AssetsComponents, Search}
import org.bouncycastle.jce.provider.BouncyCastleProvider
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.mvc.EssentialFilter
import play.filters.HttpFiltersComponents
import router.Routes
import services.{Config, ElasticsearchClient, SearchIndex}
import utils.EnvironmentMetadata
import utils.attempt.AttemptAwait._

class AppComponents(context: Context, config: Config, environmentMetadata: Option[EnvironmentMetadata])
	extends BuiltInComponentsFromContext(context) with HttpFiltersComponents with AssetsComponents {

	Security.addProvider(new BouncyCastleProvider())

	val index = ElasticsearchClient(config).await()

	val searchIndex = new SearchIndex(index, config.elasticsearch)

	val cc = controllerComponents

	val search = new controllers.Search(searchIndex, cc)

	override val router = new Routes(
		httpErrorHandler,
		search,
		assets
	)

	override def httpFilters: Seq[EssentialFilter] = super.httpFilters
}
