package net.amygdalum.tanteemmas.server;

import static java.util.Arrays.asList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;

public class Server extends AbstractVerticle {

	private CustomerRepo customers;
	private ProductRepo products;
	private TemplateEngine engine;
	private PriceCalculator prices;

	public Server() {
		engine = HandlebarsTemplateEngine.create().setExtension("html");
		products = new ProductRepo();
		customers = new CustomerRepo();
		SimulatedDateSource date = new SimulatedDateSource(50);
		SimulatedDaytimeSource daytime = new SimulatedDaytimeSource(50);
		prices = new PriceCalculator(date, daytime, new SimulatedWeatherSource(date));
	}

	public void start() {
		Router router = Router.router(vertx);
		router.route("/init").handler(this::init);
		router.route("/:customer/prices").handler(this::prices);
		router.route().handler(this::show);

		HttpServer server = vertx.createHttpServer();
		server.requestHandler(router::accept).listen(8080);
	}

	public void init(RoutingContext context) {
		try {
			customers.registerCustomer(new Customer("Armer Schlucker", true));
			
			products.registerProduct(createProduct("Regencape", BigDecimal.valueOf(15.0), "rainwear"));
			products.registerProduct(createProduct("Badeanzug", BigDecimal.valueOf(25.0), "summerdress"));
			
			context.next();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, Object> createProduct(String name, BigDecimal base, String... categories) {
		Map<String, Object> product = new HashMap<>();
		product.put("name", name);
		product.put("basePrice", base);
		product.put("categories", new HashSet<>(asList(categories)));
		return product;
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
