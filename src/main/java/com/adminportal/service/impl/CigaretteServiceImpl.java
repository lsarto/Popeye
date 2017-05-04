package com.adminportal.service.impl;

import java.util.List;

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

	@Override
	public List<Cigarette> findAll() {
		return (List<Cigarette>) cigaretteRepository.findAll();
	}

	@Override
	public Cigarette findOne(Long id) {
		return cigaretteRepository.findOne(id);
	}
}
