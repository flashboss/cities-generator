package it.vige.cities.templates.it;

import static it.vige.cities.Normalizer.execute;
import static it.vige.cities.result.Nodes.ID_SEPARATOR;

import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;
import it.vige.cities.templates.GeoNames;

/**
 * Extra configuration for italian geonames
 * @author lucastancapiano
 */
public class ExtraGeoNames extends GeoNames {

	/**
	 * ExtraGeoNames
	 * @param country         the country
	 * @param caseSensitive   true if it is case sensitive
	 * @param duplicatedNames true if it accepts duplicated names
	 * @param username        the username
	 */
	public ExtraGeoNames(String country, boolean caseSensitive, boolean duplicatedNames, String username) {
		super(country, caseSensitive, duplicatedNames, username);
		firstLevel = 1;
	}

	/**
	 * Generate
	 */
	@Override
	protected Nodes generate() throws Exception {
		Nodes nodes = new Nodes();
		addLevel0(nodes, caseSensitive);
		Nodes nodesFromGeoname = super.generate();
		for (Node node0 : nodes.getZones()) {
			for (Node node1 : nodesFromGeoname.getZones()) {
				if (((node1.getName().equalsIgnoreCase("Piemonte") || node1.getName().equalsIgnoreCase("Liguria")
						|| node1.getName().equalsIgnoreCase("Regione Autonoma Valle d'Aosta")
						|| node1.getName().equalsIgnoreCase("Lombardia"))
						&& node0.getName().equalsIgnoreCase("I: ITALIA NORD-OCCIDENTALE")) ||

						(((node1.getName().equalsIgnoreCase("Emilia-Romagna")
								|| node1.getName().equalsIgnoreCase("Veneto")
								|| node1.getName().equalsIgnoreCase("Trentino-Alto Adige")
								|| node1.getName().equalsIgnoreCase("Friuli Venezia Giulia"))
								&& node0.getName().equalsIgnoreCase("II: ITALIA NORD-ORIENTALE")))
						||

						(((node1.getName().equalsIgnoreCase("Lazio") || node1.getName().equalsIgnoreCase("Marche")
								|| node1.getName().equalsIgnoreCase("Umbria")
								|| node1.getName().equalsIgnoreCase("Toscana"))
								&& node0.getName().equalsIgnoreCase("III: ITALIA CENTRALE")))
						||

						(((node1.getName().equalsIgnoreCase("Abruzzo") || node1.getName().equalsIgnoreCase("Campania")
								|| node1.getName().equalsIgnoreCase("Basilicata")
								|| node1.getName().equalsIgnoreCase("Molise")
								|| node1.getName().equalsIgnoreCase("Calabria")
								|| node1.getName().equalsIgnoreCase("Puglia"))
								&& node0.getName().equalsIgnoreCase("IV: ITALIA MERIDIONALE")))
						||

						(((node1.getName().equalsIgnoreCase("Sardegna") || node1.getName().equalsIgnoreCase("Sicilia"))
								&& node0.getName().equalsIgnoreCase("V: ITALIA INSULARE"))))
					node0.getZones().add(node1);
			}
			changeIds(node0);
		}
		return nodes;
	}

	private void changeIds(Node node) {
		node.getZones().parallelStream().forEach(x -> {
			String prefix = node.getId();
			String suffix = x.getId();
			if (prefix.indexOf('-') > 0)
				prefix = prefix.substring(0, prefix.indexOf(ID_SEPARATOR));
			suffix = suffix.substring(suffix.indexOf('-'));
			x.setId(prefix + suffix);
			changeIds(x);
		});
	}

	/**
	 * 
	 * @param nodes         the nodes
	 * @param caseSensitive true if it is case sensitive
	 */
	private void addLevel0(Nodes nodes, boolean caseSensitive) {
		int counter = 1;
		Node northWest = new Node();
		northWest.setId("" + counter++);
		northWest.setLevel(0);
		northWest.setName(execute(caseSensitive, true, "I: ITALIA NORD-OCCIDENTALE", null));
		nodes.getZones().add(northWest);
		Node northEast = new Node();
		northEast.setId("" + counter++);
		northEast.setLevel(0);
		northEast.setName(execute(caseSensitive, true, "II: ITALIA NORD-ORIENTALE", null));
		nodes.getZones().add(northEast);
		Node south = new Node();
		south.setId("" + counter++);
		south.setLevel(0);
		south.setName(execute(caseSensitive, true, "III: ITALIA CENTRALE", null));
		nodes.getZones().add(south);
		Node centre = new Node();
		centre.setId("" + counter++);
		centre.setLevel(0);
		centre.setName(execute(caseSensitive, true, "IV: ITALIA MERIDIONALE", null));
		nodes.getZones().add(centre);
		Node islands = new Node();
		islands.setId("" + counter++);
		islands.setLevel(0);
		islands.setName(execute(caseSensitive, true, "V: ITALIA INSULARE", null));
		nodes.getZones().add(islands);
	}

}
