package net.amygdalum.tanteemmas.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;
import net.amygdalum.tanteemmas.external.TimeProvider;
import net.amygdalum.tanteemmas.external.SimulatedDateSource;
import net.amygdalum.tanteemmas.external.SimulatedDaytimeSource;
import net.amygdalum.tanteemmas.external.SimulatedWeatherSource;
import net.amygdalum.tanteemmas.sources.DateSource;
import net.amygdalum.tanteemmas.sources.DaytimeSource;
import net.amygdalum.tanteemmas.sources.Weather‬Source;

public class Server extends AbstractVerticle {

	private CustomerRepo customers;
	private ProductRepo products;
	private TemplateEngine engine;

	private TimeProvider millis;
	private DateSource date;
	private DaytimeSource daytime;
	private Weather‬Source weather;

	public Server() {
		engine = HandlebarsTemplateEngine.create().setExtension("html");
		products = new ProductRepo().init();
		customers = new CustomerRepo().init();
		millis = new TimeProvider();
		date = new SimulatedDateSource(millis);
		daytime = new SimulatedDaytimeSource(millis);
		weather = new SimulatedWeatherSource(millis, date);
	}

	public void start() {
		Router router = Router.router(vertx);
		router.route("/speed/:speed").handler(this::speed);
		router.route("/login").handler(this::login);
		router.route("/prices").handler(this::prices);
		router.route("/showPrices").handler(this::showPrices);
		router.route("/showLogin").handler(this::showLogin);
		router.route().handler(this::show);

		HttpServer server = vertx.createHttpServer();
		server.requestHandler(router::accept).listen(8080);
	}

	public void speed(RoutingContext context) {
		long speed = Long.parseLong(context.request().getParam("speed"));
		millis.setSpeed(speed);
		context.reroute("/prices");
	}

	public void login(RoutingContext context) {
		String name = context.request().getParam("customer");
		Customer customer = customers.getCustomer(name);
		PriceCalculator.customer = customer;
		context.reroute("/prices");
	}

	public void prices(RoutingContext context) {
		if (PriceCalculator.customer == null) {
			context.next();
			return;
		}
		try {
			PriceCalculator prices = new PriceCalculator(date, daytime, weather);
			List<Map<String, Object>> priceTable = new ArrayList<>();
			context.data().put("products", priceTable);
			for (Map<String, Object> product : products.getProducts()) {
				BigDecimal price = prices.computePrice(product);

				Map<String, Object> productWithPrice = new HashMap<>(product);
				productWithPrice.put("price", price);
				priceTable.add(productWithPrice);
			}
			context.next();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void show(RoutingContext context) {
		context.data().put("date",date.getDate());
		context.data().put("daytime",daytime.getDaytime());
		context.data().put("weather",weather.getWeather());
		
		if (PriceCalculator.customer == null) {
			context.reroute("/showLogin");
		} else {
			context.reroute("/showPrices");
		}
	}

	public void showPrices(RoutingContext context) {
		context.data().put("user", PriceCalculator.customer.name);

		engine.render(context, "src/main/resources/index.html", res -> {
			if (res.succeeded()) {
				context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
			} else {
				context.fail(res.cause());
			}
		});
	}

	public void showLogin(RoutingContext context) {
		engine.render(context, "src/main/resources/login.html", res -> {
			if (res.succeeded()) {
				context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
			} else {
				context.fail(res.cause());
			}
		});
	}

}
