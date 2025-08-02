package com.food.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.food.service.security.JwtEntryPoint;
import com.food.service.security.JwtFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtEntryPoint entryPoint;
	
	@Autowired
	private JwtFilter filter;
	
	   @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		   http.csrf(csrf->csrf.disable())
	        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	                .authorizeHttpRequests(auth -> auth
		            		 .requestMatchers("/auth/register-costumer").permitAll()
		            		 .requestMatchers("/auth/register-food-partner").permitAll()
		            		 .requestMatchers("/auth/register-admin").permitAll()
		            		 .requestMatchers("/auth/login").permitAll()
		            		 .requestMatchers("/costumer/**").hasRole("COSTUMER")
		            		 .requestMatchers("/food-partner/**").hasRole("FOOD-PARTNER")
		            		 .requestMatchers("/admin/**").hasRole("ADMIN")
		            		 .anyRequest().authenticated()  // All other routes require authentication
	        	            ).exceptionHandling(ex->ex.authenticationEntryPoint(entryPoint))
                    .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class);
    return http.build();
	   }
	   
	    @Bean
	    public CorsFilter corsFilter() {
	        return new CorsFilter(corsConfigurationSource());
	    }

	    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration config = new CorsConfiguration();
	        config.setAllowCredentials(true);
	        config.addAllowedOriginPattern("*");  // Allows all origins
	        config.addAllowedHeader("*");  // Allows all headers
	        config.addAllowedMethod("*");  // Allows all HTTP methods

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);  // Applies this CORS configuration to all endpoints
	        return source;
	    }
}
