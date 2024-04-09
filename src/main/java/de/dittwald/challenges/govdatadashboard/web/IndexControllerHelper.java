package de.dittwald.challenges.govdatadashboard.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.dittwald.challenges.govdatadashboard.department.Department;

public class IndexControllerHelper {

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
	public static List<Department> prepareDepartmentsForView(Set<Department> departments, Boolean flattened)
			throws IOException {

		List<Department> preparedDepartments = new ArrayList<Department>();

		departments.forEach(department -> {

			Department preparedDepartment = Department.builder().datasetCount(department.getDatasetCount())
					.title(department.getTitle()).subordinates(new ArrayList<Department>()).build();

			if (!department.getSubordinates().isEmpty()) {
				department.getSubordinates().forEach(subordinate -> {
					if (flattened) {
						preparedDepartments.add(subordinate);
					} else {
						preparedDepartment
								.setDatasetCount(preparedDepartment.getDatasetCount() + subordinate.getDatasetCount());
					}
				});
			}
			preparedDepartments.add(preparedDepartment);
		});

		return preparedDepartments;
	}

}
