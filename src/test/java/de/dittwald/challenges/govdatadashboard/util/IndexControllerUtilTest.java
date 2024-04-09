package de.dittwald.challenges.govdatadashboard.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import de.dittwald.challenges.govdatadashboard.department.Department;
import de.dittwald.challenges.govdatadashboard.department.DepartmentsHelper;
import de.dittwald.challenges.govdatadashboard.web.IndexControllerHelper;

@SpringBootTest
public class IndexControllerUtilTest {

	@Value("classpath:departments.json")
	private Resource departmentsResource;

	@Value("classpath:organizations.json")
	private Resource organizationsResource;

	private Set<Department> departmentsSet;

	private List<Department> departmentsNonFlattened;
	private List<Department> departmentsFlattened;

	private final static int FEDERAL_MINISTRIES_COUNT = 13;
	private final static int ALL_DEPARTMENTS_COUNT = 16;
	private final static int FEDERAL_MINISTRY_DATASET_COUNT = 68;

	@BeforeEach
	public void setUp() throws IOException {
		departmentsSet = DepartmentsHelper.filterOrganizationsByDepartments(
				JsonResourceLoader.getAsJson(departmentsResource), JsonResourceLoader.getAsJson(organizationsResource));
		departmentsNonFlattened = IndexControllerHelper.prepareDepartmentsForView(departmentsSet, false);
		departmentsFlattened = IndexControllerHelper.prepareDepartmentsForView(departmentsSet, true);
	}

	@Test
	public void testFederalMinistriesCount() {
		assertEquals(FEDERAL_MINISTRIES_COUNT, departmentsNonFlattened.size());
	}

	@Test
	public void testAllDepartmentsCount() {
		assertEquals(ALL_DEPARTMENTS_COUNT, departmentsFlattened.size());
	}

	@Test
	public void testFedelMinistryFinancetDatasetCount() {

		boolean success = false;

		for (Department department : departmentsNonFlattened) {
			if (StringUtils.equals(department.getTitle(), "Bundesministerium der Finanzen")) {
				assertEquals(FEDERAL_MINISTRY_DATASET_COUNT, department.getDatasetCount());
				success = true;
			}
		}

		assertTrue(success, "\"Bundesministerium der Finanzen\" was not found.");
	}

}
