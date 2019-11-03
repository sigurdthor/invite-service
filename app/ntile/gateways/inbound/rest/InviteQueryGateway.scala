package ntile.gateways.inbound.rest

import akka.actor.ActorSystem
import akka.stream.Materializer
import ntile.core.dao.InviteDao
import org.pac4j.play.scala.SecurityComponents
import play.api.http.ContentTypes
import play.api.libs.EventSource
import play.api.libs.json.Json
import play.api.mvc.AbstractController

import scala.concurrent.ExecutionContext

class InviteQueryGateway(override val controllerComponents: SecurityComponents,
                         val actorSystem: ActorSystem,
                         implicit val materializer: Materializer)(implicit val ec: ExecutionContext) extends AbstractController(controllerComponents) with InviteDao {


  def invites(offset: Int, limit: Int) = Action.async {
    for {
      count <- count
      invites <- allInvites(offset, limit)
    } yield Ok.chunked(invites.map(invite => Json.toJson(invite)) via EventSource.flow)
      .as(ContentTypes.EVENT_STREAM)
      .withHeaders("Count" -> count.toString)
  }


}
