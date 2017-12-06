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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.adminportal.domain.Category;
import com.adminportal.domain.Product;
import com.adminportal.domain.Type;
import com.adminportal.service.CategoryService;
import com.adminportal.service.ProductService;
import com.adminportal.service.TypeService;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	@Autowired
	private TypeService typeService;

	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addProduct(@ModelAttribute("typeSelected") String typeName, Model model) {
		Product product = new Product();
		
		Type type = typeService.findByName(typeName);
		model.addAttribute("type", type);
		model.addAttribute("product", product);
		List<Category> categories = categoryService.findByType(type);
		model.addAttribute("categories", categories);
		
		return "addProduct";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addProductPost(@ModelAttribute("product") Product product, HttpServletRequest request) {
		productService.save(product);

		MultipartFile productCategory = product.getProductCategory();
		MultipartFile productDetail1 = product.getProductDetail1();
		MultipartFile productDetail2 = product.getProductDetail2();
		MultipartFile productDetail3 = product.getProductDetail3();

		try {
			byte[] bytes;
			BufferedOutputStream stream;
			String name;
			
			bytes = productCategory.getBytes();
			name = product.getId() + "-1.png";
			stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/image/product/" + name)));
			stream.write(bytes);
			
		    bytes = productDetail1.getBytes();
			name = product.getId() + "-2.png";
			stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/image/product/" + name)));
			stream.write(bytes);
			
			bytes = productDetail2.getBytes();
			name = product.getId() + "-3.png";
			stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/image/product/" + name)));
			stream.write(bytes);
			
			bytes = productDetail3.getBytes();
			name = product.getId() + "-4.png";
			stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/image/product/" + name)));
			stream.write(bytes);
			
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:productList";
	}
	
	@RequestMapping("/productList")
	public String productListPage(Model model) {
		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);	

		return "productList";
		
	}

	
	@RequestMapping("/productInfo")
	public String productInfo(@RequestParam("id") Long id, Model model) {
		Product product = productService.findOne(id);
		model.addAttribute("product", product);
		
		return "productInfo";
	}
	
	
	@RequestMapping("/updateProduct")
	public String updateProduct(@RequestParam("id") Long id, Model model) {
		Product product = productService.findOne(id);
		model.addAttribute("product", product);
		
		return "updateProduct";
	}
	
	

	@RequestMapping(value="/updateProduct", method=RequestMethod.POST)
	public String updateProductPost(@ModelAttribute("product") Product product, HttpServletRequest request) {
		productService.save(product);
		
		MultipartFile productCategory = product.getProductCategory();
		MultipartFile productDetail1 = product.getProductDetail1();
		MultipartFile productDetail2 = product.getProductDetail2();
		MultipartFile productDetail3 = product.getProductDetail3();
		
		if(!productCategory.isEmpty()) {
			try {
				byte[] bytes = productCategory.getBytes();
				String name = product.getId() + "-1.png";
				
				Files.delete(Paths.get("src/main/resources/static/image/product/"+name));
				
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/image/product/" + name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!productDetail1.isEmpty()) {
			try {
				byte[] bytes = productDetail1.getBytes();
				String name = product.getId() + "-2.png";
				
				Files.delete(Paths.get("src/main/resources/static/image/product/"+name));
				
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/image/product/" + name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!productDetail2.isEmpty()) {
			try {
				byte[] bytes = productDetail2.getBytes();
				String name = product.getId() + "-3.png";
				
				Files.delete(Paths.get("src/main/resources/static/image/product/"+name));
				
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/image/product/" + name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!productDetail3.isEmpty()) {
			try {
				byte[] bytes = productDetail3.getBytes();
				String name = product.getId() + "-4.png";
				
				Files.delete(Paths.get("src/main/resources/static/image/product/"+name));
				
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File("src/main/resources/static/image/product/" + name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return "redirect:/product/productInfo?id="+product.getId();
	}
	
	@RequestMapping(value="/remove", method=RequestMethod.POST)
	public @ResponseBody Boolean remove(
			@RequestBody String id, Model model) {
		try {
			//System.out.println("id: "+id+"\n");
			productService.removeOne(Long.parseLong(id.substring(16)));
        } catch (Exception e){
        	e.printStackTrace();
			return new Boolean(false);
		}
	
		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);
		
		return new Boolean(true);
	}
}
