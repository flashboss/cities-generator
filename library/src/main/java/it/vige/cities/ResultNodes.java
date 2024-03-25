package it.vige.cities;

import it.vige.cities.result.Nodes;

/**
 * This class represents a container for storing a result and associated nodes.
 * 
 * @author lucastancapiano
 */
public class ResultNodes {

	private Result result; // Stores the result.
	private Nodes nodes; // Stores the nodes associated with the result.
	private Template template; // The template with which it was generated

	/**
	 * Constructs a new ResultNodes object with the specified result and nodes.
	 * 
	 * @param result   The result to be stored in the ResultNodes object.
	 * @param nodes    The nodes to be associated with the result in the ResultNodes
	 *                 object.
	 * @param template The template with which it was generated.
	 */
	public ResultNodes(Result result, Nodes nodes, Template template) {
		this.result = result;
		this.nodes = nodes;
		this.template = template;
	}

	/**
	 * Retrieves the result stored in this container.
	 * 
	 * @return The result stored in this container.
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * Sets the result to be stored in this container.
	 * 
	 * @param result The result to be stored in this container.
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * Retrieves the nodes associated with the result stored in this container.
	 * 
	 * @return The nodes associated with the result stored in this container.
	 */
	public Nodes getNodes() {
		return nodes;
	}

	/**
	 * Sets the nodes associated with the result to be stored in this container.
	 * 
	 * @param nodes The nodes associated with the result to be stored in this
	 *              container.
	 */
	public void setNodes(Nodes nodes) {
		this.nodes = nodes;
	}

	/**
	 * Retrieves the template associated with the result stored in this container.
	 * 
	 * @return The template associated with the result stored in this container.
	 */
	public Template getTemplate() {
		return template;
	}

	/**
	 * Sets the template associated with the result to be stored in this container.
	 * 
	 * @param template The template associated with the result to be stored in this
	 *                 container.
	 */
	public void setTemplate(Template template) {
		this.template = template;
	}
}