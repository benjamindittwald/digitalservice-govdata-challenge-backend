package de.dittwald.challenges.govdata_dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello DigitalService!
 *
 */
@SpringBootApplication
@RestController
public class Application {

	@GetMapping("/")
	public String helloWorld() {
		return "Hello DigitalService!";
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("bvl")
	public String countBvlDatasets() {

		WebClient client = WebClient.create("https://www.govdata.de/ckan/api/3/action/");

		JsonNode rootNode;
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			rootNode = objectMapper.readTree(
					client.get().uri("package_search?fq=organization:bvl").retrieve().bodyToMono(String.class).block());

			return rootNode.get("result").get("count").asText();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return client.get().uri("package_search?fq=organization:bvl").retrieve().bodyToMono(String.class).block();
	}

}
