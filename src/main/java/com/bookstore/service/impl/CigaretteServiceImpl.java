package com.bookstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.Cigarette;
import com.bookstore.repository.CigaretteRepository;
import com.bookstore.service.CigaretteService;

@Service
public class CigaretteServiceImpl implements CigaretteService{
	@Autowired
	private CigaretteRepository cigaretteRepository;
	
	public List<Cigarette> findAll(){
		return (List<Cigarette>) cigaretteRepository.findAll();
	}
}
