package net.amygdalum.tanteemmas;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import de.retest.image.ExactImageDifferenceCalculator;
import net.amygdalum.tanteemmas.server.Server;
import net.amygdalum.tanteemmas.testutils.ChromeDriverFactory;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;
import ru.yandex.qatools.ashot.shooting.SimpleShootingStrategy;
import ru.yandex.qatools.ashot.shooting.ViewportPastingDecorator;

public class PixelDiffTest {

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
		driver.findElement(By.xpath("//tr[4]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[2]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[3]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[2]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[7]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[6]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[5]/td[3]/a")).click();
		driver.findElement(By.xpath("//tr[3]/td[3]/a")).click();

		// Check
		ImageIO.write( getScreenshot(), "PNG", new File( "target/test-classes/checks/PixelDiffTest.check_order.jpg" ) );

		ExactImageDifferenceCalculator pixelDiff = new ExactImageDifferenceCalculator();
		Assert.assertEquals(1.0, pixelDiff.compare("src/test/resources/checks/PixelDiffTest.check_order.jpg",
				"target/test-classes/checks/PixelDiffTest.check_order.jpg").getMatch(), 0.01);
	}

	@After
	public void tearDown() {
		//		driver.quit();
		server.stopServer();
	}

	public BufferedImage getScreenshot() {
		ShootingStrategy shootingStrategy = new ViewportPastingDecorator(new SimpleShootingStrategy()).withScrollTimeout(100);
		AShot aShot = new AShot().shootingStrategy(shootingStrategy);
		return aShot.takeScreenshot(driver).getImage();
	}
}
