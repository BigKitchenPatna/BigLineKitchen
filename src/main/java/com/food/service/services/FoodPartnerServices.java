package com.food.service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.service.entities.FoodPartner;
import com.food.service.repo.FoodPartnerRepo;

@Service

public class FoodPartnerServices {

	@Autowired
	private FoodPartnerRepo fdrepo;
	
	public FoodPartner getById(UUID id){
		Optional<FoodPartner> fd = fdrepo.findById(id);
		return fd.get();
	}
	public FoodPartner save(FoodPartner fd) {
		return fdrepo.save(fd);
	}
	public List<FoodPartner> getAll(){
		return fdrepo.findAll();
	}
}
