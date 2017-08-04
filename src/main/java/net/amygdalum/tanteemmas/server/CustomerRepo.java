package net.amygdalum.tanteemmas.server;

import java.util.HashMap;
import java.util.Map;

public class CustomerRepo {

	private Map<String, Customer> customers;
	
	public CustomerRepo() {
		customers = new HashMap<>();
	}
	
	public Customer getCustomer(String name) {
		return customers.computeIfAbsent(name, n -> new Customer(name, false));
	}

	public void registerCustomer(Customer customer) {
		customers.put(customer.name, customer);
	}

}
