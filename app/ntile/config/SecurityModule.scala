package ntile.config

/**
  * Created by user on 7/3/17.
  */

import org.pac4j.core.config.Config
import org.pac4j.core.context.WebContext
import org.pac4j.core.profile.CommonProfile
import org.pac4j.oidc.profile.OidcProfile
import play.api.Configuration

trait SecurityModule {

  import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
  import org.pac4j.core.client.Clients
  import org.pac4j.oidc.client.OidcClient
  import org.pac4j.oidc.config.OidcConfiguration

  import com.softwaremill.macwire._


  val oidcConfiguration = new OidcConfiguration
  oidcConfiguration.setDiscoveryURI(configuration.get[String]("oidc.discoveryUri"))
  oidcConfiguration.setClientId(configuration.get[String]("oidc.clientId"))
  oidcConfiguration.setSecret(configuration.get[String]("oidc.clientSecret"))

  val oidcClient = new OidcClient[OidcProfile](oidcConfiguration)

  oidcClient.addAuthorizationGenerator((ctx: WebContext, profile: OidcProfile) => {
    def foo(ctx: WebContext, profile: OidcProfile) = {
      profile.addRole("ROLE_USER")
      profile
    }

    foo(ctx, profile)
  })

  val baseUrl: String = configuration.get[String]("baseUrl")
  val clients = new Clients(baseUrl + "/callback", oidcClient)

  val config = new Config(clients)
  config.addAuthorizer("user", new RequireAnyRoleAuthorizer[CommonProfile]("ROLE_USER"))
  config.setHttpActionAdapter(wire[SecureHttpActionAdapter])

  def configuration: Configuration
}
