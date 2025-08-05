package com.food.service.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.food.service.entities.Foods;
@Repository
public interface FoodsRepo extends JpaRepository<Foods, UUID>{
List<Foods> findAll();
Optional<Foods> findById(UUID id);
List<Foods> findByName(String name);
}
