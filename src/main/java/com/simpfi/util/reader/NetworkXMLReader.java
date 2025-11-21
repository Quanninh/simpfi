package com.simpfi.util.reader;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Lane;
import com.simpfi.util.XMLReader;

public class NetworkXMLReader extends XMLReader {

	public NetworkXMLReader(String fileAddress) throws Exception {
		super(fileAddress);
	}

	public List<Edge> parseEdge(List<Junction> junctions) throws Exception {

		NodeList edgeNodeList = document.getElementsByTagName("edge");

		List<Edge> edges = new ArrayList<>();

		for (int i = 0; i < edgeNodeList.getLength(); i++) {
			Element edge = (Element) edgeNodeList.item(i);

			NodeList lanes = edge.getElementsByTagName("lane");
			Lane[] laneArr = new Lane[lanes.getLength()];

			for (int j = 0; j < lanes.getLength(); j++) {
				Element lane = (Element) lanes.item(j);

				String shape = lane.getAttribute("shape");

				laneArr[j] = new Lane(lane.getAttribute("id"),
					extractPoints(shape));
			}

			Junction from = searchForJunction(edge.getAttribute("from"),
				junctions);
			Junction to = searchForJunction(edge.getAttribute("to"), junctions);

			Edge e = new Edge(edge.getAttribute("id"), from, to, laneArr);
			edges.add(e);
		}

		return edges;
	}

	public List<Junction> parseJunction() throws Exception {

		NodeList edgeNodeList = document.getElementsByTagName("junction");

		List<Junction> junctions = new ArrayList<>();

		for (int i = 0; i < edgeNodeList.getLength(); i++) {
			Element junction = (Element) edgeNodeList.item(i);

			String junctionId = junction.getAttribute("id");
			String junctionType = junction.getAttribute("type");

			if (junctionType.equals("internal")) {
				continue;
			}

			String shape = junction.getAttribute("shape");

			Junction j = new Junction(junctionId, junctionType,
				extractPoints(shape));
			junctions.add(j);
		}

		return junctions;
	}

	public Junction searchForJunction(String id, List<Junction> junctions) {
		for (int i = 0; i < junctions.size(); i++) {
			if (junctions.get(i).getId().equals(id)) {
				return junctions.get(i);
			}
		}
		return null;
	}

	public Edge searchForEdge(String id, List<Edge> edges) {
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).getId().equals(id)) {
				return edges.get(i);
			}
		}
		return null;
	}

}
