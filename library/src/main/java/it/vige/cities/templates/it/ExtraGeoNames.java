package it.vige.cities.templates.it;

import it.vige.cities.Normalizer;
import it.vige.cities.result.Node;
import it.vige.cities.result.Nodes;
import it.vige.cities.templates.GeoNames;

/**
 * 
 * @author lucastancapiano
 *
 *         Extra configuration for italian geonames
 */
public class ExtraGeoNames extends GeoNames {

	/**
	 * 
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
	 * 
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
		}
		return nodes;
	}

	/**
	 * 
	 * @param nodes         the nodes
	 * @param caseSensitive true if it is case sensitive
	 */
	private void addLevel0(Nodes nodes, boolean caseSensitive) {
		int counter = 0;
		Node northWest = new Node();
		northWest.setId(counter++);
		northWest.setLevel(0);
		northWest.setName(Normalizer.execute(caseSensitive, true, "I: ITALIA NORD-OCCIDENTALE", null));
		nodes.getZones().add(northWest);
		Node northEast = new Node();
		northEast.setId(counter++);
		northEast.setLevel(0);
		northEast.setName(Normalizer.execute(caseSensitive, true, "II: ITALIA NORD-ORIENTALE", null));
		nodes.getZones().add(northEast);
		Node south = new Node();
		south.setId(counter++);
		south.setLevel(0);
		south.setName(Normalizer.execute(caseSensitive, true, "III: ITALIA CENTRALE", null));
		nodes.getZones().add(south);
		Node centre = new Node();
		centre.setId(counter++);
		centre.setLevel(0);
		centre.setName(Normalizer.execute(caseSensitive, true, "IV: ITALIA MERIDIONALE", null));
		nodes.getZones().add(centre);
		Node islands = new Node();
		islands.setId(counter++);
		islands.setLevel(0);
		islands.setName(Normalizer.execute(caseSensitive, true, "V: ITALIA INSULARE", null));
		nodes.getZones().add(islands);
	}

}
