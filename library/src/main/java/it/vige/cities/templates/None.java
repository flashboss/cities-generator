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
	 * Generate
	 */
	@Override
	protected ResultNodes generate() throws Exception {
		return new ResultNodes(OK, new Nodes(), this);
	}

}
