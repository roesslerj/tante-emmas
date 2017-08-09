package net.amygdalum.tanteemmas.server;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CustomerRepo {

	private Map<String, Customer> customers;
	
	public CustomerRepo() {
		customers = new HashMap<>();
	}
	
	public Customer getCustomer(String name) {
		return customers.computeIfAbsent(name, n -> new Customer(name));
	}

	public void registerCustomer(Customer customer) {
		customers.put(customer.name, customer);
	}

	public CustomerRepo init() {
		registerCustomer(createCustomer("Armer Schlucker", BigDecimal.valueOf(5), "poor"));
		registerCustomer(createCustomer("Reicher Schnösel", BigDecimal.valueOf(5000), "rich"));
		registerCustomer(createCustomer("Reicher Pinkel", "rich"));
		registerCustomer(createCustomer("Gerd Grosskunde","rich netprice"));
		registerCustomer(createCustomer("Otto Normalverbraucher"));
		registerCustomer(createCustomer("Michaela Mustermann"));
		return this;
	}

	private Customer createCustomer(String name) {
		Customer customer = new Customer(name);
		return customer;
	}

	private Customer createCustomer(String name, String status) {
		Customer customer = new Customer(name);
		customer.status = status;
		return customer;
	}

	private Customer createCustomer(String name, BigDecimal debt, String status) {
		Customer customer = new Customer(name);
		customer.debt = debt;
		customer.status = status;
		return customer;
	}

}
