Instrument Input Dependencies (10 min)
======================================
* the reason for test failure is a not recorded input method
    * consider methods to be input, if the result (or the changes in their arguments) is pulled from the tested method (and not pushed into it)
    * examples for inputs: web service calls, results of file read, random numbers, clock queries
* annotate a method with `@Input` (testrecorder treats any state change of this method as input, i.e. arguments AND result)
* ensure that all call sites of the annotated method (i.e. the lines of code calling the method) are covered by `AgentConfig.getPackages`
* start the launch script
* navigate throught the application and generate some more tests (not too few!)
* stop the server and examine the generated tests in the workspace target folder
* now you hopefully can relax: all tests should run