package com.food.service.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.service.entities.AssignedFood;
import com.food.service.entities.Costumers;
import com.food.service.entities.FoodPartner;
import com.food.service.entities.Foods;
import com.food.service.entities.Users;
import com.food.service.security.JwtHelper;
import com.food.service.services.AssignedFoodService;
import com.food.service.services.CostumersServices;
import com.food.service.services.FoodPartnerServices;
import com.food.service.services.FoodsService;
import com.food.service.services.UsersServices;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private JwtHelper tokenHelper;
	
	@Autowired
	private CostumersServices cosS;
	
	@Autowired
	private FoodPartnerServices fds;
	
	@Autowired
	private UsersServices usrS;
	
	@Autowired
	private FoodsService foods;
	
	@Autowired
	private AssignedFoodService afs;
	
	
	

//-----------Foods---------------//
@PostMapping("/upload-food")	
public ResponseEntity<Object> uploadFood(@RequestBody Foods food){
	try {
		foods.saveFood(food);
		return new ResponseEntity<>("Food saved",HttpStatus.OK);
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_GATEWAY);
	}
}
	
@PostMapping("/assign-food")
public ResponseEntity<Object> assifnedFood(@RequestParam UUID foodId, @RequestParam UUID foodPartnerId){
try {
	System.out.println("FOOD-ID = "+foodId+" Foodpartner Id = "+foodPartnerId);
	AssignedFood af = new AssignedFood();
	Foods food = foods.getById(foodId).get();
	FoodPartner foodPartner = fds.getById(foodPartnerId);
	
	if(food==null&&foodPartner==null) {
		return new ResponseEntity<>("Food foodPartner is not found", HttpStatus.BAD_REQUEST);
	}
	if(food==null) {
		return new ResponseEntity<>("Food is not found", HttpStatus.BAD_REQUEST);
	}
	if(foodPartner==null) {
		return new ResponseEntity<>("foodPartner is not found", HttpStatus.BAD_REQUEST);
	}
	af.setFood(food);
	af.setFoodPartner(foodPartner);
	afs.save(af);
	return new ResponseEntity<>("Assigned",HttpStatus.OK);
} 
catch (NoSuchElementException e) {
	// TODO: handle exception
	e.printStackTrace();

	return new ResponseEntity<>("Food is not found", HttpStatus.BAD_REQUEST);
	
}
catch (Exception e) {
	// TODO: handle exception
	e.printStackTrace();
	return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_GATEWAY);
}	
}

@GetMapping("/get-all-assigned-food-details")
public ResponseEntity<Object> getAllAssignedFood(){
	try {
		List<AssignedFood> getAll = afs.getAll();
		Users usr = null;
		if(getAll==null) {
			return new ResponseEntity<>("List is empty", HttpStatus.BAD_REQUEST);
		}
		usr = usrS.getById(getAll.get(0).getFoodPartner().getUserId()).get();
		String token = tokenHelper.generateTokenAssignedFood(getAll, usr);
		return new ResponseEntity<>(token, HttpStatus.OK);
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		return new ResponseEntity<>("something went wrong",HttpStatus.BAD_GATEWAY);
	}

}

@GetMapping("/get-all-food")
public ResponseEntity<Object> getAllFood(){
	try {
		List<Foods> allFood = foods.getByAll();
		return new ResponseEntity<>(allFood,HttpStatus.OK);
	}catch (NullPointerException e) {
		e.printStackTrace();
		return new ResponseEntity<>("List is empty",HttpStatus.BAD_REQUEST);
	}
	catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_GATEWAY);
	}
}
//-----------Costumer handeling section-----------------//
	@GetMapping("/get-all-costumers")
	public ResponseEntity<Object> getAllCostumer(@RequestParam("id") UUID id){
		try {
			List<Costumers> costumers = cosS.getAll();
			Optional<Users> user = usrS.getById(id);
			
			String token = tokenHelper.generateToken(costumers,user.get());
			return new ResponseEntity<>(token,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong",HttpStatus.OK);
		}
	}
	
	@GetMapping("/costumer-profile")
	public ResponseEntity<Object> getCostumerProfile(@RequestParam UUID id){
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
	
	@PutMapping("/update-costumer")
	public ResponseEntity<Object> updateCostumer(@RequestParam UUID id, @RequestBody Map<String,Object> updates){
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
	
//---------------food-partner-managment------------------------//
	@GetMapping("/food-partner-profile")
	public ResponseEntity<Object> getFoodPartnerProfile(@RequestParam UUID id){
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
	
	@GetMapping("/get-all-food-partner")
	public ResponseEntity<Object> getAllFoodPartner(@RequestParam("id") UUID id){
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
	
	@PutMapping("/update-food-partner")
	public ResponseEntity<Object> updateFoodPartner(@RequestParam UUID id, @RequestBody Map<String,Object> updates){
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
