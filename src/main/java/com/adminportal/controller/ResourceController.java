package com.adminportal.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adminportal.service.ProductService;

@RestController
public class ResourceController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping(value="/product/removeList", method=RequestMethod.POST)
	public boolean removeList(
			@RequestBody ArrayList<String> productIdList, Model model
			){
		
		for (String id : productIdList) {
			String productId =id.substring(8);
			try {
				productService.removeOne(Long.parseLong(productId));
			} catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
}