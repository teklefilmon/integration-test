package com.nice.integration;

import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserControllerIntegrationTest extends IntegrationTest{
	@LocalServerPort
	private int port;
	
	@Test
	public void getUser_whenUserIdNotExist_shouldReturnNotFound() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = this.port;
		RestAssured.given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
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
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = this.port;
		RestAssured.given()
		.log().ifValidationFails()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
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
