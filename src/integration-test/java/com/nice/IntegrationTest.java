package com.nice;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.nice.Application;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Sql(value = "classpath:integration-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class IntegrationTest {
	protected static RequestSpecification spec;
	
	@BeforeClass
	public static void initSpec() {
		spec = new RequestSpecBuilder()
	            .setContentType(ContentType.JSON)
	            .setAccept(ContentType.JSON)
	            .setBaseUri("http://localhost")
	            .build();
	}
}
