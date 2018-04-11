package com.nice.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static io.restassured.RestAssured.*;

import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;


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
		given()
			.spec(spec)
			.port(port)
			.log().ifValidationFails()
			.pathParam("idOrUsername", 1)
		.when()
			.get("/users/{idOrUsername}")
		.then()
		.log().ifValidationFails()
			.statusCode(HttpStatus.OK.value()).and()
			.body("firstName", equalTo("James")).and()
			.body("lastName", equalTo("Alexander")).and()
			.body("email", equalTo("james.alexander@nice.com")).and()
			.body("userName", equalTo("james.alexander"));
	}
}
