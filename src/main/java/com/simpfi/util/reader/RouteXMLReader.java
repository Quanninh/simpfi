package com.simpfi.util.reader;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.simpfi.config.Constants;
import com.simpfi.object.Edge;
import com.simpfi.object.Route;
import com.simpfi.object.VehicleType;
import com.simpfi.util.XMLReader;

// TODO: Auto-generated Javadoc
/**
 * Creates class {@code RouteXMLReader} that inherits
 * {@link com.simpfi.util.XMLReader} used to parse route components such as
 * Route and VehicleType.
 */
public class RouteXMLReader extends XMLReader {

	/**
	 * calls the Constructor of superclass {@link XMLReader} and passes the given
	 * file address.
	 *
	 * @param fileAddress address of the file to be read
	 * @throws Exception if reading of the file fails
	 */
	public RouteXMLReader(String fileAddress) throws Exception {
		super(fileAddress);
	}

	/**
	 * Used to parse vehicle types by leveraging methods from
	 * {@link org.w3c.dom.Element}.
	 * 
	 * @return list of vehicle types from the XML file.
	 * @throws Exception if the XML structure is invalid.
	 */
	public List<VehicleType> parseVehicleType() throws Exception {
		NodeList typeNodeList = document.getElementsByTagName("vType");

		List<VehicleType> types = new ArrayList<>();

		int length = typeNodeList.getLength();
		for (int i = 0; i < length; i++) {
			Element type = (Element) typeNodeList.item(i);

			types.add(new VehicleType(type.getAttribute("id"), type.getAttribute("vClass")));
		}
		return types;
	}

	/**
	 * Used to parse routes by leveraging methods from {@link org.w3c.dom.Element}.
	 * Get the {@code edges} attribute of Route by using an instance of
	 * {@link com.simpfi.util.reader.NetworkXMLReader}.
	 * 
	 * @return list of routes from the XML file.
	 * @throws Exception if the XML structure is invalid.
	 */
	public List<Route> parseRoute() throws Exception {
		NodeList typeNodeList = document.getElementsByTagName("route");
		List<Route> routes = new ArrayList<>();

		NetworkXMLReader networkXmlReader = new NetworkXMLReader(Constants.SUMO_NETWORK);
		List<Edge> allEdges = networkXmlReader.parseEdge(networkXmlReader.parseJunction());

		int length = typeNodeList.getLength();
		for (int i = 0; i < length; i++) {
			Element route = (Element) typeNodeList.item(i);

			String edges = route.getAttribute("edges");
			String[] edgesSplit = edges.split(" ");
			int routeLength = edgesSplit.length;
			Edge[] edgesArr = new Edge[routeLength];

			for (int j = 0; j < routeLength; j++) {
				edgesArr[j] = Edge.searchForEdge(edgesSplit[j], allEdges);
			}

			routes.add(new Route(route.getAttribute("id"), edgesArr));
		}
		return routes;
	}

}
