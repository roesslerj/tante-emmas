package net.amygdalum.tanteemmas.server;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumTest {

	private WebDriver driver;
	private Server server;

	@Before
	public void setup() {
		server = new Server();
		server.startServer();

		// If ChromeDriver is not in your PATH, uncomment this and point to your installation.
		// e.g. it can be downloaded from http://chromedriver.chromium.org/downloads
		//		System.setProperty( "webdriver.chrome.driver", "path/to/chromedriver" );

		driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(-1, TimeUnit.MINUTES);
	}

	@Test
	public void check_order() throws Exception {
		driver.get("http://localhost:8080/logout");
		// login
		driver.findElement(By.name("customer")).sendKeys("Max");
		driver.findElement(By.name("login")).click();

		// test
		driver.findElement(By.xpath("//tr[4]/td[3]/a")).click();

		assertEquals("sunscreen 2.02", driver.findElement(By.xpath("(//table//tr[2])[2]")).getText());
	}

	@After
	public void tearDown() {
		driver.quit();
		server.stopServer();
	}
}
