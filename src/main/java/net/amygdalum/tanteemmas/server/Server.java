package net.amygdalum.tanteemmas.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;
import net.amygdalum.tanteemmas.external.SimulatedDateSource;
import net.amygdalum.tanteemmas.external.SimulatedDaytimeSource;
import net.amygdalum.tanteemmas.external.SimulatedWeatherSource;
import net.amygdalum.tanteemmas.external.TimeProvider;
import net.amygdalum.tanteemmas.sources.DateSource;
import net.amygdalum.tanteemmas.sources.DaytimeSource;
import net.amygdalum.tanteemmas.sources.WeatherSource;

public class Server extends AbstractVerticle {

	private CustomerRepo customers;
	private ProductRepo products;
	private TemplateEngine engine;
	private List<Map<String, Object>> orders = new ArrayList<>();

	private TimeProvider time;
	private DateSource date;
	private DaytimeSource daytime;
	private WeatherSource weather;

	public Server() {
		engine = HandlebarsTemplateEngine.create().setExtension("html");
		products = new ProductRepo().init();
		customers = new CustomerRepo().init();
		time = new TimeProvider();
		date = new SimulatedDateSource(time);
		daytime = new SimulatedDaytimeSource(time);
		weather = new SimulatedWeatherSource(time, date);
	}

	@Override
	public void start() {
		Router router = Router.router(vertx);
		router.route("/speed/:speed").handler(this::speed);
		router.route("/login").handler(this::login);
		router.route("/logout").handler(this::logout);
		router.route("/order/:product").handler(this::order);
		router.route("/prices").handler(this::prices);
		router.route("/showPrices").handler(this::showPrices);
		router.route("/showLogin").handler(this::showLogin);
		router.route().handler(this::show);

		HttpServer server = vertx.createHttpServer();
		server.requestHandler(router::accept).listen(8080);
	}

	public void speed(RoutingContext context) {
		long speed = Long.parseLong(context.request().getParam("speed"));
		time.setSpeed(speed);
		context.reroute("/prices");
	}

	public void login(RoutingContext context) {
		String name = context.request().getParam("customer");
		Customer customer = customers.getCustomer(name);
		PriceCalculator.customer = customer;
		context.reroute("/prices");
	}

	public void logout(RoutingContext context) {
		PriceCalculator.customer = null;
		context.reroute("/showLogin");
	}

	public void order(RoutingContext context) {
		if (PriceCalculator.customer == null) {
			context.next();
			return;
		}
		PriceCalculator prices = new PriceCalculator(date, daytime, weather);
		String name = context.request().getParam("product");
		Map<String, Object> product = new HashMap<>(products.getProduct(name));
		product.put("price", prices.computePrice(product));
		orders.add(product);
		context.data().put("orders", orders);
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
		context.data().put("date", date.getDate());
		context.data().put("daytime", daytime.getDaytime());
		context.data().put("weather", weather.getWeather());

		if (PriceCalculator.customer == null) {
			context.reroute("/showLogin");
		} else {
			context.reroute("/showPrices");
		}
	}

	public void showPrices(RoutingContext context) {
		context.data().put("speed", time.getSpeed());
		context.data().put("incspeed", time.getSpeed() * 10);
		context.data().put("decspeed", time.getSpeed() / 10);
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
		System.out.println("Showing login.");
		engine.render(context, "src/main/resources/login.html", res -> {
			if (res.succeeded()) {
				context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
			} else {
				context.fail(res.cause());
			}
		});
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(Server.class.getName());
	}
}
