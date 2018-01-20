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
import com.popeyestore.service.CategoryService;
import com.popeyestore.service.TypeService;
import com.popeyestore.service.UserService;
import com.popeyestore.utility.SecurityUtility;


@SpringBootApplication
public class PopeyestoreApplication implements CommandLineRunner {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TypeService typeService;
	
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
		
//		Type type1 = new Type();
//		Type type2 = new Type();
//		type1.setName("Type1");
//		type2.setName("Type2");
//		type1.setProducts(new ArrayList<Product>());
//		type2.setProducts(new ArrayList<Product>());
		Type elettronica = new Type();
		elettronica.setName("Elettronica");
		elettronica.setProducts(new ArrayList<Product>());
		Type tvEaudio = new Type();
		tvEaudio.setName("Tv e Audio");
		tvEaudio.setProducts(new ArrayList<Product>());
		Type fotografiaEauto = new Type();
		fotografiaEauto.setName("Fotografia e Auto");
		fotografiaEauto.setProducts(new ArrayList<Product>());
		Type elettrodomestici = new Type();
		elettrodomestici.setName("Elettrodomestici");
		elettrodomestici.setProducts(new ArrayList<Product>());

		
//		Category category11 = new Category();
//		Category category12 = new Category();
//		Category category21 = new Category();
//		Category category22 = new Category();
		Category computer = new Category();
		Category periferichePC = new Category();
		Category software = new Category();
		Category smartphone = new Category();
		Category tv = new Category();
		Category videoproiettori = new Category();
		Category accessoriTV = new Category();
		Category decoder = new Category();
		Category macchineFotografiche = new Category();
		Category obiettiviReflex = new Category();
		Category obiettiviMirrorless = new Category();
		Category navigazione = new Category();
		Category cottura = new Category();
		Category frigoriferi = new Category();
		Category lavastoviglie = new Category();
		Category lavatrici = new Category();
		computer.setName("PC");
		periferichePC.setName("periferiche PC");
		software.setName("software");
		smartphone.setName("smartphone");
		tv.setName("tv");
		videoproiettori.setName("videoproiettori");
		accessoriTV.setName("accessotiTV");
		decoder.setName("decoder");
		macchineFotografiche.setName("macchine fotografiche");
		obiettiviReflex.setName("obiettivi reflex");
		obiettiviMirrorless.setName("obiettivi mirrorless");
		navigazione.setName("navigazione");
		cottura.setName("cottura");
		frigoriferi.setName("frigoriferi");
		lavastoviglie.setName("lavastoviglie");
		lavatrici.setName("lavatrici");
		
//		category11.setName("category11");
//		category12.setName("category12");
//		
//		category21.setName("category21");
//		category22.setName("category22");
		
//		typeService.createType(type1);
//		typeService.createType(type2);
		typeService.createType(elettronica);
		typeService.createType(tvEaudio);
		typeService.createType(fotografiaEauto);
		typeService.createType(elettrodomestici);
		typeService.createType(elettrodomestici);


		
//		categoryService.createCategory(category11, type1);
//		categoryService.createCategory(category12, type1);
//		categoryService.createCategory(category21, type2);
//		categoryService.createCategory(category22, type2);
		categoryService.createCategory(computer, elettronica);
		categoryService.createCategory(periferichePC, elettronica);
		categoryService.createCategory(software, elettronica);
		categoryService.createCategory(smartphone, elettronica);
		categoryService.createCategory(tv, tvEaudio);
		categoryService.createCategory(videoproiettori, tvEaudio);
		categoryService.createCategory(accessoriTV, tvEaudio);
		categoryService.createCategory(decoder, tvEaudio);
		categoryService.createCategory(macchineFotografiche, fotografiaEauto);
		categoryService.createCategory(obiettiviReflex, fotografiaEauto);
		categoryService.createCategory(obiettiviMirrorless, fotografiaEauto);
		categoryService.createCategory(navigazione, fotografiaEauto);
		categoryService.createCategory(cottura, elettrodomestici);
		categoryService.createCategory(frigoriferi, elettrodomestici);
		categoryService.createCategory(lavastoviglie, elettrodomestici);
		categoryService.createCategory(lavatrici, elettrodomestici);

	}
}
