package de.dittwald.challenges.govdatadashboard.ckan;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Builder;
import reactor.netty.http.client.HttpClient;

/**
 * A client to use GovData CKANs action API.
 */
@Builder
public class CkanClient {

	private int timeout;
	private String baseUrl;
	private String urlPath;

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

		HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.timeout)
				.responseTimeout(Duration.ofMillis(this.timeout))
				.doOnConnected(connection -> connection
						.addHandlerLast(new ReadTimeoutHandler(this.timeout, TimeUnit.MILLISECONDS))
						.addHandlerLast(new WriteTimeoutHandler(this.timeout, TimeUnit.MILLISECONDS)));

		WebClient client = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient))
				.baseUrl(this.baseUrl).build();

		return organizationsMapper.readTree(client.get().uri(this.urlPath).retrieve().bodyToMono(String.class).block());
	}
}