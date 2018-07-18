package net.amygdalum.tanteemmas;

import org.approvaltests.Approvals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import net.amygdalum.tanteemmas.server.Server;
import net.amygdalum.tanteemmas.testutils.ChromeDriverFactory;

public class ApprovalsTest {

	private Server server;
	private WebDriver driver;

	@Before
	public void setup() {
		server = new Server();
		server.startServer();

		// If ChromeDriver is not in your PATH, uncomment this and point to your installation.
		// e.g. it can be downloaded from http://chromedriver.chromium.org/downloads
		//		System.setProperty( "webdriver.chrome.driver", "path/to/chromedriver" );

		driver = ChromeDriverFactory.createNewInstance();
	}

	@Test
	public void check_order() throws Exception {
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

		Approvals.verifyHtml(driver.getPageSource());
	}

	@After
	public void tearDown() {
		driver.quit();
		server.stopServer();
	}
}
