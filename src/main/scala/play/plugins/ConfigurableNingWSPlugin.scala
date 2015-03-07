package play.plugins

import com.ning.http.client.AsyncHttpClientConfig
import play.api.{Configuration, Application}
import play.api.libs.ws.ning.{NingAsyncHttpClientConfigBuilder, NingWSAPI}
import play.api.libs.ws.{WSClientConfig, DefaultWSConfigParser, WSPlugin}

/**
 * A modified version of play.api.libs.ws.ning.NingWSPlugin
 * that allows us to customise the AsyncHttpClient configuration.
 */
class ConfigurableNingWSPlugin(app: Application) extends WSPlugin {

  @volatile var loaded = false

  override lazy val enabled = true

  private val config = new DefaultWSConfigParser(app.configuration, app.classloader).parse()

  private lazy val ningAPI = new ConfigurableNingWSAPI(app, config)

  override def onStart() {
    loaded = true
  }

  override def onStop() {
    if (loaded) {
      ningAPI.resetClient()
      loaded = false
    }
  }

  def api = ningAPI

}

/**
 * A modified version of play.api.libs.ws.ning.NingWSAPI
 * that allows us to customise the AsyncHttpClient configuration.
 *
 * Note: This class has to be inside the play package because
 * `NingWSAPI` includes some `private[play]` members.
 */
class ConfigurableNingWSAPI(app: Application, clientConfig: WSClientConfig) extends NingWSAPI(app, clientConfig) {

  override def buildAsyncClientConfig(wsClientConfig: WSClientConfig): AsyncHttpClientConfig = {
    val ahcConfigBuilder = createAndConfigureAHCConfigBuilder(app.configuration)
    new NingAsyncHttpClientConfigBuilder(wsClientConfig, ahcConfigBuilder).build()
  }

  private def createAndConfigureAHCConfigBuilder(config: Configuration) = {
    val builder = new AsyncHttpClientConfig.Builder()
    config.getInt("ws.ning.maximumConnectionLifeTime").foreach(builder.setMaxConnectionLifeTimeInMs)
    config.getInt("ws.ning.idleConnectionInPoolTimeout").foreach(builder.setIdleConnectionInPoolTimeoutInMs)
    config.getInt("ws.ning.webSocketIdleTimeout").foreach(builder.setWebSocketIdleTimeoutInMs)
    builder
  }

}
