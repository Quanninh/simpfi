package com.simpfi.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Lane;

public class XMLReader {

	private File file;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document document;

	public XMLReader(String fileAddress) throws Exception {
		file = new File(fileAddress);
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		document = builder.parse(file);
	}

	public List<Edge> parseEdge(List<Junction> junctions)
		throws Exception {

		NodeList edgeNodeList = document.getElementsByTagName("edge");

		List<Edge> edges = new ArrayList<>();

		for (int i = 0; i < edgeNodeList.getLength(); i++) {
			Element edge = (Element) edgeNodeList.item(i);

			NodeList lanes = edge.getElementsByTagName("lane");
			Lane[] laneArr = new Lane[lanes.getLength()];

			for (int j = 0; j < lanes.getLength(); j++) {
				Element lane = (Element) lanes.item(j);

				String shape = lane.getAttribute("shape");

				laneArr[j] = new Lane(lane.getAttribute("id"), extractPoints(shape));
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
			// System.out.println("Reading Junction: " + junctionId);

			if (junctionType.equals("internal")) {
				continue;
			}

			String shape = junction.getAttribute("shape");

			Junction j = new Junction(junctionId, junctionType, extractPoints(shape));
			junctions.add(j);
		}

		return junctions;
	}
	
	private Point[] extractPoints(String shape) {
		String[] points = shape.split(" ");
		Point[] pointArr = new Point[points.length];

		for (int k = 0; k < points.length; k++) {
			String[] point = points[k].split(",");

			pointArr[k] = new Point(Double.parseDouble(point[0]),
				Double.parseDouble(point[1]));
		}
		
		return pointArr;
	}

	public Junction searchForJunction(String id, List<Junction> junctions) {
		for (int i = 0; i < junctions.size(); i++) {
			if (junctions.get(i).getId().equals(id)) {
				return junctions.get(i);
			}
		}
		return null;
	}

}
