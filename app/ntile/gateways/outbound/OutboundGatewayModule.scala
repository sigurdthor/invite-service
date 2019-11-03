package ntile.gateways.outbound

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import ntile.core.actors.Event
import ntile.gateways.inbound.CommandGateway

import scala.concurrent.ExecutionContext

trait OutboundGatewayModule  {

  import com.softwaremill.macwire._

  val eventProducer = wire[EventProducer]

  def actorSystem: ActorSystem
  def queueSource: Source[Event, NotUsed]

  implicit def ec: ExecutionContext
  implicit def materializer: Materializer
}
