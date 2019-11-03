package ntile

import play.api.libs.json.{Json}

package object models {

  case class Invite(email: String, token: String)


  object Invite {

    implicit val InviteFormat = Json.format[Invite]
  }
}
