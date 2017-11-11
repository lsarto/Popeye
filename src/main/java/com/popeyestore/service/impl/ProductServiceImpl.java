package com.popeyestore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.popeyestore.domain.Product;
import com.popeyestore.repository.ProductRepository;
import com.popeyestore.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	public List<Product> findAll() {
		List<Product> productList = (List<Product>) productRepository.findAll();
		List<Product> activeProductList = new ArrayList<>();

		for (Product product : productList) {
			if (product.isActive()) {
				activeProductList.add(product);
			}
		}

		return activeProductList;
	}

	public Product findOne(Long id) {
		return productRepository.findOne(id);
	}

//	public List<Product> findByCategory(String category) {
//		List<Product> productList = productRepository.findByCategory(category);
//
//		List<Product> activeProductList = new ArrayList<>();
//
//		for (Product product : productList) {
//			if (product.isActive()) {
//				activeProductList.add(product);
//			}
//		}
//
//		return activeProductList;
//	}

	public List<Product> blurrySearch(String name) {
		List<Product> productList = productRepository.findByNameContaining(name);
		List<Product> activeProductList = new ArrayList<>();

		for (Product product : productList) {
			if (product.isActive()) {
				activeProductList.add(product);
			}
		}

		return activeProductList;
	}
}
