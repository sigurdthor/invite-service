package ntile.config

/**
  * Created by user on 7/3/17.
  */

import com.typesafe.config.ConfigFactory
import reactivemongo.api._

import scala.concurrent.Future
import scala.concurrent.duration._

trait MongoModule {

  private lazy val config = ConfigFactory.load

  private lazy val driver = new MongoDriver

  private lazy val customStrategy =
    FailoverStrategy(
      initialDelay = 500 milliseconds,
      retries = 5,
      delayFactor =
        attemptNumber => 1 + attemptNumber * 0.5
    )

  lazy val db: Future[DefaultDB] = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val parsedUri = MongoConnection.parseURI(config getString "mongodb.uri")

    for {
      uri <- Future.fromTry(parsedUri)
      con = driver.connection(uri)
      dn <- Future(uri.db.get)
      db <- con.database(dn, customStrategy)
    } yield db
  }

}
