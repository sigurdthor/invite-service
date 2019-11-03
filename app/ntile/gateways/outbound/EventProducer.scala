package ntile.gateways.outbound

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import com.typesafe.config.ConfigFactory
import ntile.core.actors.Event
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import play.Logger
import play.api.libs.json.Json

class EventProducer(val queueSource: Source[Event, NotUsed], val actorSystem: ActorSystem)(implicit val materializer: Materializer) {

  val config = ConfigFactory.load

  val producerSettings = ProducerSettings(actorSystem, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers(config getString "kafka.uri")

  queueSource
    .map { elem =>
      println(s"PlainSinkProducer produce: ${elem}")
      new ProducerRecord[Array[Byte], String]("events", Json.toJson(elem).toString())
    }
    .runWith(Producer.plainSink(producerSettings))
}
