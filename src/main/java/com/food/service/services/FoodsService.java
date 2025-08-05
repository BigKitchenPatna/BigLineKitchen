package com.food.service.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.service.entities.Foods;
import com.food.service.repo.FoodsRepo;

@Service
public class FoodsService {
	
@Autowired	
private FoodsRepo fdr;

public Foods saveFood(Foods food) {
	return fdr.save(food);
}
public Optional<Foods> getById(UUID id){
	return fdr.findById(id);
}
public List<Foods> getByAll(){
	return fdr.findAll();
}
public List<Foods> findByName(String name){
	return fdr.findByName(name);
}
}
