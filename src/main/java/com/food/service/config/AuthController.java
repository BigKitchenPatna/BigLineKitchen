package com.food.service.config;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.food.service.dao.ErrorResponse;
import com.food.service.entities.Costumers;
import com.food.service.entities.FoodPartner;
import com.food.service.entities.Users;
import com.food.service.security.JwtHelper;
import com.food.service.services.CostumersServices;
import com.food.service.services.FoodPartnerServices;
import com.food.service.services.UsersServices;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
	@Autowired
	private UsersServices usrS;	
	
	@Autowired
	private CostumersServices cosS;

	@Autowired
	private FoodPartnerServices fds;
	
	@Autowired
	private JwtHelper tokenHelper;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
	
	@GetMapping("/")
	public String hello() {
		return "hello";
	}
	
	@PostMapping("/register-costumer")
	public ResponseEntity<Object> registerUser(@RequestBody Users user){
		try {
			if(usrS.getByEmail(user.getEmail())!=null){
				throw new SQLException();
			}
			System.out.println("register-user hit");
			user.setRole("COSTUMER");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			Users savedUser = usrS.saveUser(user);
			String token = tokenHelper.generateToken(savedUser);

            if (token == null) {
                log.error("Token generation failed for user: {}", user.getEmail());
                return new ResponseEntity<>("Token not generated", HttpStatus.OK);
            }

            // Return the generated token
            return new ResponseEntity<>(token, HttpStatus.OK);
		}
		catch(SQLException e) {
			e.printStackTrace();
			return new ResponseEntity<>("User already exists",HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_GATEWAY);
			
		}
	}
	@PostMapping("/register-food-partner")
	public ResponseEntity<Object> registerFoodPartner(@RequestBody Users user){
		try {
			System.out.println("register-partner hit");
			if(usrS.getByEmail(user.getEmail())!=null){
				throw new SQLException();
			}
			user.setRole("FOOD-PARTNER");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			Users savedUser = usrS.saveUser(user);
			String token = tokenHelper.generateToken(savedUser);

            if (token == null) {
                log.error("Token generation failed for user: {}", user.getEmail());
                return new ResponseEntity<>("Token not generated", HttpStatus.OK);
            }

            // Return the generated token
            return new ResponseEntity<>(token, HttpStatus.OK);
		} catch(SQLException e) {
			e.printStackTrace();
			return new ResponseEntity<>("User already exists",HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_GATEWAY);
			
		}
	}
	@PostMapping("/register-admin")
	public ResponseEntity<Object> registerAdmin(@RequestBody Users user){
		try {
			System.out.println("register-admin hit");
			if(usrS.getByEmail(user.getEmail())!=null){
				throw new SQLException();
			}
			user.setRole("ADMIN");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			Users savedUser = usrS.saveUser(user);
			String token = tokenHelper.generateToken(savedUser);

            if (token == null) {
                log.error("Token generation failed for user: {}", user.getEmail());
                return new ResponseEntity<>("Token not generated", HttpStatus.OK);
            }

            // Return the generated token
            return new ResponseEntity<>(token, HttpStatus.OK);
		}catch(SQLException e) {
			e.printStackTrace();
			return new ResponseEntity<>("User already exists",HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_GATEWAY);
			
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestParam String email, @RequestParam String password){
		try {
			Users u = usrS.getByEmail(email);
		       
	        if (u == null) {
	        	return new ResponseEntity<Object>("User not exist",HttpStatus.BAD_REQUEST);
	        }
			this.doAuthenticate(email, password);
			
			boolean isCostumer = false;
			boolean isFoodPartner = false;
			boolean isAdmin = false;
			
			Users user = usrS.getByEmail(email);
			if(user==null) {
				System.out.println("User not exist");
			}
			else {
				System.out.println("User exist  "+user.getName());
			}
			Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
			for (GrantedAuthority authority : authorities) {
				String role = authority.getAuthority();
				// logic of checking role//
				if ("ROLE_COSTUMER".equals(role)) {
					isCostumer = true;
				} else if ("ROLE_FOOD-PARTNER".equals(role)) {
					isFoodPartner = true;
				}else if("ROLE_ADMIN".equals(role)) {
					isAdmin = true;
				}
			}
			String token ="";
			if(isCostumer) {
				Costumers cos = cosS.getById(user.getId());
				token = tokenHelper.generateToken(cos, user);
					
			}
			else if(isFoodPartner) {
				FoodPartner fd = fds.getById(user.getId());
				token = tokenHelper.generateToken(fd, user);
				
			}
			else if(isAdmin) {
				System.out.println("Admin exist");
				token = tokenHelper.generateToken(user);
				System.out.println(token);
			}
			return new ResponseEntity<>(token,HttpStatus.OK);	
			
		}
		catch (BadCredentialsException e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>("Invalid username or password", HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_GATEWAY);	
		}
	}


	private void doAuthenticate(String email, String password) {
		Users user = usrS.getByEmail(email);
       
        if (user == null) {
        	System.out.println("user not exist");
            throw new BadCredentialsException("Invalid Username or Password");
        }

        // Check if the raw password matches the encoded password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
	@ExceptionHandler(BadCredentialsException.class)
	public ErrorResponse exceptionHandler() {
		ErrorResponse er = new ErrorResponse();
		er.setSuccess(false);
		er.setMessage("Invalid Username or password");
		return er;
	}
}
