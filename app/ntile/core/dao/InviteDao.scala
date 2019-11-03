package ntile.core.dao

import akka.stream.Materializer
import akka.stream.scaladsl.Source
import ntile.config.MongoModule
import ntile.models.Invite
import reactivemongo.akkastream.{State, cursorProducer}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, Macros, document}

import scala.concurrent.Future


/**
  * Created by user on 7/3/17.
  */
trait InviteDao extends DaoCommon with MongoModule {

  implicit def inviteReader: BSONDocumentReader[Invite] = Macros.reader[Invite]

  implicit def inviteWriter: BSONDocumentWriter[Invite] = Macros.writer[Invite]

  lazy val invites: Future[BSONCollection] = db.map(_.collection("invites"))

  def allInvites(offset: Int, limit: Int)(implicit m: Materializer): Future[Source[Invite, Future[State]]] = {
    for {
      col <- invites
    } yield col.find(document())
      .skip(offset)
      .cursor[Invite]()
      .documentSource()
      //.throttle(1, 3.second, 1, ThrottleMode.Shaping)
      .limit(limit)
  }

  def insert(invite: Invite) = {
    invites.flatMap(_.insert(invite)) recover byRecoverMe
  }

  def update(invite: Invite) = {
    invites.flatMap(_.update(document("email" -> invite.email), invite)) recover byRecoverMe
  }

  def findByEmail(email: String): Future[Invite] = {
    invites.flatMap(_.find(document("email" -> email)).requireOne[Invite])
  }

  def count: Future[Int] = {
    invites.flatMap(_.count(Some(document())))
  }

  def removeAccepted(email: String) = {
    invites.flatMap(_.remove(document("email" -> email))) recover byRecoverMe
  }
}
