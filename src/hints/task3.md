Eliminate Global Dependencies (10 min)
======================================
* the reason for test failure is a not recorded global variable
    * global variables have to be explicitly configured
* find the unrecorded global variable
* annotate it with `@Global`
* start the launch script
* navigate throught the application and generate some more tests (not too few!)
* stop the server and examine the generated tests in the workspace target folder
* do not be disappointed => some tests will probably fail