package com.ispan.theater.linepay;

import java.math.BigDecimal;
import java.util.List;

public class ProductPackageForm {
	
	private BigDecimal amount;
	
	private String name;
	
	private String id;
	
	private List<ProductForm> products;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ProductForm> getProducts() {
		return products;
	}

	public void setProducts(List<ProductForm> products) {
		this.products = products;
	}


	
}
