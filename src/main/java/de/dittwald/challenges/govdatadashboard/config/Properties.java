package de.dittwald.challenges.govdatadashboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

/**
 * Representation of the properties file.
 */
@Configuration
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
@Getter
public class Properties {

	@Value("${server.port}")
	private int serverPort;

	@Value("${govdata.api.baseUrl}")
	private String govdataApiBaseUrl;

	@Value("${govdata.api.organizationList}")
	private String govdataApiOrganizationsList;

	@Value("${govdata.api.timeout}")
	private int govdataApiTimeout;
}
