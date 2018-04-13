package net.amygdalum.tanteemmas.server;

import java.math.BigDecimal;

public class Customer {

	public String name;
	public BigDecimal debt;
	public boolean notCreditable;
	public String status;

	public Customer(String name) {
		this.name = name;
		this.debt = BigDecimal.ZERO;
		this.notCreditable = false;
		status = "";
	}

}
