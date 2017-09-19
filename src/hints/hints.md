* be sure that the previous server was stopped before starting a server again
* at some time you will come to change a launch script to be recording - keep a copy of the script without agent
* store each test generation (even failing ones) in a separate package - sometimes you will feel the need to inspect or reuse older test generations (and they will be overriden by default if not transfered to a separate package)
* read the javadocs for the used annotations. They will give you further hints you to accomplish succesful recordings
* do not rely on few tests, about 100 tests will produce an acceptable coverage  