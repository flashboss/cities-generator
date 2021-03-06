package it.vige.cities;

import static it.vige.cities.Result.KO;
import static it.vige.cities.Result.OK;

import it.vige.cities.result.Nodes;

/**
 * 
 * @author lucastancapiano
 *
 *         A template for the file generator
 */
public abstract class Template extends FileGenerator {

	/**
	 * 
	 */
	protected final static int MAX_LEVEL = 3;

	/**
	 * 
	 * @return the generated nodes
	 * @throws Exception if there is a problem
	 */
	protected abstract Nodes generate() throws Exception;

	/**
	 * 
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
