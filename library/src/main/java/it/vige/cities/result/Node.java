package it.vige.cities.result;

/**
 * 
 * @author lucastancapiano
 *
 *         The node of the location
 */
public class Node extends Nodes {

	private int level;
	private String name;
	private String id;

	/**
	 * 
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 
	 * @param level the level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id the id
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Object clone() {
		Node node = (Node) super.clone();
		return node;
	}
}
