package de.dittwald.challenges.govdatadashboard.department;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A client to read the departments.json from classpath.
 */
@Controller
public class DepartmentsJsonResourceLoader {

	@Value("classpath:departments.json")
	private Resource departments;

	/**
	 * Returns all departments and subordinates from the departments.json as
	 * JsonNode.
	 * 
	 * @return The departments as JsonNode
	 * @throws IOException
	 */
	public JsonNode getAllDepartments() throws IOException {

		ObjectMapper departmentMapper = new ObjectMapper();
		InputStream in = this.departments.getInputStream();

		return departmentMapper.readValue(in, JsonNode.class);
	}

}
