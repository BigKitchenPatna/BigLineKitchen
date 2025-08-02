package com.food.service.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.service.entities.FoodPartner;
import com.food.service.entities.Users;
@Repository
public interface FoodPartnerRepo extends JpaRepository<FoodPartner, UUID>{
Optional<FoodPartner> findByUserId(UUID id);
 List<FoodPartner> findAll();
}
