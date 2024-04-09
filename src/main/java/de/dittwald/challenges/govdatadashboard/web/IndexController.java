package de.dittwald.challenges.govdatadashboard.web;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.dittwald.challenges.govdatadashboard.ckan.CkanClient;
import de.dittwald.challenges.govdatadashboard.config.Properties;
import de.dittwald.challenges.govdatadashboard.department.Department;
import de.dittwald.challenges.govdatadashboard.department.DepartmentsHelper;
import de.dittwald.challenges.govdatadashboard.util.JsonResourceLoader;

/**
 * Routing and messages for index.html
 */
@Controller
public class IndexController {

	@Value("classpath:departments.json")
	private Resource departments;

	@Autowired
	private Properties properties;

	private final static boolean FLATTENED = true;
	private final static boolean NON_FLATTENED = false;

	@GetMapping("/")
	public String index(Model model) throws IOException {
		CkanClient ckanClient = CkanClient.builder().timeout(this.properties.getGovdataApiTimeout())
				.baseUrl(this.properties.getGovdataApiBaseUrl())
				.urlPath(this.properties.getGovdataApiOrganizationsList()).build();

		Set<Department> departmentsSet = DepartmentsHelper.filterOrganizationsByDepartments(
				JsonResourceLoader.getAsJson(departments), ckanClient.getOrganizationsList());

		List<Department> departments = IndexControllerHelper.prepareDepartmentsForView(departmentsSet, NON_FLATTENED);
		Collections.sort(departments);
		Collections.reverse(departments);
		model.addAttribute("departments", departments);

		List<Department> flattenedDepartments = IndexControllerHelper.prepareDepartmentsForView(departmentsSet,
				FLATTENED);
		Collections.sort(flattenedDepartments);
		Collections.reverse(flattenedDepartments);
		model.addAttribute("flattenedDepartments", flattenedDepartments);

		return "index";
	}

}
