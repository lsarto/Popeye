package com.adminportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.adminportal.domain.Type;
import com.adminportal.repository.TypeRepository;
import com.adminportal.service.TypeService;

@Controller
public class HomeController {
	@Autowired
	private TypeService typeService;

	@RequestMapping("/")
	public String index(){
		return "redirect:/home";
	}
	
	@RequestMapping("/home")
	public String home(Model model){
		List<Type> types = typeService.findAll();
		model.addAttribute("types", types);
		return "home";
	}
	
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
}
