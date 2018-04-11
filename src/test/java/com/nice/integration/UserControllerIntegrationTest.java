package com.nice.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.nice.domain.User;


public class UserControllerIntegrationTest extends IntegrationTest{
	@LocalServerPort
	private int port;
	
	@Test
	public void getUser_whenUserIdNotExist_shouldReturnNotFound() {
		given()
			.spec(spec)
			.port(port)
			.pathParam("idOrUsername", 100)
		.when()
			.get("/users/{idOrUsername}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body("title", equalTo("User Not Found"))
			.body("detail", equalTo("User with id " + 100 +" not found"));
	}
	
	@Test
	public void getUser_whenUserIdExist_shouldReturnOK() {
		User user = given()
			.spec(spec)
			.port(port)
			.pathParam("idOrUsername", 1)
		.when()
			.get("/users/{idOrUsername}")
		.then()
			.statusCode(HttpStatus.OK.value())
				.and()
			.extract().as(User.class);
		
		assertThat(user.getFirstName(), equalTo("James"));
		assertThat(user.getLastName(), equalTo("Alexander"));
		assertThat(user.getEmail(), equalTo("james.alexander@nice.com"));
		assertThat(user.getUserName(), equalTo("james.alexander"));
	}
	
	@Test
	public void getUser_whenUserNameNotExist_shouldReturnNotFound() {
		given()
			.spec(spec)
			.port(port)
			.pathParam("idOrUsername", "david.john")
		.when()
			.get("/users/{idOrUsername}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void getUser_whenUserNameExist_shouldReturnOK() {
		User user = given()
			.spec(spec)
			.port(port)
			.pathParam("idOrUsername", "james.alexander")
		.when()
			.get("/users/{idOrUsername}")
		.then()
			.statusCode(HttpStatus.OK.value())
				.and()
			.extract().as(User.class);
		
		assertThat(user.getFirstName(), equalTo("James"));
		assertThat(user.getLastName(), equalTo("Alexander"));
		assertThat(user.getEmail(), equalTo("james.alexander@nice.com"));
		assertThat(user.getUserName(), equalTo("james.alexander"));
	}
	
	@Test
	public void getAllUsers_shouldReturnOK() {
		given()
			.spec(spec)
			.port(port)
		.when()
			.get("/users")
		.then()
			.statusCode(HttpStatus.OK.value()).and()
			.body("size()", is(3));
	}
}
