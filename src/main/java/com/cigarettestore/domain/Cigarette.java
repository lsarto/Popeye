package com.cigarettestore.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Cigarette {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String model;
	private String brand;
	private String typeOfPuff;
	private String technology;
	private String subOhm;
	private String category;
	private double shippingWeight;
	private double listPrice;
	private double ourPrice;
	private boolean active=true;
	
	@Column(columnDefinition="text")
	private String description;
	private int inStockNumber;
	
	@Transient
	private MultipartFile cigaretteCategory;
	@Transient
	private MultipartFile cigaretteDetail1;
	@Transient
	private MultipartFile cigaretteDetail2;
	@Transient
	private MultipartFile cigaretteDetail3;

	@OneToMany(mappedBy = "cigarette")
	@JsonIgnore
	private List<CigaretteToCartItem> cigaretteToCartItemList;
	
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getTypeOfPuff() {
		return typeOfPuff;
	}

	public void setTypeOfPuff(String typeOfPuff) {
		this.typeOfPuff = typeOfPuff;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getSubOhm() {
		return subOhm;
	}

	public void setSubOhm(String subOhm) {
		this.subOhm = subOhm;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public MultipartFile getCigaretteCategory() {
		return cigaretteCategory;
	}

	public void setCigaretteCategory(MultipartFile cigaretteCategory) {
		this.cigaretteCategory = cigaretteCategory;
	}

	public MultipartFile getCigaretteDetail1() {
		return cigaretteDetail1;
	}

	public void setCigaretteDetail1(MultipartFile cigaretteDetail1) {
		this.cigaretteDetail1 = cigaretteDetail1;
	}

	public MultipartFile getCigaretteDetail2() {
		return cigaretteDetail2;
	}

	public void setCigaretteDetail2(MultipartFile cigaretteDetail2) {
		this.cigaretteDetail2 = cigaretteDetail2;
	}

	public MultipartFile getCigaretteDetail3() {
		return cigaretteDetail3;
	}

	public void setCigaretteDetail3(MultipartFile cigaretteDetail3) {
		this.cigaretteDetail3 = cigaretteDetail3;
	}

	public List<CigaretteToCartItem> getCigaretteToCartItemList() {
		return cigaretteToCartItemList;
	}

	public void setCigaretteToCartItemList(List<CigaretteToCartItem> cigaretteToCartItemList) {
		this.cigaretteToCartItemList = cigaretteToCartItemList;
	}


	
}
