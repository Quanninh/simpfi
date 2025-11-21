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

public class RouteXMLReader extends XMLReader {

	public RouteXMLReader(String fileAddress) throws Exception {
		super(fileAddress);
	}

	public List<VehicleType> parseVehicleType() throws Exception {
		NodeList typeNodeList = document.getElementsByTagName("vType");

		List<VehicleType> types = new ArrayList<>();

		int length = typeNodeList.getLength();
		for (int i = 0; i < length; i++) {
			Element type = (Element) typeNodeList.item(i);

			types.add(new VehicleType(type.getAttribute("id"),
				type.getAttribute("vClass")));
		}
		return types;
	}

	public List<Route> parseRoute() throws Exception {
		NodeList typeNodeList = document.getElementsByTagName("route");

		List<Route> routes = new ArrayList<>();

		NetworkXMLReader networkXmlReader = new NetworkXMLReader(
			Constants.SUMO_NETWORK);
		List<Edge> allEdges = networkXmlReader
			.parseEdge(networkXmlReader.parseJunction());

		int length = typeNodeList.getLength();
		for (int i = 0; i < length; i++) {
			Element route = (Element) typeNodeList.item(i);

			String edges = route.getAttribute("edges");
			String[] edgesSplit = edges.split(" ");
			int routeLength = edgesSplit.length;
			Edge[] edgesArr = new Edge[routeLength];

			for (int j = 0; j < routeLength; j++) {
				edgesArr[j] = networkXmlReader.searchForEdge(edgesSplit[j],
					allEdges);
			}

			routes.add(new Route(route.getAttribute("id"), edgesArr));
		}
		return routes;
	}

}
