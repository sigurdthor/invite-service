package ntile.core.actors

import akka.actor.{ActorSystem, Props}
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.{Materializer, OverflowStrategy}

trait ActorsModule  {

  lazy val (eventQueue, publisher) = Source.queue[Event](100, OverflowStrategy.backpressure)
    .toMat(Sink.asPublisher(true))(Keep.both).run()

  lazy val queueSource = Source.fromPublisher(publisher)


  val inviteActor =  actorSystem.actorOf(Props(classOf[InviteActor], eventQueue, materializer),  "inviteActor")

  implicit def materializer: Materializer
  def actorSystem: ActorSystem

  actorSystem.eventStream.subscribe(inviteActor, classOf[CreateInvite])
}
