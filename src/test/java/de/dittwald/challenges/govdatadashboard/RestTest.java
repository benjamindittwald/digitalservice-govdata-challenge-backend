package de.dittwald.challenges.govdatadashboard;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.dittwald.challenges.govdatadashboard.Application;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestTest {

	@Autowired
	private Application app;

	private static final String HELLO_DIGI = "Hello DigitalService!";

	static final String API_ROOT = "http://localhost:8080";

	@Test
	public void contextLoads() {
		assertThat(app).isNotNull();
	}

//	@Test
//	public void whenGetOk_thenOk() {
//		Response response = RestAssured.get(API_ROOT);
//		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
//	}

//	@Test
//	public void whenResponseHDS_thenOk() {
//		Response response = RestAssured.get(API_ROOT);
//		assertEquals(HELLO_DIGI, response.getBody().asString());
//	}

}
