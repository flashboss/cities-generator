package it.vige.cities;

import static it.vige.cities.Result.KO;
import static it.vige.cities.Result.OK;

import it.vige.cities.result.Nodes;

/**
 * A template for the file generator
 * @author lucastancapiano
 */
public abstract class Template extends FileGenerator {

	/**
	 * Max level
	 */
	protected final static int MAX_LEVEL = 3;

	/**
	 * Generate
	 * @return the generated nodes
	 * @throws Exception if there is a problem
	 */
	protected abstract Nodes generate() throws Exception;

	/**
	 * Generate file
	 * @return the result of the generation
	 */
	protected Result generateFile() {
		try {
			writeFile(generate());
		} catch (Exception ex) {
			return KO;
		}
		return OK;
	}
}
