package play.plugins

import com.ning.http.client.AsyncHttpClient
import org.scalatest._
import org.scalatestplus.play._
import play.api.libs.ws.WS
import play.api.test.FakeApplication

class PluginSpec extends FlatSpec with Matchers with OneAppPerSuite {

  override implicit lazy val app = new FakeApplication(
    additionalPlugins = Seq("play.plugins.ConfigurableNingWSPlugin"),
    additionalConfiguration = Map(
      "ws.ning.maximumConnectionLifeTime" -> 100,
      "ws.ning.idleConnectionInPoolTimeout" -> 200,
      "ws.ning.webSocketIdleTimeout" -> 300
    )
  )

  "App with plugin installed" should "have a properly configured AsyncHttpClient" in {
    val config = WS.client.underlying[AsyncHttpClient].getConfig
    config.getMaxConnectionLifeTimeInMs should be(100)
    config.getIdleConnectionInPoolTimeoutInMs should be(200)
    config.getWebSocketIdleTimeoutInMs should be(300)
  }

}
