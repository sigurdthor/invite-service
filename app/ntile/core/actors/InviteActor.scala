package ntile.core.actors

import akka.actor.{Actor, ActorLogging}
import akka.stream.Materializer
import akka.stream.scaladsl.SourceQueue
import ntile.core.dao.InviteDao
import ntile.models.Invite
import play.Logger
import play.api.libs.json._

case class CreateInvite(invite: Invite)

sealed trait Event

case class InviteCreated(invite: Invite) extends Event

object Event {

  implicit val createdFormat = Json.format[InviteCreated]

  implicit val eventFormat: Writes[Event] = (foo: Event) => {
    val (prod: Product, sub) = foo match {
      case b: InviteCreated => (b, Json.toJson(b)(createdFormat))
    }
    JsObject(Seq("class" -> JsString(prod.productPrefix), "data" -> sub))
  }
}

class InviteActor(eventQueue: SourceQueue[Event], implicit val materializer: Materializer) extends Actor with ActorLogging with InviteDao {

  implicit val ec = context.system.dispatcher

  override def receive = {
    case CreateInvite(invite) =>
      insert(invite) onComplete (_ => eventQueue.offer(InviteCreated(invite)))


    case msg@_ =>
      Logger.info("Message received" + msg)
  }
}
