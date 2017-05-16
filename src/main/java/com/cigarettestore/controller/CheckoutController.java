package com.cigarettestore.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cigarettestore.domain.BillingAddress;
import com.cigarettestore.domain.CartItem;
import com.cigarettestore.domain.Order;
import com.cigarettestore.domain.Payment;
import com.cigarettestore.domain.ShippingAddress;
import com.cigarettestore.domain.ShoppingCart;
import com.cigarettestore.domain.User;
import com.cigarettestore.domain.UserBilling;
import com.cigarettestore.domain.UserPayment;
import com.cigarettestore.domain.UserShipping;
import com.cigarettestore.service.BillingAddressService;
import com.cigarettestore.service.CartItemService;
import com.cigarettestore.service.OrderService;
import com.cigarettestore.service.PaymentService;
import com.cigarettestore.service.ShippingAddressService;
import com.cigarettestore.service.ShoppingCartService;
import com.cigarettestore.service.UserPaymentService;
import com.cigarettestore.service.UserService;
import com.cigarettestore.utility.MailConstructor;
import com.cigarettestore.utility.USConstants;

@Controller
public class CheckoutController {

	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private Payment payment = new Payment();

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ShippingAddressService shippingAddressService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private BillingAddressService billingAddressService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserPaymentService userPaymentService;

	@Autowired
	private PaymentService paymentService;

	@RequestMapping("/shop-checkout1")
	public String ShopCheckout1(@ModelAttribute("shoppingCart") ShoppingCart shoppingCart, Model model,
			Principal principal) {

		UserBilling userBilling = new UserBilling();
		model.addAttribute("userBilling", userBilling);
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		return "shop-checkout1";
	}

	@RequestMapping("/shop-checkout2")
	public String ShopCheckout2(@ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {

		return "shop-checkout2";
	}

	@RequestMapping("/shop-checkout3")
	public String ShopCheckout3(@ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {

		return "shop-checkout3";
	}

	@RequestMapping("/shop-checkout4")
	public String ShopCheckout4(@ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {

		return "shop-checkout4";
	}

	@RequestMapping("/paymentWithCreditCard")
	public String listOfCreditCards(Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		/* model.addAttribute("orderList", user.orderList()); */

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("billingAddress", billingAddress);

		return "shop-checkout4";
	}

	@RequestMapping("/paymentWithNewCreditCard")
	public String addNewCreditCard(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("billingAddress", billingAddress);

		UserShipping userShipping = new UserShipping();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();

		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);

		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();

		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());

		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		model.addAttribute("classActiveShipping", true);

		model.addAttribute("userShipping", userShipping);
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);
		model.addAttribute("payment", payment);
		paymentService.setByUserPayment(userPayment, payment);

		/* model.addAttribute("orderList", user.orderList()); */

		// List<String> stateList = USConstants.listOfUSStatesCode;
		// Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		/* model.addAttribute("orderList", user.orderList()); */

		return "shop-checkout4";
	}

	@RequestMapping(value = "/shop-checkout2", method = RequestMethod.POST)
	public String ShopCheckout2(@ModelAttribute("firstname") String firstname,
			@ModelAttribute("lastname") String lastname, @ModelAttribute("company") String company,
			@ModelAttribute("street") String street, @ModelAttribute("city") String city,
			@ModelAttribute("zip") String zip, @ModelAttribute("state") String state,
			@ModelAttribute("country") String country, @ModelAttribute("phone") String phone,
			@ModelAttribute("email") String email, Principal principal, Model model) {

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

	@RequestMapping(value = "/shop-checkout3", method = RequestMethod.POST)
	public String ShopCheckout3(

			@ModelAttribute("firstname") String firstname, @ModelAttribute("lastname") String lastname,
			@ModelAttribute("company") String company, @ModelAttribute("street") String street,
			@ModelAttribute("city") String city, @ModelAttribute("zip") String zip,
			@ModelAttribute("state") String state, @ModelAttribute("country") String country,
			@ModelAttribute("phone") String phone, @ModelAttribute("email") String email,

			@ModelAttribute("shippingMethod") String shippingMethod, Principal principal, Model model) {

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

		model.addAttribute("shippingMethod", shippingMethod);

		return "shop-checkout3";
	}

	@RequestMapping(value = "/shop-checkout4", method = RequestMethod.POST)
	public String ShopCheckout4(

			@ModelAttribute("firstname") String firstname, @ModelAttribute("lastname") String lastname,
			@ModelAttribute("company") String company, @ModelAttribute("street") String street,
			@ModelAttribute("city") String city, @ModelAttribute("zip") String zip,
			@ModelAttribute("state") String state, @ModelAttribute("country") String country,
			@ModelAttribute("phone") String phone, @ModelAttribute("email") String email,

			@ModelAttribute("payment") String payment, Principal principal, Model model) {

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
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("billingAddress", billingAddress);

		User user = userService.findByUsername(principal.getName());

		UserBilling userBilling = new UserBilling();
		UserShipping userShipping = new UserShipping();
		UserPayment userPayment = new UserPayment();

		model.addAttribute("userShipping", userShipping);
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);

		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		/* model.addAttribute("orderList", user.orderList()); */

		// List<String> stateList = USConstants.listOfUSStatesCode;
		// Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		/* model.addAttribute("orderList", user.orderList()); */

		return "shop-checkout4";
	}

	@RequestMapping(value = "/shop-checkout5", method = RequestMethod.POST)
	public String ShopCheckout5() {
		return "shop-checkout5";
	}

	@RequestMapping("/checkout")
	public String checkout(@RequestParam("id") Long cartId,
			@RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField, Model model,
			Principal principal) {
		User user = userService.findByUsername(principal.getName());

		if (cartId != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

		if (cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppintCart/cart";
		}

		for (CartItem cartItem : cartItemList) {
			if (cartItem.getCigarette().getInStockNumber() < cartItem.getQty()) {
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

		for (UserShipping userShipping : userShippingList) {
			if (userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}

		for (UserPayment userPayment : userPaymentList) {
			if (userPayment.isDefaultPayment()) {
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

		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}

		return "checkout";

	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutPost(@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			@ModelAttribute("billingAddress") BillingAddress billingAddress, @ModelAttribute("payment") Payment payment,
			@ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
			@ModelAttribute("shippingMethod") String shippingMethod, @ModelAttribute("firstname") String firstname,
			@ModelAttribute("lastname") String lastname, @ModelAttribute("company") String company,
			@ModelAttribute("street") String street, @ModelAttribute("city") String city,
			@ModelAttribute("zip") String zip, @ModelAttribute("state") String state,
			@ModelAttribute("country") String country, @ModelAttribute("phone") String phone,
			@ModelAttribute("email") String email,

			Principal principal, Model model) {
		ShoppingCart shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);

		/*
		 * if (billingSameAsShipping.equals("true")) {
		 * billingAddress.setBillingAddressName(shippingAddress.
		 * getShippingAddressName());
		 * billingAddress.setBillingAddressStreet1(shippingAddress.
		 * getShippingAddressStreet1());
		 * billingAddress.setBillingAddressStreet2(shippingAddress.
		 * getShippingAddressStreet2());
		 * billingAddress.setBillingAddressCity(shippingAddress.
		 * getShippingAddressCity());
		 * billingAddress.setBillingAddressState(shippingAddress.
		 * getShippingAddressState());
		 * billingAddress.setBillingAddressCountry(shippingAddress.
		 * getShippingAddressCountry());
		 * billingAddress.setBillingAddressZipcode(shippingAddress.
		 * getShippingAddressZipcode()); }
		 */

		if (shippingAddress.getShippingAddressStreet1().isEmpty() || shippingAddress.getShippingAddressCity().isEmpty()
				|| shippingAddress.getShippingAddressState().isEmpty()
				|| shippingAddress.getShippingAddressName().isEmpty()
				|| shippingAddress.getShippingAddressZipcode().isEmpty() || payment.getCardNumber().isEmpty()
				|| payment.getCvc() == 0 || billingAddress.getBillingAddressStreet1().isEmpty()
				|| billingAddress.getBillingAddressCity().isEmpty() || billingAddress.getBillingAddressState().isEmpty()
				|| billingAddress.getBillingAddressName().isEmpty()
				|| billingAddress.getBillingAddressZipcode().isEmpty())
			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";

		User user = userService.findByUsername(principal.getName());

		Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod,
				user);

		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));

		shoppingCartService.clearShoppingCart(shoppingCart);

		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;

		if (shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		} else {
			estimatedDeliveryDate = today.plusDays(3);
		}

		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);

		return "orderSubmittedPage";
	}

	@RequestMapping("/setPaymentMethod")
	public String setPaymentMethod(@RequestParam("userPaymentId") Long userPaymentId, Principal principal,
			Model model) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		UserBilling userBilling = userPayment.getUserBilling();

		if (userPayment.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			paymentService.setByUserPayment(userPayment, payment);

			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

			billingAddressService.setByUserBilling(userBilling, billingAddress);

			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());

			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			model.addAttribute("shippingAddress", shippingAddress);

			model.addAttribute("classActivePayment", true);

			model.addAttribute("emptyPaymentList", false);

			if (userShippingList.size() == 0) {
				model.addAttribute("emptyShippingList", true);
			} else {
				model.addAttribute("emptyShippingList", false);
			}

			return "checkout";
		}
	}
}
