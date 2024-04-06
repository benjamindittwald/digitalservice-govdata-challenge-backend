package de.dittwald.challenges.govdatadashboard.department;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

/**
 * Filters CKANSs organizations with departments.json
 */
@Controller
@Slf4j
public class OrganizationsFilter {

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
	public Set<Department> filterOrganizationsByDepartments(JsonNode departments, JsonNode organizations)
			throws IOException {

		Set<Department> matchingDepartments = new HashSet<Department>();
		JsonNode organizationsNode = organizations;

		departments.get("departments").forEach(departmentJson -> {

			Department department = new Department();
			department.setTitle(departmentJson.get("name").asText());

			organizationsNode.get("result").forEach(organizationJson -> {
				if (StringUtils.equals(organizationJson.get("title").asText(), department.getTitle())) {
					department.setDatasetCount(organizationJson.get("package_count").asInt());
				}
			});

			if (departmentJson.has("subordinates")) {
				departmentJson.get("subordinates").forEach(subordinateJson -> {

					Department subordinate = new Department();
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
			matchingDepartments.add(department);
			log.debug("Added department \"{}\" including its subordinates with count of {} direct published datasets",
					department.getTitle(), department.getDatasetCount());
		});

		return matchingDepartments;
	}

}
