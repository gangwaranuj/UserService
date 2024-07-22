package com.ms.userservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.userservice.entities.User;
import com.ms.userservice.entities.User.UserBuilder;
import com.ms.userservice.services.IUserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/users")
public class UserController {

	
	@Autowired
	private IUserService userService;
	
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User createUser = userService.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
	}
	
	@GetMapping("/{id}")
	@CircuitBreaker(name="RatingHotelBreaker",fallbackMethod = "ratingHotelFallBack")
	public ResponseEntity<User> findUserById(@PathVariable String id) {
		User user = userService.getUserById(id);
		return ResponseEntity.status(HttpStatus.FOUND).body(user);
	}
	
	@GetMapping
	public ResponseEntity<List<User>> getAllUser() {
		List<User> user = userService.getAllUser();
		return ResponseEntity.ok(user);
	}
	
	//ratingHotelFallBack for circuit breaker
	public ResponseEntity<User> ratingHotelFallBack(String id, Exception e) {
		
		User user =  User.builder()
				.email("dummy@hcl.com")
				.name("ABC")
				.about("i am a circuit Breaker.")
				.userId("12345").build();
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
}
