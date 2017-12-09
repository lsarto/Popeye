package com.adminportal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
