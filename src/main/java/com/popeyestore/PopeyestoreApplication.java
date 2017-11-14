package com.popeyestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.popeyestore.domain.Category;
import com.popeyestore.domain.SubCategory;
import com.popeyestore.domain.User;
import com.popeyestore.domain.security.Role;
import com.popeyestore.domain.security.UserRole;
import com.popeyestore.service.CategoryService;
import com.popeyestore.service.UserService;
import com.popeyestore.utility.SecurityUtility;

@SpringBootApplication
public class PopeyestoreApplication implements CommandLineRunner {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;

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
		
		Category category1 = new Category();
		Category category2 = new Category();
		category1.setName("Categoria1");
		category2.setName("Categoria2");
		
		SubCategory subCategory11 = new SubCategory();
		SubCategory subCategory12 = new SubCategory();
		SubCategory subCategory21 = new SubCategory();
		SubCategory subCategory22 = new SubCategory();
		subCategory11.setName("subCategory11");
		subCategory12.setName("subCategory12");
		subCategory21.setName("subCategory21");
		subCategory22.setName("subCategory22");
		subCategory11.setQty(2);
		subCategory12.setQty(2);
		subCategory21.setQty(2);
		subCategory22.setQty(2);

		List<SubCategory> subCategories1 = new ArrayList<>();
		List<SubCategory> subCategories2 = new ArrayList<>();
		subCategories1.add(subCategory11);
		subCategories1.add(subCategory12);
		subCategories2.add(subCategory21);
		subCategories1.add(subCategory22);
		
		categoryService.createCategory(category1, subCategories1);
		categoryService.createCategory(category2, subCategories2);
	}
}
