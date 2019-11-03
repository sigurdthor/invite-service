package ntile.config


import _root_.controllers.AssetsComponents
import com.softwaremill.macwire._
import ntile.core.actors.ActorsModule
import ntile.controllers.ControllersModule
import ntile.gateways.inbound.InboundGatewayModule
import ntile.gateways.outbound.OutboundGatewayModule
import org.pac4j.play.scala.DefaultSecurityComponents
import org.pac4j.play.store.PlayCacheSessionStore
import play.api.ApplicationLoader.Context
import play.api._
import play.api.cache.redis.RedisCacheComponents
import play.api.mvc.BodyParsers.Default
import play.api.routing.Router
import router.Routes

import scala.concurrent.ExecutionContext


/**
  * Application loader that wires up the application dependencies using Macwire
  */
class NtileApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = new NtileComponents(context).application
}


class NtileComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with RedisCacheComponents
  with InboundGatewayModule
  with OutboundGatewayModule
  with ControllersModule
  with ActorsModule
  with SecurityModule
  with AssetsComponents
  with NoHttpFiltersComponents {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  // set up logger
  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  lazy val playSessionStore = new PlayCacheSessionStore(cacheApi( "play" ).javaSync)

  lazy val securityComponents = DefaultSecurityComponents(playSessionStore, config, new Default(playBodyParsers), controllerComponents)

  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor
    val prefix: String = "/"
    wire[Routes]
  }
}


