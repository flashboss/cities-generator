package it.vige.cities.result;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author lucastancapiano
 *
 *         A set of nodes
 */
public class Nodes implements Cloneable {

	public final static String ID_SEPARATOR = "-";
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

	@Override
	public Object clone() {
		try {
			Nodes nodes = (Nodes) super.clone();
			List<Node> zones = nodes.getZones();
			nodes.setZones(zones.parallelStream().map(p -> (Node) p.clone()).collect(toList()));
			return nodes;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
