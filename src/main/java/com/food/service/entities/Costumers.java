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
@Table(name="costumers")
@Setter
@Getter
public class Costumers {
    @Id
    private UUID userId;
private String name;
private String state;
private String city;
private int pincode;
private String address;
@Override
public String toString() {
	return "Costumers [user=" + userId.toString() + ", name=" + name + ", state=" + state + ", city=" + city + ", pincode="
			+ pincode + ", address=" + address + "]";
}

}
