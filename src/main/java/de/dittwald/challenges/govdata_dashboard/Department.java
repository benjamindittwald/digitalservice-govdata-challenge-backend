package de.dittwald.challenges.govdata_dashboard;

import java.util.ArrayList;
import java.util.List;

public class Department implements Comparable<Department> {

	public Department() {
		this.subordinates = new ArrayList<Department>();
	}

	public Department(String title, int datasetCount) {
		super();
		this.title = title;
		this.datasetCount = datasetCount;
		this.subordinates = new ArrayList<Department>();
	}

	private String title;
	private int datasetCount;
	private List<Department> subordinates;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDatasetCount() {
		return datasetCount;
	}

	public void setDatasetCount(int datasetCount) {
		this.datasetCount = datasetCount;
	}

	public List<Department> getSubordinates() {
		return subordinates;
	}

	public void setSubordinates(List<Department> subordinates) {
		this.subordinates = subordinates;
	}

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
