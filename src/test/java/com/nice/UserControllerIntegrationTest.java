package com.nice;

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
	
	@Test
	public void createUser_shouldReturnCreated() {
		User user = new User("Ryan", "Owen", "ryan.owen@nice.com", "ryan.owen");
		String location = given()
			.spec(spec)
			.port(port)
			.request().body(user)
		.when()
			.post("/users")
		.then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().header("location");
		
		validateUserDetails(user, location);
	}
	
	@Test
	public void updateUser_whenUserIdNotExist_shouldReturnNotFound() {
		User user = new User("Ryan", "Owen", "ryan.owen@nice.com", "ryan.owen");
		given()
			.spec(spec)
			.port(port)
			.pathParam("id", 100)
			.body(user)
		.when()
			.put("/users/{id}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void updateUser_whenUserIdExist_shouldReturnOK() {
		User user = new User("Ryan", "Owen", "ryan.owen@nice.com", "ryan.owen");
		given()
			.spec(spec)
			.port(port)
			.pathParam("id", 1)
			.body(user)
		.when()
			.put("/users/{id}")
		.then()
			.statusCode(HttpStatus.OK.value());
		
		validateUserDetails(user, "/users/1");
	}
	
	@Test
	public void deleteUser_whenUserIdNotExist_shouldReturnNotFound() {
		given()
			.spec(spec)
			.port(port)
			.pathParam("id", 100)
		.when()
			.delete("/users/{id}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deleteUser_whenUserIdExist_shouldReturnNoContent() {
		given()
			.spec(spec)
			.port(port)
			.pathParam("id", 1)
		.when()
			.delete("/users/{id}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	private void validateUserDetails(User user, String url) {
		User updatedUser = given()
				.spec(spec)
				.port(port)
			.when()
				.get(url)
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().as(User.class);
			
			assertThat(updatedUser.getFirstName(), equalTo(user.getFirstName()));
			assertThat(updatedUser.getLastName(), equalTo(user.getLastName()));
			assertThat(updatedUser.getEmail(), equalTo(user.getEmail()));
			assertThat(updatedUser.getUserName(), equalTo(user.getUserName()));
	}
}
