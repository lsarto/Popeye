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
	private ProductService cigaretteService;
	
	@RequestMapping(value="/cigarette/removeList", method=RequestMethod.POST)
	public boolean removeList(
			@RequestBody ArrayList<String> bookIdList, Model model
			){
		
		for (String id : bookIdList) {
			String bookId =id.substring(8);
			try {
				cigaretteService.removeOne(Long.parseLong(bookId));
			} catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
}