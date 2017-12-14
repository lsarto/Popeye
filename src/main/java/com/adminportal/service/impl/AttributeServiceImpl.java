package com.adminportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.domain.Product;
import com.adminportal.domain.ProductAttribute;
import com.adminportal.repository.ProductAttributeRepository;
import com.adminportal.service.AttributeService;

@Service
public class AttributeServiceImpl implements AttributeService{
	@Autowired
	private ProductAttributeRepository attributeRepository;
	
	@Override
	public ProductAttribute save(ProductAttribute productAttribute) {
		return attributeRepository.save(productAttribute);
	}

	@Override
	public ProductAttribute findOne(Long id) {
		return attributeRepository.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		attributeRepository.delete(id);
	}

	@Override
	public List<ProductAttribute> findByProduct(Product product) {
		return attributeRepository.findByProduct(product);
	}

}
