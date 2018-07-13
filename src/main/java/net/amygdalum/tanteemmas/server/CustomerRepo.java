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
		registerCustomer(createCustomer("Poor", BigDecimal.valueOf(5), "poor"));
		registerCustomer(createCustomer("Min", BigDecimal.valueOf(5), "poor"));
		registerCustomer(createCustomer("Max", BigDecimal.valueOf(5000), "rich"));
		registerCustomer(createCustomer("Rich", "rich"));
		registerCustomer(createCustomer("Big","rich netprice"));
		registerCustomer(createCustomer("Ave"));
		registerCustomer(createCustomer("Nobody"));
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
