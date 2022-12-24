package it.vige.cities.result;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodes containing the informations of the cities from the providers
 * @author lucastancapiano
 */
public class Nodes implements Cloneable {

	/**
	 * Separator for id node
	 */
	public final static String ID_SEPARATOR = "-";
	
	private List<Node> zones = new ArrayList<Node>();
	
	/**
	 * default nodes
	 */
	public Nodes() {
		
	}

	/**
	 * Zones
	 * @return the list of nodes
	 */
	public List<Node> getZones() {
		return zones;
	}

	/**
	 * Zones
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
