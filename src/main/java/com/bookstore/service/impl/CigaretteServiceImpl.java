package com.bookstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Cigarette;
import com.bookstore.repository.CigaretteRepository;

@Service
public class CigaretteServiceImpl {
	@Autowired
	private CigaretteRepository cigaretteRepository;
	
	public List<Cigarette> findAll(){
		return (List<Cigarette>) cigaretteRepository.findAll();
	}
}
