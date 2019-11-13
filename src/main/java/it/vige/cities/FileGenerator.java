package it.vige.cities;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.vige.cities.result.Nodes;

public class FileGenerator {

	private ObjectMapper mapper = new ObjectMapper();

	protected void writeFile(Nodes nodes) throws Exception {
		mapper.writeValue(new File("build/output.json"), nodes);
	}

	protected Nodes readFile() throws Exception {
		return mapper.readValue(new File("build/output.json"), Nodes.class);
	}

}
