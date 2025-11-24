package it.vige.cities.result;

/**
 * The node of the location
 * 
 * @author lucastancapiano
 */
public class Node extends Nodes {

	/**
	 * Hierarchical level of the node (0 = region, 1 = province, 2 = municipality)
	 */
	private int level;
	
	/**
	 * Name of the location
	 */
	private String name;
	
	/**
	 * Unique identifier for the node
	 */
	private String id;

	/**
	 * Default constructor for Node
	 * Creates an empty node
	 */
	public Node() {

	}

	/**
	 * Get the hierarchical level of the node
	 * 
	 * @return the level (0 = region, 1 = province, 2 = municipality)
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Set the hierarchical level of the node
	 * 
	 * @param level the level to set (0 = region, 1 = province, 2 = municipality)
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Get the name of the location
	 * 
	 * @return the name of the location
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the location
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the unique identifier of the node
	 * 
	 * @return the unique identifier
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the unique identifier of the node
	 * 
	 * @param id the identifier to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Clone this Node object
	 * Creates a shallow copy of the node
	 * 
	 * @return a cloned Node object
	 */
	@Override
	public Object clone() {
		Node node = (Node) super.clone();
		return node;
	}
}
