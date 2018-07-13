package net.amygdalum.tanteemmas.server;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumLargeTest {

	private WebDriver driver;
	private Server server;

	@Before
	public void setup() {
		server = new Server();
		server.startServer();
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
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
		driver.findElement(By.xpath("//tr[4]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[2]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[3]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[2]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[7]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[6]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[5]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[3]/td[3]/a")).click();

		assertEquals("sunscreen 2.02", driver.findElement(By.xpath("(//table//tr[2])[2]")).getText());
		assertEquals("sunscreen 2.06", driver.findElement(By.xpath("(//table//tr[3])[2]")).getText());
		assertEquals("rain cape 15.45", driver.findElement(By.xpath("(//table//tr[4])[2]")).getText());
		assertEquals("bathing suit 26.53", driver.findElement(By.xpath("(//table//tr[5])[2]")).getText());
		assertEquals("rain cape 15.45", driver.findElement(By.xpath("(//table//tr[6])[2]")).getText());
		assertEquals("10 eggs 2.62", driver.findElement(By.xpath("(//table//tr[7])[2]")).getText());
		assertEquals("flour 0.71", driver.findElement(By.xpath("//table//tr[8]")).getText());
		assertEquals("vodka 10.40", driver.findElement(By.xpath("//table//tr[9]")).getText());
		assertEquals("bathing suit 26.53", driver.findElement(By.xpath("//table//tr[10]")).getText());
	}

	@After
	public void tearDown() {
		driver.quit();
		server.stopServer();
	}

}
