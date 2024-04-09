package de.dittwald.challenges.govdatadashboard.department;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;

import de.dittwald.challenges.govdatadashboard.util.JsonResourceLoader;

@SpringBootTest
public class DepartmentsHelperTest {

	@Value("classpath:departments.json")
	private Resource departmentsResource;

	@Value("classpath:organizations.json")
	private Resource organizationsResource;

	private JsonNode departmentsJson;
	private JsonNode organizationsJson;

	private final static String CKAN_ORGANIZATION_BERLIN_ONLINE = "Berlin Open Data";
	private final static int DEPARTMEMTS_COUNT = 13;
	private final static int STORED_ORGANIZATIONS_COUNT = 5;

	@BeforeEach
	public void setUp() throws IOException {
		this.departmentsJson = JsonResourceLoader.getAsJson(departmentsResource);
		this.organizationsJson = JsonResourceLoader.getAsJson(organizationsResource);
	}

	@Test
	public void testFilterResultCount() throws IOException {
		assertEquals(DEPARTMEMTS_COUNT, DepartmentsHelper
				.filterOrganizationsByDepartments(this.departmentsJson, this.organizationsJson).size());
	}

	@Test
	public void testIfBerlinOnlineIsNotIncludedThenOk() throws IOException {

		DepartmentsHelper.filterOrganizationsByDepartments(this.departmentsJson, this.organizationsJson)
				.forEach(department -> {
					if (StringUtils.equals(department.getTitle(), CKAN_ORGANIZATION_BERLIN_ONLINE)) {
						fail("Berlin Online is not part of departments.json");
					}
				});
	}

	@Test
	public void testStoredOrganizationsCount() throws IOException {

		int storedOrganizationsCount = 0;

		List<String> organizationTitles = new ArrayList<String>(Arrays.asList("Auswärtiges Amt",
				"Bundesministerium der Finanzen", "Bundeszentralamt für Steuern", "Generalzolldirektion", "ITZ-Bund"));

		for (Department department : DepartmentsHelper.filterOrganizationsByDepartments(this.departmentsJson,
				this.organizationsJson)) {
			if (organizationTitles.contains(department.getTitle())) {
				storedOrganizationsCount++;
				if (department.getSubordinates().size() > 0) {
					for (Department subordinate : department.getSubordinates()) {
						if (organizationTitles.contains(subordinate.getTitle())) {
							storedOrganizationsCount++;
						}
					}
				}
			}
		}

		assertEquals(STORED_ORGANIZATIONS_COUNT, storedOrganizationsCount);
	}

}
