package com.food.service.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="food_partner")
@Setter
@Getter
public class FoodPartner {
    @Id
    private UUID userId;
private String name;
private String state;
private String city;
private String address;
private String licenseNumber;
private String certifications;
@Override
public String toString() {
	return "FoodPartner [user=" + userId.toString() + ", name=" + name + ", state=" + state + ", city=" + city + ", address="
			+ address + ", licenseNumber=" + licenseNumber + ", certifications=" + certifications + "]";
}

}
