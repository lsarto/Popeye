package com.adminportal.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private double shippingWeight;
	private double listPrice;
	private double ourPrice;
	private boolean newProduct=false;
	private boolean sale=false;
	private boolean latest=false;
	private boolean active=true;
	
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<ProductAttribute> productAttributes;
	
	@ManyToOne
	private Category category;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Type type;
	
	@Column(columnDefinition="text")
	private String description;
	private int inStockNumber;
	
	@Transient
	private MultipartFile productCategory;
	@Transient
	private MultipartFile productDetail1;
	@Transient
	private MultipartFile productDetail2;
	@Transient
	private MultipartFile productDetail3;
	@Transient
	private MultipartFile latestImage;

	@OneToMany(mappedBy = "product")
	@JsonIgnore
	private List<ProductToCartItem> productToCartItemList;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getShippingWeight() {
		return shippingWeight;
	}

	public void setShippingWeight(double shippingWeight) {
		this.shippingWeight = shippingWeight;
	}

	public double getListPrice() {
		return listPrice;
	}

	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

	public double getOurPrice() {
		return ourPrice;
	}

	public void setOurPrice(double ourPrice) {
		this.ourPrice = ourPrice;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getInStockNumber() {
		return inStockNumber;
	}

	public void setInStockNumber(int inStockNumber) {
		this.inStockNumber = inStockNumber;
	}

	public MultipartFile getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(MultipartFile productCategory) {
		this.productCategory = productCategory;
	}

	public MultipartFile getProductDetail1() {
		return productDetail1;
	}

	public void setProductDetail1(MultipartFile productDetail1) {
		this.productDetail1 = productDetail1;
	}

	public MultipartFile getProductDetail2() {
		return productDetail2;
	}

	public void setProductDetail2(MultipartFile productDetail2) {
		this.productDetail2 = productDetail2;
	}

	public MultipartFile getProductDetail3() {
		return productDetail3;
	}

	public void setProductDetail3(MultipartFile productDetail3) {
		this.productDetail3 = productDetail3;
	}
	
	public MultipartFile getLatestImage() {
		return latestImage;
	}

	public void setLatestImage(MultipartFile latestImage) {
		this.latestImage = latestImage;
	}

	public List<ProductToCartItem> getProductToCartItemList() {
		return productToCartItemList;
	}

	public void setProductToCartItemList(List<ProductToCartItem> productToCartItemList) {
		this.productToCartItemList = productToCartItemList;
	}

	public boolean isNewProduct() {
		return newProduct;
	}

	public void setNewProduct(boolean newProduct) {
		this.newProduct = newProduct;
	}

	public boolean isSale() {
		return sale;
	}

	public void setSale(boolean sale) {
		this.sale = sale;
	}

	public List<ProductAttribute> getProductAttributes() {
		return productAttributes;
	}

	public void setProductAttributes(List<ProductAttribute> productAttributes) {
		this.productAttributes = productAttributes;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isLatest() {
		return latest;
	}

	public void setLatest(boolean latest) {
		this.latest = latest;
	}


	
}
