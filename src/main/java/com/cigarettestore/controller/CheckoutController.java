package com.cigarettestore.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cigarettestore.domain.BillingAddress;
import com.cigarettestore.domain.CartItem;
import com.cigarettestore.domain.Payment;
import com.cigarettestore.domain.ShippingAddress;
import com.cigarettestore.domain.ShoppingCart;
import com.cigarettestore.domain.User;
import com.cigarettestore.domain.UserBilling;
import com.cigarettestore.domain.UserPayment;
import com.cigarettestore.domain.UserShipping;
import com.cigarettestore.service.BillingAddressService;
import com.cigarettestore.service.CartItemService;
import com.cigarettestore.service.PaymentService;
import com.cigarettestore.service.ShippingAddressService;
import com.cigarettestore.service.UserPaymentService;
import com.cigarettestore.service.UserService;
import com.cigarettestore.utility.USConstants;

@Controller
public class CheckoutController {

	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private Payment payment = new Payment();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShippingAddressService shippingAddressService;
	
	@Autowired
	private BillingAddressService billingAddressService;
	
	@Autowired
	private PaymentService paymentService;
	
	@RequestMapping("/shop-checkout1")
	public String ShopCheckout1() {
		return "shop-checkout1";
	}
	
	@RequestMapping(value="/shop-checkout2", method=RequestMethod.POST)
	public String ShopCheckout2(
			@ModelAttribute("firstname") String firstname,
			@ModelAttribute("lastname") String lastname,
			@ModelAttribute("company") String company,
			@ModelAttribute("street") String street,
			@ModelAttribute("city") String city,
			@ModelAttribute("zip") String zip,
			@ModelAttribute("state") String state,
			@ModelAttribute("country") String country,
			@ModelAttribute("phone") String phone,
			@ModelAttribute("email") String email,
			Principal principal, Model model) {
		
		model.addAttribute("firstname", firstname);
		model.addAttribute("lastname", lastname);
		model.addAttribute("company", company);
		model.addAttribute("street", street);
		model.addAttribute("city", city);
		model.addAttribute("zip", zip);
		model.addAttribute("state", state);
		model.addAttribute("country", country);
		model.addAttribute("phone", phone);
		model.addAttribute("email", email);
		
		
		return "shop-checkout2";
	}
	
	@RequestMapping("/shop-checkout3")
	public String ShopCheckout3(
			@ModelAttribute("firstname") String firstname,
			@ModelAttribute("lastname") String lastname,
			@ModelAttribute("company") String company,
			@ModelAttribute("street") String street,
			@ModelAttribute("city") String city,
			@ModelAttribute("zip") String zip,
			@ModelAttribute("state") String state,
			@ModelAttribute("country") String country,
			@ModelAttribute("phone") String phone,
			@ModelAttribute("email") String email,
			@ModelAttribute("payment") String payment,
			Principal principal, Model model) {
		
		model.addAttribute("firstname", firstname);
		model.addAttribute("lastname", lastname);
		model.addAttribute("company", company);
		model.addAttribute("street", street);
		model.addAttribute("city", city);
		model.addAttribute("zip", zip);
		model.addAttribute("state", state);
		model.addAttribute("country", country);
		model.addAttribute("phone", phone);
		model.addAttribute("email", email);
		model.addAttribute("payment", payment);
		
		return "shop-checkout3";
	}
	
	
	@RequestMapping("/shop-checkout4")
	public String ShopCheckout4(
			@RequestParam("id") Long CartId,
			@ModelAttribute("firstname") String firstname,
			@ModelAttribute("lastname") String lastname,
			@ModelAttribute("company") String company,
			@ModelAttribute("street") String street,
			@ModelAttribute("city") String city,
			@ModelAttribute("zip") String zip,
			@ModelAttribute("state") String state,
			@ModelAttribute("country") String country,
			@ModelAttribute("phone") String phone,
			@ModelAttribute("email") String email,
			@ModelAttribute("payment") String payment,
			Principal principal, Model model) {
		
		model.addAttribute("firstname", firstname);
		model.addAttribute("lastname", lastname);
		model.addAttribute("company", company);
		model.addAttribute("street", street);
		model.addAttribute("city", city);
		model.addAttribute("zip", zip);
		model.addAttribute("state", state);
		model.addAttribute("country", country);
		model.addAttribute("phone", phone);
		model.addAttribute("email", email);
		model.addAttribute("payment", payment); 

		
		User user = userService.findByUsername(principal.getName());
		
		UserBilling userBilling = new UserBilling();
		UserShipping userShipping = new UserShipping();
		UserPayment userPayment = new UserPayment();
		
		
		model.addAttribute("userShipping", userShipping);
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		/*model.addAttribute("orderList", user.orderList());*/
		
//		List<String> stateList = USConstants.listOfUSStatesCode;
//			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			/*model.addAttribute("orderList", user.orderList());*/
		
		return "shop-checkout4";
	}
	
	@RequestMapping("/shop-checkout5")
	public String ShopCheckout5() {
		return "shop-checkout5";
	}
	
	
	@RequestMapping("/checkout")
	public String checkout(
			@RequestParam("id") Long cartId,
			@RequestParam(value="missingRequiredField", required=false) boolean missingRequiredField,
			Model model, Principal principal
			){
		User user = userService.findByUsername(principal.getName());
		
		if(cartId != user.getShoppingCart().getId()) {
			return "404";
		}
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		
		if(cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppintCart/cart";
		}
		
		for (CartItem cartItem : cartItemList) {
			if(cartItem.getCigarette().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}
		
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		
		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);
		
		if (userPaymentList.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}
		
		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}
		
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		for(UserShipping userShipping : userShippingList) {
			if(userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}
		
		for (UserPayment userPayment : userPaymentList) {
			if(userPayment.isDefaultPayment()) {
				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}
		
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		
		model.addAttribute("classActiveShipping", true);
		
		if(missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}
		
		return "shop-checkout1";
		
	}
}
