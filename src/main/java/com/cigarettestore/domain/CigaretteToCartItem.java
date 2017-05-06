package com.cigarettestore.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CigaretteToCartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="cigarette_id")
	private Cigarette book;
	
	@ManyToOne
	@JoinColumn(name="cart_item_id")
	private CartItem cartItem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cigarette getBook() {
		return book;
	}

	public void setBook(Cigarette book) {
		this.book = book;
	}

	public CartItem getCartItem() {
		return cartItem;
	}

	public void setCartItem(CartItem cartItem) {
		this.cartItem = cartItem;
	}

	
	
}