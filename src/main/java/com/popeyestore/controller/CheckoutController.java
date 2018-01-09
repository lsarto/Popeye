package com.popeyestore.controller;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import org.apache.log4j.Logger;

import static com.popeye.paypal.api.payments.util.SampleConstants.clientID;
import static com.popeye.paypal.api.payments.util.SampleConstants.clientSecret;
import static com.popeye.paypal.api.payments.util.SampleConstants.mode;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.popeyestore.domain.BillingAddress;
import com.popeyestore.domain.CartItem;
import com.popeyestore.domain.Order;
import com.popeyestore.domain.ShippingAddress;
import com.popeyestore.domain.ShoppingCart;
import com.popeyestore.domain.User;
import com.popeyestore.domain.UserBilling;
import com.popeyestore.domain.UserPayment;
import com.popeyestore.domain.UserShipping;
import com.popeyestore.service.BillingAddressService;
import com.popeyestore.service.CartItemService;
import com.popeyestore.service.OrderService;
import com.popeyestore.service.PaymentService;
import com.popeyestore.service.ShippingAddressService;
import com.popeyestore.service.ShoppingCartService;
import com.popeyestore.service.UserPaymentService;
import com.popeyestore.service.UserService;
import com.popeyestore.service.UserShippingService;
import com.popeyestore.utility.MailConstructor;

import com.popeyestore.utility.ITConstants;

@Controller
public class CheckoutController {

	private static final Logger LOGGER = Logger.getLogger(CheckoutController.class);

	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private com.popeyestore.domain.Payment payment = new com.popeyestore.domain.Payment();
	String shippingMethod = "";
	
	private String serialized;

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
	private UserShippingService userShippingService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private BillingAddressService billingAddressService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private UserPaymentService userPaymentService;

	
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
			return "forward:/shoppingCart/cart";
		}

		List<Integer> toRemove = new ArrayList<>();
		Integer i=0;
		for (CartItem cartItem : cartItemList) {
			if(!cartItem.getProduct().isActive()){
				toRemove.add(i);
			} else if(cartItem.getProduct().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
			i++;
		}	
		for(Integer rmvIndx: toRemove){
			cartItemList.remove(rmvIndx.intValue());
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
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}

		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());

		List<String> stateList = ITConstants.listOfITStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		model.addAttribute("classActiveShipping", true);

		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}
		
		return "shop-checkout1";
	}

	
	@RequestMapping(value = "/paymentWithPaypal", method = RequestMethod.POST)
	@ResponseBody
	public String paymentWithPaypal(Principal principal, HttpServletRequest req, HttpServletResponse resp) {
		Map<String, String> map = new HashMap<String, String>();
		Payment createdPayment = null;
		ShoppingCart shoppingCart = null;

		// ### Api Context
		// Pass in a `ApiContext` object to authenticate
		// the call and to send a unique request id
		// (that ensures idempotency). The SDK generates
		// a request id if you do not pass one explicitly.
		APIContext apiContext = new APIContext(clientID, clientSecret, mode);
		if (req.getParameter("payerID") != null) {
			Payment payment = new Payment();
			if (req.getParameter("paymentID") != null) {
				payment.setId(req.getParameter("paymentID"));
			}

			PaymentExecution paymentExecution = new PaymentExecution();
			paymentExecution.setPayerId(req.getParameter("payerID"));
			try {
				createdPayment = payment.execute(apiContext, paymentExecution);
				LOGGER.info("Executed payment with id = "
						+ createdPayment.getId() + " and status = "
						+ createdPayment.getState() + " and created time = "
						+ createdPayment.getCreateTime());
				
				shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();
				ObjectMapper mapper = new ObjectMapper();
				ListIterator<Transaction> iteratorTransaction = createdPayment.getTransactions().listIterator();
				Transaction transaction = iteratorTransaction.next();
				com.paypal.api.payments.ShippingAddress paypalShippingAddress = 
											transaction.getItemList().getShippingAddress();

				shippingAddress.setShippingAddressStreet1(paypalShippingAddress.getLine1());
				shippingAddress.setShippingAddressStreet2(paypalShippingAddress.getLine2());
				shippingAddress.setShippingAddressZipcode(paypalShippingAddress.getPostalCode());
				shippingAddress.setShippingAddressState(paypalShippingAddress.getState());
				shippingAddress.setShippingAddressCountry(paypalShippingAddress.getCountryCode());
				shippingAddress.setShippingAddressName(paypalShippingAddress.getRecipientName());
				shippingAddress.setShippingAddressCity(paypalShippingAddress.getCity());
				
				LocalDate today = LocalDate.now();
				LocalDate estimatedDeliveryDate;
				mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

				if (shippingMethod.equals("groundShipping")) {
					estimatedDeliveryDate = today.plusDays(5);
				} else {
					estimatedDeliveryDate = today.plusDays(3);
				}
				
				String formattedStringDate = estimatedDeliveryDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
				
				List<CartItem> cartItemList = shoppingCart.getCartItemList();
				List<Integer> toRemove = new ArrayList<>();
				Integer i=0;
				for (CartItem cartItem : cartItemList) {
					if(!cartItem.getProduct().isActive()){
						toRemove.add(i);
					}
					
					i++;
				}	
				for(Integer rmvIndx: toRemove){
					cartItemList.remove(rmvIndx.intValue());
				}

				serialized = "{\"cartItemList\":"+mapper.writeValueAsString(cartItemList)+
						", \"estimatedDeliveryDate\":"+mapper.writeValueAsString(formattedStringDate)+"}";
				
				User user = userService.findByUsername(principal.getName());
				Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, shippingMethod, user);
				mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ITALY));
				shoppingCartService.clearShoppingCart(shoppingCart);
				shippingAddress = new ShippingAddress();
				billingAddress = new BillingAddress();
				
			} catch (PayPalRESTException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		} else {
				
			shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();
			ObjectMapper mapper = new ObjectMapper();
			
			String shippingAddressString = req.getParameter("shippingAddress");
			String billingAddressString = req.getParameter("billingAddress");
			String billingSameAsShippingString = req.getParameter("billingSameAsShipping");
			String shippingMethodString = req.getParameter("shippingMethod");
			
			//JSON from String to Object
			try {
				if(shippingAddressString!=null && billingAddressString!=null && billingSameAsShippingString!=null
						&& shippingMethodString!=null){
					shippingAddress = mapper.readValue(shippingAddressString, ShippingAddress.class);
					billingAddress = mapper.readValue(billingAddressString, BillingAddress.class);
					Boolean billingSameAsShipping = mapper.readValue(billingSameAsShippingString, Boolean.class);
					shippingMethod = mapper.readValue(shippingMethodString, String.class);
					
					if (billingSameAsShipping!=null && billingSameAsShipping.equals(new Boolean(true))) {
						billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
						billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
						billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
						billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
						billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
						billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
						billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());
					}
									
					if (shippingAddress.getShippingAddressStreet1().isEmpty() || shippingAddress.getShippingAddressCity().isEmpty()
							|| shippingAddress.getShippingAddressState().isEmpty()
							|| shippingAddress.getShippingAddressName().isEmpty()
							|| shippingAddress.getShippingAddressZipcode().isEmpty()
							|| shippingAddress.getShippingAddressCountry().isEmpty()
							|| billingAddress.getBillingAddressStreet1().isEmpty()
							|| billingAddress.getBillingAddressCity().isEmpty() || billingAddress.getBillingAddressState().isEmpty()
							|| billingAddress.getBillingAddressName().isEmpty()
							|| billingAddress.getBillingAddressCountry().isEmpty()
							|| billingAddress.getBillingAddressZipcode().isEmpty()) {
						return "{\"missField\":\"true\", \"errPath\":\"/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true\"}";
					}
				}
			} catch (JsonParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// ###Details
			// Let's you specify details of a payment amount.
			Details details = new Details();
			details.setShipping("1");
			details.setSubtotal(String.valueOf(shoppingCart.getGrandTotal()));
			details.setTax(String.valueOf(shoppingCart.getGrandTotal().multiply(new BigDecimal(0.06))
					.setScale(2, BigDecimal.ROUND_HALF_UP)));

			// ###Amount
			// Let's you specify a payment amount.
			Amount amount = new Amount();
			amount.setCurrency("EUR");
			// Total must be equal to sum of shipping, tax and subtotal.
			amount.setTotal(String.valueOf(shoppingCart.getGrandTotal()
					.add(shoppingCart.getGrandTotal().multiply(new BigDecimal(0.06)).setScale(2, BigDecimal.ROUND_HALF_UP))
					.add(new BigDecimal(1)).setScale(2, BigDecimal.ROUND_HALF_UP)));
			amount.setDetails(details);

			// ###Transaction
			// A transaction defines the contract of a
			// payment - what is the payment for and who
			// is fulfilling it. Transaction is created with
			// a `Payee` and `Amount` types
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction
					.setDescription("Questa è la descrizione della transazione del pagamento.");

			// ### Items
			ItemList itemList = new ItemList();
			List<Item> items = new ArrayList<Item>();
			for(CartItem cartItem: shoppingCart.getCartItemList()){
				if(cartItem.getProduct().isActive()){
					Item item = new Item();
					item.setName(cartItem.getProduct().getName())
						.setQuantity(String.valueOf(cartItem.getQty())).setCurrency("EUR")
						.setPrice(String.valueOf(cartItem.getProduct().getOurPrice()));
					items.add(item);
				}
			}
			itemList.setItems(items);
			com.paypal.api.payments.ShippingAddress paypalShippingAddress = 
					new com.paypal.api.payments.ShippingAddress();
			paypalShippingAddress.setLine1(shippingAddress.getShippingAddressStreet1());
			paypalShippingAddress.setLine2(shippingAddress.getShippingAddressStreet2());
			paypalShippingAddress.setPostalCode(shippingAddress.getShippingAddressZipcode());
			paypalShippingAddress.setState(shippingAddress.getShippingAddressState());
			paypalShippingAddress.setCountryCode(shippingAddress.getShippingAddressCountry());
			paypalShippingAddress.setRecipientName(shippingAddress.getShippingAddressName());
			paypalShippingAddress.setCity(shippingAddress.getShippingAddressCity());
			
			itemList.setShippingAddress(paypalShippingAddress);
			
			transaction.setItemList(itemList);
			
			
			// The Payment creation API requires a list of
			// Transaction; add the created `Transaction`
			// to a List
			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);

			// ###Payer
			// A resource representing a Payer that funds a payment
			// Payment Method
			// as 'paypal'
			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");

			// ###Payment
			// A Payment Resource; create one using
			// the above types and intent as 'sale'
			Payment payment = new Payment();
			payment.setIntent("sale");
			payment.setPayer(payer);
			payment.setTransactions(transactions);

			// ###Redirect URLs
			RedirectUrls redirectUrls = new RedirectUrls();
			String guid = UUID.randomUUID().toString().replaceAll("-", "");
			redirectUrls.setCancelUrl(req.getScheme() + "://"
					+ req.getServerName() + ":" + req.getServerPort()
					+ req.getContextPath() + "/paymentwithpaypal?guid=" + guid);
			redirectUrls.setReturnUrl(req.getScheme() + "://"
					+ req.getServerName() + ":" + req.getServerPort()
					+ req.getContextPath() + "/paymentwithpaypal?guid=" + guid);
			payment.setRedirectUrls(redirectUrls);

			// Create a payment by posting to the APIService
			// using a valid AccessToken
			// The return object contains the status;
			try {
				createdPayment = payment.create(apiContext);
				LOGGER.info("Created payment with id = "
						+ createdPayment.getId() + " and status = "
						+ createdPayment.getState() + " and created time = "
						+ createdPayment.getCreateTime());
				// ###Payment Approval Url
				Iterator<Links> links = createdPayment.getLinks().iterator();
				while (links.hasNext()) {
					Links link = links.next();
					if (link.getRel().equalsIgnoreCase("approval_url")) {
						req.setAttribute("redirectURL", link.getHref());
					}
				}
				map.put(guid, createdPayment.getId());
				return "{\"paymentID\":\"" + createdPayment.getId()+"\"}";

			} catch (PayPalRESTException e) {
				e.printStackTrace();
			}
		}
		
		return serialized;
	}
	
	@RequestMapping(value = "/orderSubmittedPage", method = RequestMethod.POST)
	public String orderSubmittedPage(@ModelAttribute("cartItemList") String cartItemListString,
			@ModelAttribute("estimatedDeliveryDate") String estimatedDeliveryDateString, 
			Principal principal, Model model) {
		ObjectMapper mapper = new ObjectMapper();
		JavaType listType = mapper
			    .getTypeFactory()
			    .constructCollectionType(List.class, CartItem.class);
		List<CartItem> cartItemList = null;
		String formattedStringDate = null;
		try {
			cartItemList = mapper.readValue(cartItemListString, listType);
			formattedStringDate = mapper.readValue(estimatedDeliveryDateString, String.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocalDate estimatedDeliveryDate = LocalDate.parse(formattedStringDate);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
		
		return "orderSubmittedPage";
	}


	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(@RequestParam("userShippingId") Long userShippingId, Principal principal,
			Model model) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);

		if (userShipping.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			shippingAddressService.setByUserShipping(userShipping, shippingAddress);

			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
			List<Integer> toRemove = new ArrayList<>();
			Integer i=0;
			for (CartItem cartItem : cartItemList) {
				if(!cartItem.getProduct().isActive()){
					toRemove.add(i);
				}
				
				i++;
			}	
			for(Integer rmvIndx: toRemove){
				cartItemList.remove(rmvIndx.intValue());
			}

			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());

			List<String> stateList = ITConstants.listOfITStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			model.addAttribute("shippingAddress", shippingAddress);

			model.addAttribute("classActiveShipping", true);

			if (userPaymentList.size() == 0) {
				model.addAttribute("emptyPaymentList", true);
			} else {
				model.addAttribute("emptyPaymentList", false);
			}

			model.addAttribute("emptyShippingList", false);

			return "shop-checkout1";
		}
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
			List<Integer> toRemove = new ArrayList<>();
			Integer i=0;
			for (CartItem cartItem : cartItemList) {
				if(!cartItem.getProduct().isActive()){
					toRemove.add(i);
				}
				
				i++;
			}	
			for(Integer rmvIndx: toRemove){
				cartItemList.remove(rmvIndx.intValue());
			}

			billingAddressService.setByUserBilling(userBilling, billingAddress);

			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());

			List<String> stateList = ITConstants.listOfITStatesCode;
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

			return "shop-checkout1";
		}
	}
}
