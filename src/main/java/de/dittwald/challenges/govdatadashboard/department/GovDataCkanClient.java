package de.dittwald.challenges.govdatadashboard.department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.dittwald.challenges.govdatadashboard.config.Properties;

/**
 * A client to use GovData CKANs action API.
 */
@Controller
public class GovDataCkanClient {

	@Autowired
	private Properties properties;

	/**
	 * Requests all organizations from GovData CKANs action API. Includes all fields
	 * and the corresponding number of published datasets.
	 * 
	 * @return The organizations as JsonNode
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public JsonNode getOrganizationsList() throws JsonMappingException, JsonProcessingException {
		ObjectMapper organizationsMapper = new ObjectMapper();
		WebClient client = WebClient.create(properties.getGovdataApiBaseUrl());

		return organizationsMapper.readTree(client.get().uri(properties.getGovdataApiOrganizationsList()).retrieve()
				.bodyToMono(String.class).block());
	}
}