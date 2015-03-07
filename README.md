# play-configurable-ningwsplugin

A modified version of the standard NingWSPlugin that adds support for the following configuration properties:

```
// How long (ms) to keep a connection in the pool (default: forever)
ws.ning.maximumConnectionLifeTime

// How long (ms) to keep an idle connection in the pool (default: 1 min)
ws.ning.idleConnectionInPoolTimeout

// How long (ms) to keep an idle WebSocket conn open (default: 15 min)
ws.ning.webSocketIdleTimeout
```

## Compatibility

Works with Play 2.3.8. Should work with older 2.3.x versions, but I'm not sure when they upgraded the AsyncHttpClient dependency in Play. You might hit issues because some methods were renamed in AsyncHttpClient.

## How to use

1. Add a dependency in your sbt build file:

    ```
    "com.github.cb372" %% "play-configurable-ningwsplugin" % "0.1.0"
    ```

2. Add the plugin to your `play.plugins` file:

    ```
    # This priority has to be < 700, so that we are used in preference to the default NingWSPlugin
    100:play.plugins.ConfigurableNingWSPlugin
    ```

3. Add configuration to your `application.conf` as desired. For example:

    ```
    ws.ning.maximumConnectionLifeTime = 60000
    ```
