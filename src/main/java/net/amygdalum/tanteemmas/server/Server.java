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
import net.amygdalum.tanteemmas.external.SimulatedDateSource;
import net.amygdalum.tanteemmas.external.SimulatedDaytimeSource;
import net.amygdalum.tanteemmas.external.SimulatedWeatherSource;

public class Server extends AbstractVerticle {

	private CustomerRepo customers;
	private ProductRepo products;
	private TemplateEngine engine;
	private PriceCalculator prices;

	public Server() {
		engine = HandlebarsTemplateEngine.create().setExtension("html");
		products = new ProductRepo().init();
		customers = new CustomerRepo().init();
		SimulatedDateSource date = new SimulatedDateSource(50);
		SimulatedDaytimeSource daytime = new SimulatedDaytimeSource(50);
		prices = new PriceCalculator(date, daytime, new SimulatedWeatherSource(date));
	}

	public void start() {
		Router router = Router.router(vertx);
		router.route("/:customer/prices").handler(this::prices);
		router.route().handler(this::show);

		HttpServer server = vertx.createHttpServer();
		server.requestHandler(router::accept).listen(8080);
	}

	public void prices(RoutingContext context) {
		try {
			List<Map<String, Object>> priceTable = new ArrayList<>();
			context.data().put("products", priceTable);
			String name = context.request().getParam("customer");
			Customer customer = customers.getCustomer(name);
			for (Map<String, Object> product : products.getProducts()) {
				BigDecimal price = prices.computePrice(customer, product);

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

		engine.render(context, "src/main/resources/index.html", res -> {
			if (res.succeeded()) {
				context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
			} else {
				context.fail(res.cause());
			}
		});
	}

}
