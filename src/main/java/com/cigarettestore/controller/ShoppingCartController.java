package com.cigarettestore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		shoppingCartService.updateShoppingCart(shoppingCart);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		
		return "shop-basket";
	}
	
	@RequestMapping("/addItem")
	public String addItem(
			@ModelAttribute("cigarette") Cigarette cigarette,
			Model model, Principal principal
			) {
		User user = userService.findByUsername(principal.getName());
		cigarette = cigaretteService.findOne(cigarette.getId());
		
		CartItem cartItem = cartItemService.addCigaretteToCartItem(cigarette, user, 1);
		model.addAttribute("addCigaretteSuccess", true);
		
		return "forward:/shop-detail?id="+cigarette.getId();
	}
}
