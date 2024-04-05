package de.dittwald.challenges.govdata_dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello DigitalService!
 *
 */

@SpringBootApplication
@Controller
public class Application {

	Logger logger = LoggerFactory.getLogger(Application.class);

	@Value("classpath:departments.json")
	private Resource departments;

	@Autowired
	private Properties properties;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/")
	public String index(Model model) throws IOException {

		Set<Department> departments = ckanOrganizationsDatasetCountList();
		List<Department> federalMinistries = new ArrayList<Department>();

		departments.forEach(department -> {
			logger.info(department.getTitle() + ": " + department.getDatasetCount());
			Department fm = new Department(department.getTitle(), department.getDatasetCount());

			if (!department.getSubordinates().isEmpty()) {
				department.getSubordinates().forEach(subordinate -> {
					fm.setDatasetCount(fm.getDatasetCount() + subordinate.getDatasetCount());
					logger.info("  \\_" + subordinate.getTitle() + ": " + subordinate.getDatasetCount());
				});
			}
			federalMinistries.add(fm);
		});

		Collections.sort(federalMinistries);
		Collections.reverse(federalMinistries);

		model.addAttribute("departments", departments);
		model.addAttribute("federalMinistries", federalMinistries);

		return "index";
	}

	/**
	 * Creates and returns a list that matches the departments of the
	 * departments.json file with the corresponding organizations and their number
	 * of published datasets of GovDatas CKAN.
	 * 
	 * @return A list of departments incl. their number of published datasets on
	 *         GovData
	 * @throws IOException
	 */
	private Set<Department> ckanOrganizationsDatasetCountList() throws IOException {
		JsonNode departmentsJson;
		JsonNode organizationsJson;
		ObjectMapper departmentObjectMapper = new ObjectMapper();
		ObjectMapper organizationObjectMapper = new ObjectMapper();
		InputStream in = this.departments.getInputStream();
		WebClient client = WebClient.create(properties.getGovdataApiBaseUrl());

		Set<Department> matchingDepartments = new HashSet<Department>();

		// Retrieve all CKAN organizations including all fields and its dataset count
		organizationsJson = organizationObjectMapper.readTree(client.get()
				.uri(properties.getGovdataApiOrganizationsList()).retrieve().bodyToMono(String.class).block());

		departmentsJson = departmentObjectMapper.readValue(in, JsonNode.class);

		/**
		 * Iterate over all departments and its subordinates of the departments.json and
		 * find matching titles in the organizations list of GovData.
		 * 
		 * If a match is found, the dataset count will be determined for this department
		 * / organization.
		 * 
		 * Todo: Some departments of departments.json are not listed as organizations in
		 * GovData. Those are not considered in the current implementation.
		 */
		departmentsJson.get("departments").forEach(departmentJson -> {

			Department department = new Department();
			department.setTitle(departmentJson.get("name").asText());

			organizationsJson.get("result").forEach(organizationJson -> {
				if (StringUtils.equals(organizationJson.get("title").asText(), department.getTitle())) {
					department.setDatasetCount(organizationJson.get("package_count").asInt());
				}
			});

			if (departmentJson.has("subordinates")) {
				departmentJson.get("subordinates").forEach(subordinateJson -> {

					Department subordinate = new Department();
					subordinate.setTitle(subordinateJson.get("name").asText());

					organizationsJson.get("result").forEach(organizationJson -> {
						if (StringUtils.equals(organizationJson.get("title").asText(), subordinate.getTitle())) {
							subordinate.setDatasetCount(organizationJson.get("package_count").asInt());
							department.getSubordinates().add(subordinate);
						}
					});

				});
			}
			matchingDepartments.add(department);
		});

		return matchingDepartments;
	}

}
