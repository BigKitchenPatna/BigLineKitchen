package com.food.service.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.service.entities.Costumers;
import com.food.service.entities.FoodPartner;
import com.food.service.entities.Users;
import com.food.service.security.JwtHelper;
import com.food.service.services.CostumersServices;
import com.food.service.services.UsersServices;

@RestController
@RequestMapping("/costumer")
public class CostumerController {
	@Autowired
	private JwtHelper tokenHelper;
	
	@Autowired
	private CostumersServices cosS;
	
	@Autowired
	private UsersServices usrS;
	
	@GetMapping("/")
	public String hello() {
		return "Hello Costumer";
	}

	@GetMapping("/get-all")
	public ResponseEntity<Object> getAllCostumer(@RequestParam("id") UUID id){
		try {
			System.out.println("get-all-hit-costumer");
			List<Costumers> costumers = cosS.getAll();
			Optional<Users> user = usrS.getById(id);
			System.out.println(user);
			String token = tokenHelper.generateToken(costumers,user.get());
			return new ResponseEntity<>(token,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong",HttpStatus.OK);
		}
	}
	
	@GetMapping("/profile")
	public ResponseEntity<Object> getProfile(@RequestParam UUID id){
		try {
			Optional<Users> user = usrS.getById(id);
			Costumers fd = cosS.getById(id);
			String token = tokenHelper.generateToken(fd, user.get());
			return new ResponseEntity<Object>(token,HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_GATEWAY);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<Object> update(@RequestParam UUID id, @RequestBody Map<String,Object> updates){
		try {
			Costumers cos=cosS.getById(id);
			for(Map.Entry<String, Object> entry:updates.entrySet()) {
				String key = entry.getKey();
				if(key.equalsIgnoreCase("state")) {
					cos.setState(entry.getValue().toString());
				}
				if(key.equalsIgnoreCase("city")) {
					cos.setCity(entry.getValue().toString());
				}
				if(key.equalsIgnoreCase("address")) {
					cos.setAddress(entry.getValue().toString());
				}
			}
			cosS.save(cos);
			return new ResponseEntity<>("Updated",HttpStatus.OK);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>("Some went wrong",HttpStatus.BAD_GATEWAY);
		}
	}
}
