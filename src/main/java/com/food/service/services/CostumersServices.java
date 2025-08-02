package com.food.service.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.service.entities.Costumers;
import com.food.service.repo.CostumersRepo;

@Service
public class CostumersServices {
	
	@Autowired
	private CostumersRepo cosRepo;
	
	
	public List<Costumers> getAll(){
		return cosRepo.findAll();
	}
	public Costumers save(Costumers cos) {
		return cosRepo.save(cos);
	}
	public Costumers getById(UUID id){
		return cosRepo.findByUserId(id);
	}
}
