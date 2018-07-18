package net.amygdalum.tanteemmas.testutils;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverFactory {
	
	private ChromeDriverFactory() {}
	
	public static ChromeDriver createNewInstance( final String... args ) {
		// If ChromeDriver is not in your PATH, uncomment this and point to your installation.
		//		System.setProperty( "webdriver.chrome.driver", "path/to/chromedriver" );
		
		final ChromeOptions opts = new ChromeOptions();
		opts.addArguments(
				// Enable headless mode for faster execution.
				"--headless",
				// Use Chrome in container-based Travis CI environment (see https://docs.travis-ci.com/user/chrome#Sandboxing).
				"--no-sandbox",
				// Fix window size for stable results.
				"--window-size=1200,800" );
		opts.addArguments( args );
		
		ChromeDriver driver = new ChromeDriver( opts );
		// Wait infinitely for page load to complete.
		driver.manage().timeouts().pageLoadTimeout(-1, TimeUnit.MINUTES);
		
		return driver;
	}

}
