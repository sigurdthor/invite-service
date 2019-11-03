package ntile.controllers

import org.pac4j.core.profile.CommonProfile
import org.pac4j.play.scala.{Security, SecurityComponents}

import scala.concurrent.ExecutionContext


class HomeController(val controllerComponents: SecurityComponents)(implicit ec: ExecutionContext) extends Security[CommonProfile] {

  def index = Action { implicit request =>
    //Secure("OidcClient", "user") { implicit request =>
    Ok(ntile.views.html.index())
  }


}
