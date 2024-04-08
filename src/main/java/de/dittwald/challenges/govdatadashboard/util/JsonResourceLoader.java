package de.dittwald.challenges.govdatadashboard.util;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A client to read the departments.json from classpath.
 */
public class JsonResourceLoader {

	/**
	 * Reads the JSON file from the given resource.
	 * 
	 * @return The given JSON file as JsonNode
	 * @throws IOException
	 */
	public static JsonNode getAsJson(Resource resource) throws IOException {

		ObjectMapper departmentMapper = new ObjectMapper();
		InputStream in = resource.getInputStream();

		return departmentMapper.readValue(in, JsonNode.class);
	}

}
