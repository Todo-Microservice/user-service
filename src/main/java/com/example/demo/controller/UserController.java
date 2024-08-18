package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.User;
import com.example.demo.respository.UserRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/u/")
@Tag(name = "User API", description = "API for CRUD operations on User")
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	// private String todoServiceUrl = "http://localhost:8081/todo";
	
	// @Autowired
	// private LoadBalancerClient loadBalancerClient;
	
	// private DiscoveryClient discoveryClient;
	
	@GetMapping("/{id}")
	@Operation(
			summary = "Get User details", 
			description = "Get user deatils by passing user id"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "User found"),
		@ApiResponse(responseCode = "400", description = "User not found")
	})
	public ResponseEntity<?> getUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isPresent()) {
			return ResponseEntity.ok(user.get());
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping	
	@Operation(
			summary = "Create a new user", 
			description = "Creates a new user by passing a JSON object"
	)
	public ResponseEntity<?> createUser(@RequestBody User user) {
		user = userRepository.save(user);
		
		if(user.getId() != null) {
			return ResponseEntity.ok(user);
		}
		return ResponseEntity.badRequest().build();
	}
	
	@DeleteMapping("/{id}")
	@Operation(
			summary = "Delete User", 
			description = "Get user deatils by passing user id and delete the user"
	)
	public ResponseEntity<?> deleteUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isPresent()) {
			userRepository.delete(user.get());
			return ResponseEntity.ok("success");
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/{username}/todo")
	@Operation(
			summary = "Get todos of User", 
			description = "Get todos user deatils by passing username"
	)
	@CircuitBreaker(name = "userServiceCB", fallbackMethod = "defaultTodo")
	public ResponseEntity<?> getTodos(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		
		if(user == null) {
			return ResponseEntity.badRequest().build();
		}
		
		// List<ServiceInstance> instances = discoveryClient.getInstances("TODOSERVICE");
		// ServiceInstance serviceInstance = instances.get(0);
		
		// ServiceInstance serviceInstance = loadBalancerClient.choose("TODOSERVICE");
		
		// System.out.println("Using: " + serviceInstance.getUri());
		logger.debug("Fetching todos from TODO-SERVICE");
		
		ResponseEntity<List> res = restTemplate.getForEntity(
				"http://TODOSERVICE" + "/todo/u/" + user.getId(), 
				List.class);
		
		List todoList = res.getBody();
		
		logger.debug("Todos found from TODO-SERVICE");
		
		return ResponseEntity.ok(todoList);
	}
}
