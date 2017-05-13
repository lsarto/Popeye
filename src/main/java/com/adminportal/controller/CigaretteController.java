package com.adminportal.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

		MultipartFile cigaretteCategory = cigarette.getCigaretteCategory();
		MultipartFile cigaretteDetail1 = cigarette.getCigaretteDetail1();
		MultipartFile cigaretteDetail2 = cigarette.getCigaretteDetail2();
		MultipartFile cigaretteDetail3 = cigarette.getCigaretteDetail3();

		try {
			byte[] bytes;
			BufferedOutputStream stream;
			String name;
			
			bytes = cigaretteCategory.getBytes();
			name = cigarette.getId() + "-1.png";
			stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/image/cigarette/" + name)));
			stream.write(bytes);
			
		    bytes = cigaretteDetail1.getBytes();
			name = cigarette.getId() + "-2.png";
			stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/image/cigarette/" + name)));
			stream.write(bytes);
			
			bytes = cigaretteDetail2.getBytes();
			name = cigarette.getId() + "-3.png";
			stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/image/cigarette/" + name)));
			stream.write(bytes);
			
			bytes = cigaretteDetail3.getBytes();
			name = cigarette.getId() + "-4.png";
			stream = new BufferedOutputStream(
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
		List<Cigarette> cigaretteList = cigaretteService.findAll();
		model.addAttribute("cigaretteList", cigaretteList);		
		return "cigaretteList";
		
	}
	
	@RequestMapping("/cigaretteInfo")
	public String cigaretteInfo(@RequestParam("id") Long id, Model model) {
		Cigarette cigarette = cigaretteService.findOne(id);
		model.addAttribute("cigarette", cigarette);
		
		return "cigaretteInfo";
	}
	
	
	@RequestMapping("/updateCigarette")
	public String updateCigarette(@RequestParam("id") Long id, Model model) {
		Cigarette cigarette = cigaretteService.findOne(id);
		model.addAttribute("cigarette", cigarette);
		
		return "updateCigarette";
	}
	
	

	@RequestMapping(value="/updateCigarette", method=RequestMethod.POST)
	public String updateCigarettePost(@ModelAttribute("cigarette") Cigarette cigarette, HttpServletRequest request) {
		cigaretteService.save(cigarette);
		
		MultipartFile cigaretteCategory = cigarette.getCigaretteCategory();
		MultipartFile cigaretteDetail1 = cigarette.getCigaretteDetail1();
		MultipartFile cigaretteDetail2 = cigarette.getCigaretteDetail2();
		MultipartFile cigaretteDetail3 = cigarette.getCigaretteDetail3();
		
		if(!cigaretteCategory.isEmpty()) {
			try {
				byte[] bytes = cigaretteCategory.getBytes();
				String name = cigarette.getId() + ".png";
				
				Files.delete(Paths.get("src/main/resources/static/image/cigarette/"+name));
				
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/image/cigarette/" + name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!cigaretteDetail1.isEmpty()) {
			try {
				byte[] bytes = cigaretteDetail1.getBytes();
				String name = cigarette.getId() + ".png";
				
				Files.delete(Paths.get("src/main/resources/static/image/cigarette/"+name));
				
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/image/cigarette/" + name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!cigaretteDetail2.isEmpty()) {
			try {
				byte[] bytes = cigaretteDetail2.getBytes();
				String name = cigarette.getId() + ".png";
				
				Files.delete(Paths.get("src/main/resources/static/image/cigarette/"+name));
				
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/image/cigarette/" + name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!cigaretteDetail3.isEmpty()) {
			try {
				byte[] bytes = cigaretteDetail3.getBytes();
				String name = cigarette.getId() + ".png";
				
				Files.delete(Paths.get("src/main/resources/static/image/cigarette/"+name));
				
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/image/cigarette/" + name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return "redirect:/cigarette/cigaretteInfo?id="+cigarette.getId();
	}
	
	
}
