package ntile.gateways.inbound

import akka.actor.ActorSystem
import akka.stream.Materializer
import ntile.core.dao.InviteDao
import ntile.gateways.inbound.rest.{AuthorizationGateway, InviteQueryGateway}
import org.pac4j.play.scala.SecurityComponents
import play.api.Configuration

import scala.concurrent.ExecutionContext

trait InboundGatewayModule extends   {

  import com.softwaremill.macwire._

  lazy val inviteController = wire[InviteQueryGateway]
  lazy val authorizationController = wire[AuthorizationGateway]

  val commandDispatcher = wire[CommandGateway]

  def securityComponents: SecurityComponents
  def configuration: Configuration
  def actorSystem: ActorSystem


  implicit def ec: ExecutionContext
  implicit def materializer: Materializer
}
