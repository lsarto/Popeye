package com.cigarettestore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cigarettestore.domain.Cigarette;
import com.cigarettestore.repository.CigaretteRepository;
import com.cigarettestore.service.CigaretteService;

@Service
public class CigaretteServiceImpl implements CigaretteService {
	@Autowired
	private CigaretteRepository cigaretteRepository;

	public List<Cigarette> findAll() {
		List<Cigarette> cigaretteList = (List<Cigarette>) cigaretteRepository.findAll();
		List<Cigarette> activeCigaretteList = new ArrayList<>();

		for (Cigarette cigarette : cigaretteList) {
			if (cigarette.isActive()) {
				activeCigaretteList.add(cigarette);
			}
		}

		return activeCigaretteList;
	}

	public Cigarette findOne(Long id) {
		return cigaretteRepository.findOne(id);
	}

	public List<Cigarette> findByCategory(String category) {
		List<Cigarette> cigaretteList = cigaretteRepository.findByCategory(category);

		List<Cigarette> activeCigaretteList = new ArrayList<>();

		for (Cigarette cigarette : cigaretteList) {
			if (cigarette.isActive()) {
				activeCigaretteList.add(cigarette);
			}
		}

		return activeCigaretteList;
	}

	public List<Cigarette> blurrySearch(String name) {
		List<Cigarette> cigaretteList = cigaretteRepository.findByNameContaining(name);
		List<Cigarette> activeCigaretteList = new ArrayList<>();

		for (Cigarette cigarette : cigaretteList) {
			if (cigarette.isActive()) {
				activeCigaretteList.add(cigarette);
			}
		}

		return activeCigaretteList;
	}
}
