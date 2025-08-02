package com.food.service.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.service.entities.Costumers;
import com.food.service.entities.Users;
@Repository
public interface CostumersRepo extends JpaRepository<Costumers, UUID>{
@Override
List<Costumers> findAll();


Costumers findByUserId(UUID id);
}
