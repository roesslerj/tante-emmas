package net.amygdalum.tanteemmas.server;

import java.util.ArrayList;
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

}
