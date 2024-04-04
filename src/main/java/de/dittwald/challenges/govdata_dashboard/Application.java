package de.dittwald.challenges.govdata_dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonParser;
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

	@Value("classpath:departments.json")
	private Resource departments;

	private WebClient client = WebClient.create("https://www.govdata.de/ckan/api/3/action/");

	@GetMapping("/")
	public String helloWorld() {
		return "Hello DigitalService!";
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("count")
	public String countDatasetsPerDepartment(@RequestParam(name = "name") String department) {

		JsonNode rootNode;
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			rootNode = objectMapper.readTree(this.client.get().uri("package_search?fq=organization:" + department)
					.retrieve().bodyToMono(String.class).block());

			return rootNode.get("result").get("count").asText();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Ooops and error happened";
	}

	@GetMapping("countAll")
	public String datasetsCountPerMinistry() throws IOException {

		JsonNode departmentsJson;
		ObjectMapper objectMapper = new ObjectMapper();
		InputStream in = departments.getInputStream();
		StringBuilder sb = new StringBuilder();

		departmentsJson = objectMapper.readValue(in, JsonNode.class);

		// Todo: Map department names to correct equivalents govdata ckan
		departmentsJson.get("departments").forEach(departmentJson -> {

			String department = departmentJson.get("name").asText();
			sb.append(department).append(": ").append(countDatasetsPerDepartment(department)).append("\n");

			if (departmentJson.has("subordinates")) {
				departmentJson.get("subordinates").forEach(subordinateJson -> {

					String subordinate = subordinateJson.get("name").asText();
					sb.append("\t").append(subordinate).append(": ").append(countDatasetsPerDepartment(subordinate))
							.append("\n");
				});
			}
		});

		return sb.toString();
	}

}
