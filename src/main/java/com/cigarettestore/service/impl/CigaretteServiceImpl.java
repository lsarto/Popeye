package com.cigarettestore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cigarettestore.domain.Cigarette;
import com.cigarettestore.repository.CigaretteRepository;
import com.cigarettestore.service.CigaretteService;

@Service
public class CigaretteServiceImpl implements CigaretteService{
	@Autowired
	private CigaretteRepository cigaretteRepository;
	
	public List<Cigarette> findAll(){
		return (List<Cigarette>) cigaretteRepository.findAll();
	}

	@Override
	public Cigarette findOne(Long id) {
		return cigaretteRepository.findOne(id);
	}
}
