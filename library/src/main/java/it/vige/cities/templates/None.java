package it.vige.cities.templates;

import static it.vige.cities.Result.OK;

import it.vige.cities.ResultNodes;
import it.vige.cities.Template;
import it.vige.cities.result.Nodes;

/**
 * An empty generator used for tests
 * 
 * @author lucastancapiano
 */
public class None extends Template {

	/**
	 * Default constructor for None template
	 * Creates an empty generator used for tests
	 */
	public None() {
		super();
	}

	/**
	 * Generate empty nodes
	 * Returns an empty Nodes object with OK result, used for testing purposes
	 * 
	 * @return ResultNodes with OK result and empty Nodes
	 * @throws Exception if there is a problem (should not occur for this template)
	 */
	@Override
	protected ResultNodes generate() throws Exception {
		return new ResultNodes(OK, new Nodes(), this);
	}

}
