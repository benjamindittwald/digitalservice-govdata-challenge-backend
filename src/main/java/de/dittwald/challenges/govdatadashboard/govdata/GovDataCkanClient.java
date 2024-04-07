package de.dittwald.challenges.govdatadashboard.govdata;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.dittwald.challenges.govdatadashboard.config.Properties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

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

		HttpClient httpClient = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.properties.getGovdataApiTiemout())
				.responseTimeout(Duration.ofMillis(this.properties.getGovdataApiTiemout()))
				.doOnConnected(connection -> connection
						.addHandlerLast(
								new ReadTimeoutHandler(this.properties.getGovdataApiTiemout(), TimeUnit.MILLISECONDS))
						.addHandlerLast(new WriteTimeoutHandler(this.properties.getGovdataApiTiemout(),
								TimeUnit.MILLISECONDS)));

		WebClient client = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient))
				.baseUrl(this.properties.getGovdataApiBaseUrl()).build();

		return organizationsMapper.readTree(client.get().uri(properties.getGovdataApiOrganizationsList()).retrieve()
				.bodyToMono(String.class).block());
	}
}