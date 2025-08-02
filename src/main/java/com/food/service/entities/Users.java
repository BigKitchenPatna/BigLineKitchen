package com.food.service.entities;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Setter
@Getter
public class Users implements UserDetails{
@Id
private UUID id;
private String phone;
private String name;
@Column(name="email")
private String email;
private String password;
private String role;
private String created_at;


@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
	// TODO Auto-generated method stub
	return List.of(new SimpleGrantedAuthority("ROLE_" + getRole()));
}
@Override
public String getPassword() {
    return password;
}

@Override
public String getUsername() {
    return email;
}

@Override
public boolean isAccountNonExpired() {
    return true;
}

@Override
public boolean isAccountNonLocked() {
    return true;
}

@Override
public boolean isCredentialsNonExpired() {
    return true;
}

@Override
public boolean isEnabled() {
    return true;
}
@Override
public String toString() {
	return "Users [id=" + id + ", phone=" + phone + ", name=" + name + ", email=" + email + ", password=" + password
			+ ", role=" + role + ", created_at=" + created_at + "]";
}

}
