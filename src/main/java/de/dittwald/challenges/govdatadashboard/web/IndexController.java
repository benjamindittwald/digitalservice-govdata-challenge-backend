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
import de.dittwald.challenges.govdatadashboard.department.Department;
import de.dittwald.challenges.govdatadashboard.department.OrganizationsFilter;
import de.dittwald.challenges.govdatadashboard.util.IndexControllerUtil;
import de.dittwald.challenges.govdatadashboard.util.JsonResourceLoader;

/**
 * Routing and messages for index.html
 */
@Controller
public class IndexController {

	@Value("classpath:departments.json")
	private Resource departments;

	@Autowired
	private OrganizationsFilter organizationsFilter;

	@Autowired
	private CkanClient govDataCkanClient;

	private final static boolean FLATTENED = true;
	private final static boolean NON_FLATTENED = false;

	@GetMapping("/")
	public String index(Model model) throws IOException {

		Set<Department> departmentsSet = this.organizationsFilter.filterOrganizationsByDepartments(
				JsonResourceLoader.getAsJson(departments), this.govDataCkanClient.getOrganizationsList());

		List<Department> departments = IndexControllerUtil.prepareDepartmentsForView(departmentsSet, NON_FLATTENED);
		Collections.sort(departments);
		Collections.reverse(departments);
		model.addAttribute("departments", departments);

		List<Department> flattenedDepartments = IndexControllerUtil.prepareDepartmentsForView(departmentsSet,
				FLATTENED);
		Collections.sort(flattenedDepartments);
		Collections.reverse(flattenedDepartments);
		model.addAttribute("flattenedDepartments", flattenedDepartments);

		return "index";
	}

}
