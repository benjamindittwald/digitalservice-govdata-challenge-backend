package de.dittwald.challenges.govdatadashboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Representation of the properties file.
 */
@Configuration
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class Properties {

	@Value("${server.port}")
	private int serverPort;

	@Value("${govdata.api.baseUrl}")
	private String govdataApiBaseUrl;

	@Value("${govdata.api.organizationList}")
	private String govdataApiOrganizationsList;

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getGovdataApiBaseUrl() {
		return govdataApiBaseUrl;
	}

	public void setGovdataApiBaseUrl(String govdataApiBaseUrl) {
		this.govdataApiBaseUrl = govdataApiBaseUrl;
	}

	public String getGovdataApiOrganizationsList() {
		return govdataApiOrganizationsList;
	}

	public void setGovdataApiOrganizationsList(String govdataApiOrganizationsList) {
		this.govdataApiOrganizationsList = govdataApiOrganizationsList;
	}
}
