package com.nice.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nice.domain.User;
import com.nice.service.UserService;

@RestController
public class UserController {

	private UserService userService;

	public UserController(UserService service) {
		Objects.requireNonNull(service, "UserService is required for the class to function");
		this.userService = service;
	}

	@GetMapping(value = "/users/{idOrUsername}")
	ResponseEntity<User> getUser(@PathVariable String idOrUsername) {
		User user = null;
		try {
			Long id = Long.parseLong(idOrUsername);
			user = userService.findOne(id);
		} catch (NumberFormatException exception) {
			user = userService.findOne(idOrUsername);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@GetMapping(value = "/users")
	ResponseEntity<List<User>> getAllUsers() {
		return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
	}

	@PostMapping(value="/users")
	ResponseEntity<Void> createUser(@Valid @RequestBody User user){
		User newUser = userService.create(user);
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserUri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        responseHeaders.setLocation(newUserUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}
	
	@PutMapping(value="/users/{id}")
	ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody @Valid User user) {
		userService.update(id, user);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/users/{id}")
	ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.delete(id);
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
}
