package com.food.service.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.food.service.entities.Users;
import java.util.List;



@Repository
public interface UsersRepo extends JpaRepository<Users, UUID>{
	
Users findByEmail(String email);
Optional<Users> findById(UUID id);
}
