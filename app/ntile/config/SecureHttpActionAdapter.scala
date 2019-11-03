package ntile.config

import org.pac4j.core.context.HttpConstants
import org.pac4j.play.PlayWebContext
import org.pac4j.play.http.DefaultHttpActionAdapter
import play.mvc.{Result, Results}

class SecureHttpActionAdapter extends DefaultHttpActionAdapter {

  override def adapt(code: Int, context: PlayWebContext): Result = {
    if (code == HttpConstants.UNAUTHORIZED) {
      Results.unauthorized("Please authorize")
    } else if (code == HttpConstants.FORBIDDEN) {
      Results.forbidden("Acces denied to resource")
    } else {
      super.adapt(code, context)
    }
  }
}
