package com.food.service.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.service.entities.AssignedFood;
import com.food.service.repo.AssignedFoodRepo;

@Service
public class AssignedFoodService {
	

	@Autowired
	private AssignedFoodRepo afdr;
	
	
	public AssignedFood save(AssignedFood af) {
		return afdr.save(af);
	}
	
	public List<AssignedFood> getAll(){
		return afdr.findAll();
	}
	
	public List<AssignedFood> getByFoodPartnerId(UUID id){
		return afdr.findByFoodPartnerUserId(id);
	}
	
	public List<AssignedFood> getByFoodId(UUID id){
		return afdr.findByFoodId(id);
	}
}
