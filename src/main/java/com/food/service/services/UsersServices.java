package com.food.service.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.service.entities.Costumers;
import com.food.service.entities.FoodPartner;
import com.food.service.entities.Users;
import com.food.service.repo.CostumersRepo;
import com.food.service.repo.FoodPartnerRepo;
import com.food.service.repo.UsersRepo;

@Service
public class UsersServices {


@Autowired
private UsersRepo usrpo;

@Autowired
private CostumersRepo cosrepo;

@Autowired
private FoodPartnerRepo fdrepo;

public Optional<Users> getById(UUID id){
	return usrpo.findById(id);
}

public Users saveUser(Users usr){
	Users savedUser = usrpo.save(usr);
	if(usr.getRole().equals("COSTUMER")) {
		Costumers cos = new Costumers();
		cos.setUserId(savedUser.getId());
		
		cos.setName(usr.getName());
		cosrepo.save(cos);
	}
	if(usr.getRole().equals("FOOD-PARTNER")) {
		FoodPartner fp = new  FoodPartner();
		fp.setUserId(savedUser.getId());
		
		fp.setName(usr.getName());
		fdrepo.save(fp);
	}
	return savedUser;
}

public Users getByEmail(String email){
	
	return usrpo.findByEmail(email);
}
	
}
