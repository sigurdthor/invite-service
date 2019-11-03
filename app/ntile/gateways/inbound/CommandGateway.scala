package ntile.gateways.inbound

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import com.typesafe.config.ConfigFactory
import ntile.core.actors.CreateInvite
import ntile.models.Invite
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import play.Logger
import play.api.libs.json.Json

class CommandGateway(val actorSystem: ActorSystem, implicit val materializer: Materializer) {

  val config = ConfigFactory.load

  val consumerSettings =
    ConsumerSettings(actorSystem, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers(config getString "kafka.uri")
      .withGroupId("CommittableSourceConsumer")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  Logger.info("Init consumer")

  Consumer.committableSource(consumerSettings, Subscriptions.topics("commands"))
    .runWith(Sink.foreach(msg => {
      println(s"CommittableSourceConsumer consume: $msg")
      val json = Json.parse(msg.record.value())
      val name = (json \ "name").as[String]

      val command = name match {
        case "CreateInvite" => CreateInvite((json \ "payload").as[Invite])
      }

      actorSystem.eventStream.publish(command)

      msg.committableOffset.commitScaladsl()
    }))
}