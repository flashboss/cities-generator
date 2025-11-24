package it.vige.cities.result;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Nodes containing the informations of the cities from the providers
 * @author lucastancapiano
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Nodes implements Cloneable {

	/**
	 * Separator for id node (used in hierarchical IDs)
	 */
	public final static String ID_SEPARATOR = "-";
	
	/**
	 * List of zone nodes (hierarchical structure)
	 */
	private List<Node> zones = new ArrayList<Node>();
	
	/**
	 * Default constructor for Nodes
	 * Creates an empty nodes container
	 */
	public Nodes() {
		
	}

	/**
	 * Get the list of zone nodes
	 * 
	 * @return the list of zone nodes
	 */
	public List<Node> getZones() {
		return zones;
	}

	/**
	 * Set the list of zone nodes
	 * 
	 * @param zones the list of zone nodes to set
	 */
	public void setZones(List<Node> zones) {
		this.zones = zones;
	}

	/**
	 * Clone this Nodes object and all its child nodes
	 * Creates a deep copy of the nodes hierarchy
	 * 
	 * @return a cloned Nodes object, or null if cloning fails
	 */
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
