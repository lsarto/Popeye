package com.cigarettestore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cigarettestore.domain.Cigarette;
import com.cigarettestore.domain.User;
import com.cigarettestore.service.CigaretteService;
import com.cigarettestore.service.UserService;



@Controller
public class SearchController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private CigaretteService cigaretteService;

	@RequestMapping("/searchByCategory")
	public String searchByCategory(
			@RequestParam("category") String category,
			Model model, Principal principal
			){
		if(principal!=null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		String classActiveCategory = "active"+category;
		classActiveCategory = classActiveCategory.replaceAll("\\s+", "");
		classActiveCategory = classActiveCategory.replaceAll("&", "");
		model.addAttribute(classActiveCategory, true);
		
		List<Cigarette> cigaretteList = cigaretteService.findByCategory(category);
		
		if (cigaretteList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "shop-category";
		}
		
		model.addAttribute("cigaretteList", cigaretteList);
		
		return "shop-category";
	}
	
	@RequestMapping("/searchCigarette")
	public String searchCigarette(
			@ModelAttribute("keyword") String keyword,
			Principal principal, Model model
			) {
		if(principal!=null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		List<Cigarette> cigaretteList = cigaretteService.blurrySearch(keyword);
		
		if (cigaretteList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "cigaretteshelf";
		}
		
		model.addAttribute("cigaretteList", cigaretteList);
		
		return "cigaretteshelf";
	}
}
