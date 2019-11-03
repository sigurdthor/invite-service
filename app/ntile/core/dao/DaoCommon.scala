package ntile.core.dao

import play.api.Logger
import reactivemongo.api.commands.WriteResult

import scala.concurrent.ExecutionContext

trait DaoCommon {

  def  byRecoverMe[U >: Unit]: PartialFunction[Throwable, U] = {
    case WriteResult.Code(code) => Logger.error(s"Mongo op failed with code $code")
    case WriteResult.Message(msg) => Logger.error(s"Mongo op failed with message $msg")
    case e => Logger.error(e.getMessage)
  }

  implicit def ec: ExecutionContext

}
