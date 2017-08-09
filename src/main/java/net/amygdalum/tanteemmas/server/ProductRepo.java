package net.amygdalum.tanteemmas.server;

import static java.util.Arrays.asList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ProductRepo {

	private List<Map<String, Object>> products;

	public ProductRepo() {
		products = new ArrayList<>();
	}
	
	public List<Map<String, Object>> getProducts() {
		return products;
	}

	public void registerProduct(Map<String, Object> product) {
		products.add(product);
	}

	public ProductRepo init() {
		registerProduct(createProduct("Regencape", BigDecimal.valueOf(15.0), "rainwear"));
		registerProduct(createProduct("Badeanzug", BigDecimal.valueOf(25.0), "summercollection"));
		registerProduct(createProduct("Sonnencreme", BigDecimal.valueOf(2.0), "summercollection"));
		
		registerProduct(createProduct("Wodka", BigDecimal.valueOf(10.0), "spirits"));

		registerProduct(createProduct("Mehl", BigDecimal.valueOf(0.69), "food"));
		registerProduct(createProduct("10 Eier", BigDecimal.valueOf(2.59), "food"));

		return this;
	}

	private Map<String, Object> createProduct(String name, BigDecimal base, String... categories) {
		Map<String, Object> product = new HashMap<>();
		product.put("name", name);
		product.put("basePrice", base);
		product.put("categories", new HashSet<>(asList(categories)));
		return product;
	}


}
