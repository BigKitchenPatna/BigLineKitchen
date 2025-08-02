package com.food.service.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="foods")
@Setter
@Getter
public class Foods {
	@Id
	@GeneratedValue
	private UUID id;
	private String name;
	private String category;
	private double price;
	@Override
	public String toString() {
		return "Foods [id=" + id + ", name=" + name + ", catergory=" + category + ", price=" + price + "]";
	}
	
}
