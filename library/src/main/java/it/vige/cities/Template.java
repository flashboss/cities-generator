package it.vige.cities;

import static it.vige.cities.Result.KO;
import static it.vige.cities.Result.OK;

import it.vige.cities.result.Nodes;

/**
 * A template for the file generator
 * 
 * @author lucastancapiano
 */
public abstract class Template extends FileGenerator {

	/**
	 * Max level
	 */
	protected final static int MAX_LEVEL = 3;

	/**
	 * Generate
	 * 
	 * @return the generated nodes
	 * @throws Exception if there is a problem
	 */
	protected abstract ResultNodes generate() throws Exception;

	/**
	 * default template
	 */
	public Template() {

	}

	/**
	 * Generate file
	 * 
	 * @return the result of the generation
	 */
	protected ResultNodes generateFile() {
		Nodes nodes = null;
		try {
			nodes = generate().getNodes();
			writeFile(nodes);
		} catch (Exception ex) {
			return new ResultNodes(KO, nodes, this);
		}
		return new ResultNodes(OK, nodes, this);
	}
}
