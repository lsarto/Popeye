package com.popeyestore.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.popeyestore.domain.CartItem;
import com.popeyestore.domain.Category;
import com.popeyestore.domain.Type;
import com.popeyestore.domain.Product;
import com.popeyestore.domain.Order;
import com.popeyestore.domain.User;
import com.popeyestore.domain.UserBilling;
import com.popeyestore.domain.UserPayment;
import com.popeyestore.domain.UserShipping;
import com.popeyestore.domain.security.PasswordResetToken;
import com.popeyestore.domain.security.Role;
import com.popeyestore.domain.security.UserRole;
import com.popeyestore.service.CartItemService;
import com.popeyestore.service.CategoryService;
import com.popeyestore.service.TypeService;
import com.popeyestore.service.ProductService;
import com.popeyestore.service.OrderService;
import com.popeyestore.service.UserPaymentService;
import com.popeyestore.service.UserService;
import com.popeyestore.service.UserShippingService;
import com.popeyestore.service.impl.UserSecurityService;
import com.popeyestore.utility.ITConstants;
import com.popeyestore.utility.MailConstructor;
import com.popeyestore.utility.SecurityUtility;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Controller
public class HomeController {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSecurityService userSecurityService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserPaymentService userPaymentService;

	@Autowired
	private UserShippingService userShippingService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private HttpSession session;
	
	
	@PostConstruct
	public void init(){
		List<Type> types = typeService.findAll();
		List<Category> categories = categoryService.findAll();
		session.setAttribute("categories", categories);
		session.setAttribute("types", types);
	}
	
	@RequestMapping("/")
	public String index(Model model, HttpSession session) {
		List<Product> productList = productService.findLatest();
		model.addAttribute("productList", productList);
		model.addAttribute("home", true);
		return "index5";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "customer-register";
	}
	
	@RequestMapping("/contact")
	public String contact(Model model) {
		model.addAttribute("contatti", true);
		
		return "contact";
	}

	@RequestMapping("/myProfile")
	public String myProfile(HttpSession session,
			Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		
		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);

		List<String> stateList = ITConstants.listOfITStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveEdit", true);

		return "customer-account";
	}

	@RequestMapping("/shop-category")
	public String shopCategory(Model model, Principal principal) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		List<Product> productList = productService.findAll();
		List<Type> types = typeService.findAll();
		model.addAttribute("types", types);
		model.addAttribute("productList", productList);
//		model.addAttribute("activeAll",true);
		model.addAttribute("negozio", true);

		return "shop-category";
	}
	
	@RequestMapping("/shop-detail")
	public String shopDetail(@PathParam("id") Long id, Model model, Principal principal) {
		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}

		Product product = productService.findOne(id);
		List<Type> types = typeService.findAll();

		model.addAttribute("types", types);
		model.addAttribute("product", product);

		List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);

		return "shop-detail";
	}

	@RequestMapping("/forgetPassword")
	public String forgetPassword(HttpServletRequest request, @ModelAttribute("email") String email, Model model) {

		model.addAttribute("classActiveForgetPassword", true);

		User user = userService.findByEmail(email);

		if (user == null) {
			model.addAttribute("emailNotExist", true);
			return "customer-register";
		}

		String password = SecurityUtility.randomPassword();

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		userService.save(user);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(newEmail);

		model.addAttribute("forgetPasswordEmailSent", "true");

		return "customer-register";
	}

	@RequestMapping("/listOfCreditCards")
	public String listOfCreditCards(HttpSession session,
			Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);

		return "customer-account";
	}

	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(HttpSession session,
			Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		

		model.addAttribute("listOfCreditcards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);

		return "customer-account";
	}

	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(HttpSession session,
			@ModelAttribute("id") Long creditCardId,
			@ModelAttribute("idShipping") Long shippingAddressId, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		UserShipping userShipping = userShippingService.findById(shippingAddressId);

		if ((user.getId() != userPayment.getUser().getId()) || (user.getId() != userShipping.getUser().getId())) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			UserBilling userBilling = userPayment.getUserBilling();
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("userBilling", userBilling);
			model.addAttribute("userShipping", userShipping);

			List<String> stateList = ITConstants.listOfITStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("classActiveShipping", true);
			// model.addAttribute("listOfCreditCards", true);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			
			

			return "customer-account";
		}
	}

	@RequestMapping(value = "/setDefaultPayment", method = RequestMethod.POST)
	public String setDefaultPayment(@ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal,
			Model model) {
		if (defaultPaymentId == null || defaultPaymentId == 0) {
			model.addAttribute("emptyPaymentId", true);
			return "forward:myProfile";
		}

		User user = userService.findByUsername(principal.getName());
		userService.setUserDefaultPayment(defaultPaymentId, user);

		model.addAttribute("user", user);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);

		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		

		return "customer-account";
	}

	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(@ModelAttribute("id") Long creditCardId,
			@ModelAttribute("idShipping") Long userShippingId, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);
		UserShipping userShipping = userShippingService.findById(userShippingId);

		if (user.getId() != userPayment.getUser().getId() || (user.getId() != userShipping.getUser().getId())) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);

			userPaymentService.removeById(creditCardId);
			userShippingService.removeById(userShippingId);

			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());

			return "customer-account";
		}
	}

	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(HttpSession session,
			Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveBilling", true);

		UserShipping userShipping = new UserShipping();

		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();

		model.addAttribute("userShipping", userShipping);
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);

		List<String> stateList = ITConstants.listOfITStatesCode;
		Collections.sort(stateList);
		

		// List<String> stateList = USConstants.listOfUSStatesCode;
		// Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		

		return "customer-account";
	}

	@RequestMapping(value = "/addNewCreditCard", method = RequestMethod.POST)
	public String addNewCreditCard(@ModelAttribute("userShipping") UserShipping userShipping,
			@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		userService.updateUserBilling(userBilling, userPayment, user);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);

		userService.updateUserShipping(userShipping, user);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);

		return "customer-account";
	}

	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfCreditCards", true);

		UserShipping userShipping = new UserShipping();

		model.addAttribute("userShipping", userShipping);

		List<String> stateList = ITConstants.listOfITStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		

		return "customer-account";
	}

	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email-login") String userEmail,
			@ModelAttribute("name-login") String username, Model model) throws Exception {
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email-login", userEmail);
		model.addAttribute("name-login", username);

		if (userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);

			return "customer-register";
		}

		if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);

			return "customer-register";
		}

		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);

		String password = SecurityUtility.randomPassword();

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(email);

		model.addAttribute("emailSent", "true");

		return "customer-register";
	}

	@RequestMapping("/newUser")
	public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
		PasswordResetToken passToken = userService.getPasswordResetToken(token);

		if (passToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		User user = passToken.getUser();
		String username = user.getUsername();

		UserDetails userDetails = userSecurityService.loadUserByUsername(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("user", user);

		model.addAttribute("classActiveEdit", true);
		return "redirect:/myProfile";
	}

	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
	public String updateUserInfo(HttpSession session,
			@ModelAttribute("user") User user, @ModelAttribute("newPassword") String newPassword,
			Model model) throws Exception {
		User currentUser = userService.findById(user.getId());

		if (currentUser == null) {
			throw new Exception("User not found");
		}

		/* verifica se l'email è già esistente */
		if (userService.findByEmail(user.getEmail()) != null) {
			if (userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
				model.addAttribute("emailExists", true);
				return "customer-account";
			}
		}

		/* verifica se l'username è già esistente */
		if (userService.findByUsername(user.getUsername()) != null) {
			if (userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
				model.addAttribute("usernameExists", true);
				return "customer-account";
			}
		}

		// update password
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if (passwordEncoder.matches(user.getPassword(), dbPassword)) {
				currentUser.setPassword(passwordEncoder.encode(newPassword));
				model.addAttribute("passwordChanged", true);
			} else {
				model.addAttribute("incorrectPassword", true);

				return "customer-account";
			}
		}

		if (user.getFirstName() != null) {
			currentUser.setFirstName(user.getFirstName());
		}

		if (user.getLastName() != null) {
			currentUser.setLastName(user.getLastName());
		}

		if (user.getUsername() != null) {
			currentUser.setUsername(user.getUsername());
		}

		if (user.getEmail() != null) {
			currentUser.setEmail(user.getEmail());
		}

		userService.save(currentUser);

		model.addAttribute("updateSuccess", true);
		model.addAttribute("user", currentUser);
		model.addAttribute("classActiveEdit", true);

		UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		

		return "customer-account";
	}
	
	
	@RequestMapping("/orderList")
	public String orderList(
			Principal principal, Model model) {
			User user = userService.findByUsername(principal.getName());
			model.addAttribute("orderList", user.getOrderList());
			return "customer-orders";
		}
	

	@RequestMapping("/orderDetail")
	public String orderDetail(
			@RequestParam("id") Long orderId, 
			Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		Order order = orderService.findOne(orderId);

		if (order.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			List<CartItem> cartItemList = cartItemService.findByOrder(order);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("user", user);
			model.addAttribute("order", order);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			

			UserShipping userShipping = new UserShipping();
			model.addAttribute("userShipping", userShipping);

			List<String> stateList = ITConstants.listOfITStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveOrders", true);
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("displayOrderDetail", true);

			return "customer-order";
		}
	}
}
