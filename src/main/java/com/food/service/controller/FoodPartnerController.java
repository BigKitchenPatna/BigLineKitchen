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

import com.food.service.entities.AssignedFood;
import com.food.service.entities.FoodPartner;
import com.food.service.entities.Users;
import com.food.service.security.JwtHelper;
import com.food.service.services.AssignedFoodService;
import com.food.service.services.FoodPartnerServices;
import com.food.service.services.UsersServices;

@RestController
@RequestMapping("/food-partner")
public class FoodPartnerController {
	
	@Autowired
	private JwtHelper tokenHelper;

	@Autowired
	private UsersServices usrS;
	@Autowired
	private FoodPartnerServices fds;
	
	@Autowired
	private AssignedFoodService afs;
	
	@GetMapping("/get-assigned-food")
	public ResponseEntity<Object> getAssignedFood(@RequestParam UUID id){
		try {
			List<AssignedFood> asf = afs.getByFoodPartnerId(id);
			if(asf==null) {
				throw new NullPointerException();
			}
			Users user = usrS.getById(id).get();
			String token = tokenHelper.generateTokenAssignedFood(asf, user);
			return new ResponseEntity<>(token,HttpStatus.OK);
		}catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>("Not any food assigned",HttpStatus.OK);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_GATEWAY);
			// TODO: handle exception
		}
	}
	
	
	@GetMapping("/profile")
	public ResponseEntity<Object> getProfile(@RequestParam UUID id){
		try {
			Optional<Users> user = usrS.getById(id);
			FoodPartner fd = fds.getById(id);
			String token = tokenHelper.generateToken(fd, user.get());
			return new ResponseEntity<Object>(token,HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_GATEWAY);
		}
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<Object> getAllCostumer(@RequestParam("id") UUID id){
		try {
			List<FoodPartner> foodPartners = fds.getAll();
			Optional<Users> user = usrS.getById(id);
			
			String token = tokenHelper.generateTokenFoodPartners(foodPartners,user.get());
			
			return new ResponseEntity<>(token,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong",HttpStatus.OK);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<Object> update(@RequestParam UUID id, @RequestBody Map<String,Object> updates){
		try {
			FoodPartner cos=fds.getById(id);
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
				if(key.equalsIgnoreCase("certifications")) {
					cos.setCertifications(entry.getValue().toString());
				}
				if(key.equalsIgnoreCase("licenseNumber")) {
					cos.setLicenseNumber(entry.getValue().toString());
				}
			}
			fds.save(cos);
			return new ResponseEntity<>("Updated",HttpStatus.OK);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>("Some went wrong",HttpStatus.BAD_GATEWAY);
		}
	}
}
