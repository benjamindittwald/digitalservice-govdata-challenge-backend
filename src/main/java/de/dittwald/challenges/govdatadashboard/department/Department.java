package de.dittwald.challenges.govdatadashboard.department;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representation of a department.
 */
@Getter
@Setter
@Builder
public class Department implements Comparable<Department> {

	private String title;
	private int datasetCount;
	private List<Department> subordinates;

	@Override
	public int compareTo(Department o) {

		if (this.datasetCount > o.getDatasetCount()) {
			return 1;
		} else if (this.datasetCount == o.getDatasetCount()) {
			return 0;
		} else {
			return -1;
		}

	}

}
