package net.amygdalum.tanteemmas.server;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;

public class RecheckTest {

	private WebDriver driver;
	private Recheck re;
	private Server server;

	@Before
	public void setup() {
		server = new Server();
		server.startServer();

		// If ChromeDriver is not in your PATH, uncomment this and point to your installation.
		// e.g. it can be downloaded from http://chromedriver.chromium.org/downloads
		System.setProperty( "webdriver.chrome.driver", "src/test/resources/chromedriver" );

		driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout(-1, TimeUnit.MINUTES);

		re = new RecheckImpl();
	}

	@Test
	public void check_order() throws Exception {
		re.startTest( "check_order" );
		driver.get("http://localhost:8080/logout");
		// login
		driver.findElement(By.name("customer")).sendKeys("Max");
		driver.findElement(By.name("login")).click();

		// test
		driver.findElement(By.xpath("//tr[4]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[4]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[2]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[3]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[2]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[7]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[6]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[5]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[3]/td[3]/a")).click();

		re.check( driver, "final" );
		re.capTest();
	}

	@After
	public void tearDown() {
		driver.quit();
		server.stopServer();
		re.cap();
	}
}
