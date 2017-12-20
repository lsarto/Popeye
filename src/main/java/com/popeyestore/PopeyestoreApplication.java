package com.popeyestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.popeyestore.domain.Type;
import com.popeyestore.domain.Category;
import com.popeyestore.domain.Product;
import com.popeyestore.domain.User;
import com.popeyestore.domain.security.Role;
import com.popeyestore.domain.security.UserRole;
import com.popeyestore.service.TypeService;
import com.popeyestore.service.UserService;
import com.popeyestore.utility.SecurityUtility;

@SpringBootApplication
public class PopeyestoreApplication implements CommandLineRunner {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TypeService typeService;

	public static void main(String[] args) {
		SpringApplication.run(PopeyestoreApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		User user1 = new User();
		user1.setFirstName("John");
		user1.setLastName("Adams");
		user1.setUsername("j");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
		user1.setEmail("lino.sarto@gmail.com");
		Set<UserRole> userRoles = new HashSet<>();
		Role role1= new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
		
		Type type1 = new Type();
		Type type2 = new Type();
		type1.setName("Type1");
		type2.setName("Type2");
		type1.setProducts(new ArrayList<Product>());
		type2.setProducts(new ArrayList<Product>());
		
		Category category11 = new Category();
		Category category12 = new Category();
		Category category21 = new Category();
		Category category22 = new Category();
		
		category11.setName("category11");
		category12.setName("category12");
		category11.setType(type1);
		category12.setType(type1);
		
		category21.setName("category21");
		category22.setName("category22");
		category21.setType(type2);
		category22.setType(type2);
		
		category11.setQty(0);
		category12.setQty(0);
		category21.setQty(0);
		category22.setQty(0);

		List<Category> categories1 = new ArrayList<>();
		List<Category> categories2 = new ArrayList<>();
		categories1.add(category11);
		categories1.add(category12);
		categories2.add(category21);
		categories2.add(category22);
		
		typeService.createCategory(type1, categories1);
		typeService.createCategory(type2, categories2);
	}
}
