Instrument Output Dependencies (10 min)
======================================
* we have already found out how to capture input of methods but what about output
    * consider methods to be output, that send messages out of your program 
    * examples for outputs: file system writes, console logs, web service calls (PUT, DELETE, POST)
* find a method with output to the file system and record it with `@Recorded`
* decide whether the output method may be annotated with `@Output` or should be specified in the `AgentConfig`
* ensure that all call sites of the annotated method (i.e. the lines of code calling the method) are covered by `AgentConfig.getPackages`
* start the launch script
* navigate throught the application and generate some more tests (not too few!)
* stop the server and examine the generated tests in the workspace target folder
* now you hopefully can relax: all tests should run