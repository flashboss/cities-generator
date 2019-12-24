package it.vige.cities;

import it.vige.cities.result.Nodes;

public abstract class Template extends FileGenerator {
	
	protected final static int MAX_LEVEL = 3;

	protected abstract Nodes generate() throws Exception;

	protected Result generateFile() {
		try {
			writeFile(generate());
		} catch (Exception ex) {
			return Result.KO;
		}
		return Result.OK;
	}
}
