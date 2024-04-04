package de.dittwald.challenges.govdata_dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello DigitalService!
 *
 */
@SpringBootApplication
@RestController
public class Application {

	@Value("classpath:departments.json")
	private Resource departments;

	private WebClient client = WebClient.create("https://www.govdata.de/ckan/api/3/action/");

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("organizations")
	public ResponseEntity<List<Department>> ckanOrganizationsList() throws IOException {
		return ResponseEntity.ok(ckanOrganizationsDatasetCountList());
	}

	private List<Department> ckanOrganizationsDatasetCountList() throws IOException {
		JsonNode departmentsJson;
		JsonNode organizationsJson;
		ObjectMapper departmentObjectMapper = new ObjectMapper();
		ObjectMapper organizationObjectMapper = new ObjectMapper();
		InputStream in = this.departments.getInputStream();

		List<Department> newDepartments = new ArrayList<Department>();

		// Retreive all CKAN organizations including all fields and its dataset count
		organizationsJson = organizationObjectMapper
				.readTree(this.client.get().uri("organization_list?all_fields=true&include_dataset_count=true")
						.retrieve().bodyToMono(String.class).block());

		// Map department.json to JsonNode
		departmentsJson = departmentObjectMapper.readValue(in, JsonNode.class);

		/**
		 * Iterate over all departments and its subordinates of the departments.json and
		 * find matching titles in the organizations list of CKAN.
		 * 
		 * If a match is found, the dataset count will be determined for this department
		 * / organization.
		 * 
		 * Todo: Some departments of departments.json are not listed as organizations in
		 * CKAN. Those are not considered in the current implementation.
		 */
		departmentsJson.get("departments").forEach(departmentJson -> {

			String department = departmentJson.get("name").asText();

			organizationsJson.get("result").forEach(organizationJson -> {
				if (organizationJson.get("title").asText().equals(department)) {
					newDepartments.add(new Department(department, organizationJson.get("package_count").asInt()));
				}
			});

			if (departmentJson.has("subordinates")) {
				departmentJson.get("subordinates").forEach(subordinateJson -> {

					String subordinate = subordinateJson.get("name").asText();
					organizationsJson.get("result").forEach(organizationJson -> {
						if (organizationJson.get("title").asText().equals(subordinate)) {
							newDepartments
									.add(new Department(subordinate, organizationJson.get("package_count").asInt()));
						}
					});

				});
			}
		});

		return newDepartments;
	}

}
