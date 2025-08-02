package com.food.service.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="assigned_food")
@Setter
@Getter
public class AssignedFood {
@Override
	public String toString() {
		return "AssignedFood [id=" + id + ", foodPartner=" + foodPartner + ", food=" + food + "]";
	}
@Id
@GeneratedValue
private UUID id;
@ManyToOne
@JoinColumn(name = "food_id", nullable = false)
private Foods food;

@ManyToOne
@JoinColumn(name = "food_partner_id", nullable = false)
private FoodPartner foodPartner;
}
