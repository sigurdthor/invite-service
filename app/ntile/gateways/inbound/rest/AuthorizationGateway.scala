package ntile.gateways.inbound.rest

import org.pac4j.core.engine.{DefaultCallbackLogic, DefaultLogoutLogic}
import org.pac4j.core.http.HttpActionAdapter
import org.pac4j.play.PlayWebContext
import org.pac4j.play.scala.SecurityComponents
import play.api.mvc.{AbstractController, RequestHeader}
import play.mvc.Result

import scala.concurrent.{ExecutionContext, Future}

class AuthorizationGateway(cc: SecurityComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def callback = Action.async { implicit requestHeader: RequestHeader =>

    JavaContext.withContext { ctx =>
      val callbackLogic  = new DefaultCallbackLogic[Result, PlayWebContext]
      val playWebContext = new PlayWebContext(ctx, cc.playSessionStore)
      val config = cc.config
      Future {
        callbackLogic.perform(playWebContext, config, config.getHttpActionAdapter.asInstanceOf[HttpActionAdapter[Result, PlayWebContext]], "/", true, false)
      }.map(result => result.asScala())
    }
  }

  def logout = Action.async { implicit requestHeader: RequestHeader =>

    JavaContext.withContext { ctx =>
      val logoutLogic  = new DefaultLogoutLogic[Result, PlayWebContext]
      val playWebContext = new PlayWebContext(ctx, cc.playSessionStore)
      val config = cc.config
      Future {
        logoutLogic.perform(playWebContext, config, config.getHttpActionAdapter.asInstanceOf[HttpActionAdapter[Result, PlayWebContext]], "http://localhost:9000/", "http://localhost:9000/.*", true, true, true)
      }.map(result => result.asScala())
    }
  }
}

object JavaContext {

  import play.mvc.Http
  import play.core.j.JavaHelpers

  def withContext[Status](block: Http.Context => Status)(implicit header: RequestHeader): Status = {
    try {
      val ctx = JavaHelpers.createJavaContext(header, JavaHelpers.createContextComponents())
      Http.Context.current.set(ctx)
      block(ctx)
    }
    finally {
      Http.Context.current.remove()
    }
  }
}
