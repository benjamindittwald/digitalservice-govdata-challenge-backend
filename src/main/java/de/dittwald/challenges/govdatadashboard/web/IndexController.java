package de.dittwald.challenges.govdatadashboard.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.dittwald.challenges.govdatadashboard.department.Department;
import de.dittwald.challenges.govdatadashboard.department.DepartmentsJsonClient;
import de.dittwald.challenges.govdatadashboard.department.OrganizationsFilter;
import de.dittwald.challenges.govdatadashboard.govdata.GovDataCkanClient;

/**
 * Routing and messages for index.html
 */
@Controller
public class IndexController {

	@Autowired
	private OrganizationsFilter organizationsFilter;

	@Autowired
	private DepartmentsJsonClient departmentsJsonClient;

	@Autowired
	private GovDataCkanClient govDataCkanClient;

	private final static boolean FLATTENED = true;
	private final static boolean NON_FLATTENED = false;

	@GetMapping("/")
	public String index(Model model) throws IOException {

		Set<Department> departmentsSet = this.organizationsFilter.filterOrganizationsByDepartments(
				this.departmentsJsonClient.getAllDepartments(), this.govDataCkanClient.getOrganizationsList());

		List<Department> departments = prepareDepartmentsForView(departmentsSet, NON_FLATTENED);
		Collections.sort(departments);
		Collections.reverse(departments);
		model.addAttribute("departments", departments);

		List<Department> flattenedDepartments = prepareDepartmentsForView(departmentsSet, FLATTENED);
		Collections.sort(flattenedDepartments);
		Collections.reverse(flattenedDepartments);
		model.addAttribute("flattenedDepartments", flattenedDepartments);

		return "index";
	}

	/**
	 * 
	 * Either returns a list that only consists of root departments (federal
	 * ministries) or a flattened list with all Departments listed in
	 * departments.json.
	 * 
	 * In the non-flattened list the dataset count of each root department is an
	 * aggregation of all direct published datasets of the root departments and all
	 * published datasets of their subordinates.
	 * 
	 * @param departments A set of departments and its subordinates
	 * @param flattened   If true the list to return is flattened. Departments and
	 *                    subordinates are put on the same level and only direct
	 *                    publications are count.
	 * @return A list of departments
	 * @throws IOException
	 */
	private List<Department> prepareDepartmentsForView(Set<Department> departments, Boolean flattened)
			throws IOException {

		List<Department> preparedDepartments = new ArrayList<Department>();

		departments.forEach(department -> {

			Department preparedDepartment = new Department(department.getTitle(), department.getDatasetCount());

			if (!department.getSubordinates().isEmpty()) {
				department.getSubordinates().forEach(subordinate -> {
					if (flattened) {
						preparedDepartments.add(subordinate);
					} else {
						preparedDepartment.setDatasetCount(preparedDepartment.getDatasetCount() + subordinate.getDatasetCount());
					}
				});
			}
			preparedDepartments.add(preparedDepartment);
		});

		return preparedDepartments;
	}

}
