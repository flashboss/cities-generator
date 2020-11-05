package it.vige.cities.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author lucastancapiano
 *
 *         A set of nodes
 */
public class Nodes {

	private List<Node> zones = new ArrayList<Node>();

	/**
	 * 
	 * @return the list of nodes
	 */
	public List<Node> getZones() {
		return zones;
	}

	/**
	 * 
	 * @param zones the list of zones
	 */
	public void setZones(List<Node> zones) {
		this.zones = zones;
	}
}
