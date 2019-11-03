package ntile.controllers

import org.pac4j.play.scala.SecurityComponents

import scala.concurrent.ExecutionContext

trait ControllersModule  {

  import com.softwaremill.macwire._


  lazy val homeController = wire[HomeController]


  def securityComponents: SecurityComponents

  implicit def ec: ExecutionContext
}
