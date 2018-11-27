import java.security.Security

import controllers._
import extraction.{PdfOcrExtractor, TikaExtractor}
import org.bouncycastle.jce.provider.BouncyCastleProvider
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.mvc.EssentialFilter
import play.filters.HttpFiltersComponents
import scalikejdbc.config.DBs
import router.Routes
import services.workers.{IngestionWorker, WorkerScheduler}
import services.{Config, Database}
import utils.EnvironmentMetadata
import utils.aws.S3Client
import utils.controller.{AuthActionBuilder, DefaultAuthActionBuilder}

class AppComponents(context: Context, config: Config, environmentMetadata: Option[EnvironmentMetadata])
	extends BuiltInComponentsFromContext(context) with HttpFiltersComponents with AssetsComponents {

	val index = Index

	val cc = controllerComponents

	val search = Search(index, cc)

	override val router = new Routes(
		httpErrorHandler,
		projects,
		datasets,
		sheets,
		users,
		management,
		assets
	)

	override def httpFilters: Seq[EssentialFilter] = super.httpFilters
}
