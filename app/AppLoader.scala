import play.api.ApplicationLoader.Context
import play.api.{Application, ApplicationLoader}
import services.Config

class AppLoader extends ApplicationLoader {
	override def load(context: Context): Application = {
		new AppComponents(context, Config(context.initialConfiguration.underlying), None).application
	}
}
