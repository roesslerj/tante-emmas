package net.amygdalum.tanteemmas;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import net.amygdalum.tanteemmas.server.Server;
import net.amygdalum.tanteemmas.testutils.ChromeDriverFactory;

public class SeleniumTest {

	private WebDriver driver;
	private Server server;

	@Before
	public void setup() {
		server = new Server();
		server.startServer();

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

		assertEquals("sunscreen 2.02", driver.findElement(By.xpath("(//table//tr[2])[2]")).getText());
	}

	@After
	public void tearDown() {
		driver.quit();
		server.stopServer();
	}
}
