package de.dittwald.challenges.govdatadashboard.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest
public class JsonResourceLoaderTest {
	@Value("classpath:departments.json")
	private Resource departmentsResource;

	private JsonNode departmentsJson;

	private final static int DEPARTMENTS_JSON_FEDERAL_MINSTRIES_COUNT = 13;

	@BeforeEach
	public void setUp() throws IOException {
		this.departmentsJson = JsonResourceLoader.getAsJson(departmentsResource);
	}

	@Test
	public void testDepartmentsCount() {
		assertEquals(DEPARTMENTS_JSON_FEDERAL_MINSTRIES_COUNT, this.departmentsJson.get("departments").size());
	}
}
