Install Testrecorder (10 min)
=============================

* put testrecorder-0.3.x-jar-with-dependencies.jar in your workspace
* add testrecorder-0.3.x-jar-with-dependencies.jar to your class path
* put `AgentConfig.java` in the package `net.amygdalum.tanteemmas.testrecorder` in your src/main/java folder 
* try to understand the configuration in `AgentConfig.java`
* modify the launch script by adding  `-javaagent:testrecorder-0.3.x-jar-with-dependencies.jar=net.amygdalum.tanteemmas.testrecorder.AgentConfig`
    * eclipse: through run configurations -> TantenteEmmas -> Arguments -> Vm Arguments
    * intellij: TODO
    * other: `java -cp [classpath] -javaagent:[...] io.vertx.core.Launcher run net.amygdalum.tanteemmas.server.Server`
* start the launch script
    * if the message `loading AgentConfig` appears => everything ok
    * else if the message `loading default config` appears => agent has been loaded but configuration not found
    * else => agent has not been loaded => adjust the path to the testrecorder.jar
* stop the application
* annotate `PriceCalculator.computePrice` with `@Recorded`
* start the launch script, the messages should be:
    * `loading AgentConfig`
    * `recording snapshots of ...`
* browse to http://localhost:8080
* navigate through the application
* watch the console for generated tests
* stop the server and examine the generated tests in the workspace target folder
* do not be disappointed => these tests will probably fail