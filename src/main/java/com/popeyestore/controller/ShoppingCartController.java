package com.popeyestore.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.popeyestore.domain.Product;
import com.popeyestore.domain.CartItem;
import com.popeyestore.domain.ShoppingCart;
import com.popeyestore.domain.User;
import com.popeyestore.service.CartItemService;
import com.popeyestore.service.ProductService;
import com.popeyestore.service.ShoppingCartService;
import com.popeyestore.service.UserService;


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
	private ProductService productService;
	
	@RequestMapping("/cart")
	public String shoppingCart(HttpSession session, Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		shoppingCartService.updateShoppingCart(shoppingCart);
		int numItems=0;
		
		if(cartItemList!=null && !cartItemList.isEmpty()){
			BigDecimal grandTotal = new BigDecimal(0);
			for(CartItem cartItem: cartItemList){
				if(cartItem.getProduct().isActive()){
					numItems++;
					BigDecimal bigDecimal = new BigDecimal(cartItem.getProduct().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));
					bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
					grandTotal = grandTotal.add(bigDecimal);
				}
			}
			if(!grandTotal.equals(shoppingCart.getGrandTotal())){
				model.addAttribute("notEnoughStock", true);
			} else if(grandTotal.equals(new BigDecimal(0))){
				model.addAttribute("emptyCart", true);
			}
		}
		
		model.addAttribute("cartItemList", cartItemList);
		session.setAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		session.setAttribute("shoppingCart", shoppingCart);
		model.addAttribute("numItems", numItems);
		
		model.addAttribute("carrello", true);
		
		return "shop-basket";
	}
	
	@RequestMapping("/addItem")
	public String addItem(
			@ModelAttribute("product") Product product,
			Model model, Principal principal
			) {
		User user = userService.findByUsername(principal.getName());
		product = productService.findOne(product.getId());
		
		if (product.getInStockNumber() < 1) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/shop-detail?id="+product.getId();
		}
		
		CartItem cartItem = cartItemService.addProductToCartItem(product, user, 1);
		cartItem.setQty(1);
		cartItemService.updateCartItem(cartItem);
		model.addAttribute("addProductSuccess", true);
		
		return "forward:/shoppingCart/cart";
	}
	
	@RequestMapping("/updateCartItem")
	public String updateShoppingCart(Model model,
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
