package com.adminportal.domain;

import java.util.List;


public class DataTransfer {

	private Product product;
	private List<Category> categories;
	private List<ProductAttribute> productAttributes;
	
	public DataTransfer(){}

	public DataTransfer(Product product, List<Category> categories, List<ProductAttribute> productAttributes) {
		super();
		this.product = product;
		this.categories = categories;
		this.productAttributes = productAttributes;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<ProductAttribute> getProductAttributes() {
		return productAttributes;
	}

	public void setProductAttributes(List<ProductAttribute> productAttributes) {
		this.productAttributes = productAttributes;
	}

	
}
