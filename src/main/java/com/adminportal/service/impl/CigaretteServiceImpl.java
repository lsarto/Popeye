package com.adminportal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.domain.Cigarette;
import com.adminportal.repository.CigaretteRepository;
import com.adminportal.service.CigaretteService;

@Service
public class CigaretteServiceImpl implements CigaretteService{
	
	@Autowired
	private CigaretteRepository cigaretteRepository;
	
	public Cigarette save(Cigarette cigarette) {
		return cigaretteRepository.save(cigarette);
	}
}
