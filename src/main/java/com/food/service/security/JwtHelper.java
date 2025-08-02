package com.food.service.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.food.service.entities.AssignedFood;
import com.food.service.entities.Costumers;
import com.food.service.entities.FoodPartner;
import com.food.service.entities.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtHelper {
	 private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

	    //retrieve username from jwt token
	    public String getUsernameFromToken(String token) {
	        return getClaimFromToken(token, Claims::getSubject);
	    }
	    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = getAllClaimsFromToken(token);
	        return claimsResolver.apply(claims);
	    }
	    private Claims getAllClaimsFromToken(String token) {
	        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	    }
	    public String generateToken(UserDetails userDetails) {
	        Map<String, Object> claims = new HashMap<>();
	        return doGenerateToken(claims, userDetails.getUsername());
	    }
	    public String generateToken(Users user) {
	    	Map<String, Object> claims = new HashMap<>();
	        claims.put("id", user.getId());
	        claims.put("name", user.getEmail());
	        claims.put("email", user.getEmail());
	        claims.put("phone", user.getPhone());
	        claims.put("role", user.getRole());
	    	return doGenerateToken(claims,user.getEmail());
	    }
	    public String generateToken(Costumers cos, Users user ){
	    	Map<String, Object> claims = new HashMap<>();
	    	claims.put("id", user.getId());
	        claims.put("name", user.getEmail());
	        claims.put("email", user.getEmail());
	        claims.put("phone", user.getPhone());
	        claims.put("state", cos.getState());
	        claims.put("city", cos.getCity());
	        claims.put("address", cos.getAddress());
	        claims.put("role", "COSTUMER");
	    	return doGenerateToken(claims,user.getEmail());
	    }
	    
	    public String generateToken(FoodPartner cos, Users user ){
	    	Map<String, Object> claims = new HashMap<>();
	    	claims.put("id", user.getId());
	        claims.put("name", user.getEmail());
	        claims.put("email", user.getEmail());
	        claims.put("phone", user.getPhone());
	        claims.put("state", cos.getState());
	        claims.put("city", cos.getCity());
	        claims.put("address", cos.getAddress());
	        claims.put("licenseNumber", cos.getLicenseNumber());
	        claims.put("certifications", cos.getCertifications());
	        claims.put("role", "FOOD-PARTNER");
	    	return doGenerateToken(claims,user.getEmail());
	    }
	    public String generateToken(List<Costumers> costumers, Users user) {
	    	Map<String, Object> claims = new HashMap<>();
	    	claims.put("costumers", costumers);
	    	return doGenerateToken(claims, user.getEmail());
	    	
	    }
	    
	    public String generateTokenAssignedFood(List<AssignedFood> costumers, Users user) {
	    	Map<String, Object> claims = new HashMap<>();
	    	claims.put("Assigned_food_details", costumers);
	    	return doGenerateToken(claims, user.getEmail());
	    	
	    }
	    
	    public String generateTokenFoodPartners(List<FoodPartner> costumers, Users user) {
	    	Map<String, Object> claims = new HashMap<>();
	    	claims.put("costumers", costumers);
	    	return doGenerateToken(claims, user.getEmail());
	    	
	    }
	    private String doGenerateToken(Map<String, Object> claims, String subject) {
	        return Jwts.builder()
	                .setClaims(claims)
	                .setSubject(subject)
	                .setIssuedAt(new Date(System.currentTimeMillis()))  // Issued time
	                .signWith(SignatureAlgorithm.HS512, secret)  // Sign the token
	                .compact();  // Do not set expiration
	    }
	    public Boolean validateToken(String token, Users userDetails) {
	        final String username = getUsernameFromToken(token);
	        return (username.equals(userDetails.getEmail()));  // No expiration check
	   
	}
}
