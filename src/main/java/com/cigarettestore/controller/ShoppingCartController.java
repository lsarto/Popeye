package com.cigarettestore.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cigarettestore.domain.Cigarette;
import com.cigarettestore.domain.CartItem;
import com.cigarettestore.domain.ShoppingCart;
import com.cigarettestore.domain.User;
import com.cigarettestore.service.CartItemService;
import com.cigarettestore.service.CigaretteService;
import com.cigarettestore.service.ShoppingCartService;
import com.cigarettestore.service.UserService;


@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private CigaretteService cigaretteService;
	
	@RequestMapping("/cart")
	public String shoppingCart(HttpSession session, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		shoppingCartService.updateShoppingCart(shoppingCart);
		
		model.addAttribute("cartItemList", cartItemList);
		session.setAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		session.setAttribute("shoppingCart", shoppingCart);
		
		model.addAttribute("carrello", true);
		
		return "shop-basket";
	}
	
	@RequestMapping("/addItem")
	public String addItem(
			@ModelAttribute("cigarette") Cigarette cigarette,
			Model model, Principal principal
			) {
		User user = userService.findByUsername(principal.getName());
		cigarette = cigaretteService.findOne(cigarette.getId());
		
		if (cigarette.getInStockNumber() < 1) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/shop-detail?id="+cigarette.getId();
		}
		
		CartItem cartItem = cartItemService.addCigaretteToCartItem(cigarette, user, 1);
		cartItem.setQty(1);
		cartItemService.updateCartItem(cartItem);
		model.addAttribute("addCigaretteSuccess", true);
		
		return "forward:/shoppingCart/cart";
	}
	
	@RequestMapping("/updateCartItem")
	public String updateShoppingCart(
			@ModelAttribute("id") Long cartItemId,
			@ModelAttribute("qty") int qty
			) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem);
		
		return "forward:/shoppingCart/cart";
	}
	
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("id") Long id) {
		cartItemService.removeCartItem(cartItemService.findById(id));
		
		return "forward:/shoppingCart/cart";
	}
}
