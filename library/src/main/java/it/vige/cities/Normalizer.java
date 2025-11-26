package it.vige.cities;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;

import it.vige.cities.result.Node;

/**
 * Normalizer for the names of the cities. According the configuration they will
 * be formatted
 * 
 * @author lucastancapiano
 */
public class Normalizer {

	private static final Logger logger = getLogger(Normalizer.class);

	/**
	 * Default constructor for Normalizer
	 * Creates a new normalizer instance
	 */
	public Normalizer() {
		
	}
	
	/**
	 * Set name for a node according to configuration
	 * Normalizes the name based on case sensitivity and duplicate name settings
	 * 
	 * @param caseSensitive   true if names should be case-sensitive, false to convert to uppercase
	 * @param duplicatedNames true if duplicate names are allowed, false to append count suffix
	 * @param text            the original text to normalize
	 * @param zones           the list of zones to check for duplicates
	 * @param node            the node to set the name on
	 */
	public static void setName(boolean caseSensitive, boolean duplicatedNames, final String text, List<Node> zones,
			Node node) {
		logger.debug("Setting name - original: {}, caseSensitive: {}, duplicatedNames: {}", text, caseSensitive, duplicatedNames);
		String newText = text;
		if (!caseSensitive) {
			newText = newText.toUpperCase();
			logger.debug("Converted to uppercase: {}", newText);
		}
		if (!duplicatedNames) {
			long count = countDuplicatesByName(zones, newText);
			logger.debug("Duplicate count for '{}': {}", newText, count);
			if (count > 0) {
				newText = newText + " (" + count + ")";
				logger.debug("Added duplicate suffix: {}", newText);
			}
		}
		node.setName(newText);
		logger.debug("Final name set: {}", newText);
	}

	/**
	 * Counts the number of nodes with a duplicate name within the specified list of
	 * nodes.
	 *
	 * @param nodes the list of nodes to search for duplicates
	 * @param name  the name to search for duplicates of
	 * @return the number of nodes with a duplicate name
	 */
	public static long countDuplicatesByName(List<Node> nodes, String name) {
		if (nodes != null)
			return countDuplicates(nodes, n -> getName(n).equals(name));
		else
			return 0;
	}

	/**
	 * Counts the number of nodes that satisfy the specified predicate within the
	 * specified list of nodes.
	 *
	 * @param nodes     the list of nodes to search
	 * @param predicate the predicate to test each node against
	 * @return the number of nodes that satisfy the predicate
	 */
	private static long countDuplicates(List<Node> nodes, Predicate<Node> predicate) {
		long count = nodes.stream().filter(predicate).count();
		for (Node node : nodes) {
			count += countDuplicates(node.getZones(), predicate);
		}
		return count;
	}

	/**
	 * Gets the name from the specified node.
	 *
	 * @param node the node to extract the name from
	 * @return the extracted name from the node, excluding any text within
	 *         parentheses
	 */
	private static String getName(Node node) {
		String name = node.getName();
		if (name.contains(" ("))
			return name.substring(0, name.indexOf(" ("));
		else
			return name;
	}

}
