import play.api.ApplicationLoader.Context
import play.api.{Application, ApplicationLoader, Configuration}
import services.{Config, DiscoveryService}
import utils.{AwsDiscovery, EnvironmentMetadata}

class AppLoader extends ApplicationLoader {
	override def load(contextBefore: Context): Application = {
		val (config, envMetadata) = getConfig(contextBefore)
		val contextAfter = contextBefore.copy(initialConfiguration = Configuration(config.underlying))

		new AppComponents(contextAfter, config, envMetadata).application
	}

	private def getConfig(context: Context): (Config, Option[EnvironmentMetadata]) = {
		val before = Config(context.initialConfiguration.underlying)

		before.discovery match {
			case Some(DiscoveryService.AWS) =>
				val AwsDiscovery(stack, stage, region, after) = AwsDiscovery(before)
				(after, Some(EnvironmentMetadata(stack, stage, region)))

			case _ =>
				(before, None)
		}
	}

}
