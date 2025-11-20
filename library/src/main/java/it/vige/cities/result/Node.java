package it.vige.cities.result;

/**
 * The node of the location
 * 
 * @author lucastancapiano
 */
public class Node extends Nodes {

	private int level;
	private String name;
	private String id;

	/**
	 * default node
	 */
	public Node() {

	}

	/**
	 * Level
	 * 
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Level
	 * 
	 * @param level the level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name
	 * 
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Id
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Id
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
