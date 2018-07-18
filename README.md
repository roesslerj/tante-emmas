# Tante Emmas Online Shop

This is an example legacy code project, borrowed from [testrecorder](http://testrecorder.amygdalum.net/), to demonstrate various Golden Master Testing tools.

## Setup

Make sure ChromeDriver is [installed](http://chromedriver.chromium.org/downloads/) and in your `PATH`. If not, go to `ChromeDriverFactory` and uncomment the following line:

```java
System.setProperty( "webdriver.chrome.driver", "path/to/chromedriver" );
```

Afterwards, run `mvn test` to execute all tests.
