package com.adminportal.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.adminportal.domain.Cigarette;
import com.adminportal.service.CigaretteService;

@Controller
@RequestMapping("/cigarette")
public class CigaretteController {

	@Autowired
	private CigaretteService cigaretteService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addCigarette(Model model) {
		Cigarette cigarette = new Cigarette();
		model.addAttribute("cigarette", cigarette);
		return "addCigarette";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addCigarettePost(@ModelAttribute("cigarette") Cigarette cigarette, HttpServletRequest request) {
		cigaretteService.save(cigarette);

		MultipartFile cigaretteImage = cigarette.getCigaretteImage();

		try {
			byte[] bytes = cigaretteImage.getBytes();
			String name = cigarette.getId() + ".png";
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/image/cigarette/" + name)));
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:cigaretteList";
	}
	
	@RequestMapping("/cigaretteList")
	public String cigaretteList(Model model) {
		/*List<Cigarette> cigaretteList = cigaretteService.findAll();*/
		
		return "cigaretteList";
		
	}

}
