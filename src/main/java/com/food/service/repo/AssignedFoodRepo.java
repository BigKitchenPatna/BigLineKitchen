package com.food.service.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.service.entities.AssignedFood;

public interface AssignedFoodRepo extends JpaRepository<AssignedFood, UUID>{
List<AssignedFood> findAll();
List<AssignedFood> findByFoodPartnerUserId(UUID foodPartnerId);
List<AssignedFood> findByFoodId(UUID foodId);
}
