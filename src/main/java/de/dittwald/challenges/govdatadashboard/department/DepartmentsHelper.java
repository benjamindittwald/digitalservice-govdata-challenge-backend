package de.dittwald.challenges.govdatadashboard.department;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

/**
 * Filters CKANs organizations with departments.json
 */
@Slf4j
public class DepartmentsHelper {

	/**
	 * Iterates over all departments and its subordinates of the departments.json
	 * and find matching titles in the organizations list of GovData received from
	 * its CKAN action API.
	 * 
	 * If a match is found, the dataset count will be determined for this department
	 * / organization and the department is added to the return list.
	 * 
	 *
	 * @return A matching list of departments from GovData filtered by
	 *         departments.json
	 * @throws IOException
	 */
	public static Set<Department> filterOrganizationsByDepartments(JsonNode departments, JsonNode organizations)
			throws IOException {

		Set<Department> filteredDepartments = new HashSet<Department>();
		JsonNode organizationsNode = organizations;

		departments.get("departments").forEach(departmentJson -> {

			Department department = Department.builder().subordinates(new ArrayList<Department>()).build();
			department.setTitle(departmentJson.get("name").asText());

			organizationsNode.get("result").forEach(organizationJson -> {
				if (StringUtils.equals(organizationJson.get("title").asText(), department.getTitle())) {
					department.setDatasetCount(organizationJson.get("package_count").asInt());
				}
			});

			if (departmentJson.has("subordinates")) {
				departmentJson.get("subordinates").forEach(subordinateJson -> {

					Department subordinate = Department.builder().subordinates(new ArrayList<Department>()).build();
					subordinate.setTitle(subordinateJson.get("name").asText());

					organizationsNode.get("result").forEach(organizationJson -> {
						if (StringUtils.equals(organizationJson.get("title").asText(), subordinate.getTitle())) {
							subordinate.setDatasetCount(organizationJson.get("package_count").asInt());
							department.getSubordinates().add(subordinate);
							log.debug(
									"Added subordinate \"{}\" of root department \"{}\" with count of {} direct published datasets",
									subordinate.getTitle(), department.getTitle(), subordinate.getDatasetCount());
						}
					});

				});
			}
			filteredDepartments.add(department);
			log.debug("Added department \"{}\" including its subordinates with count of {} direct published datasets",
					department.getTitle(), department.getDatasetCount());
		});

		return filteredDepartments;
	}

}
