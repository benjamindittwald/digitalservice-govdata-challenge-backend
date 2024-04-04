package de.dittwald.challenges.govdata_dashboard;

public class Department {

	public Department(String title, int datasetCount) {
		super();
		this.title = title;
		this.datasetCount = datasetCount;
	}

	private String title;
	private int datasetCount;

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

}
