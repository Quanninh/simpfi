package com.simpfi.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {

	public XMLReader(String fileAddress) throws Exception {
		File file = new File(fileAddress);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document doc = builder.parse(file);
		
		NodeList edgeList = doc.getElementsByTagName("edge");
		
		for (int i = 0; i < edgeList.getLength(); i++) {
	        Element edge = (Element) edgeList.item(i);

	        String edgeId = edge.getAttribute("id");
	        String from = edge.getAttribute("from");
	        String to = edge.getAttribute("to");

	        System.out.println("EDGE ID: " + edgeId);
	        System.out.println("  from: " + from);
	        System.out.println("  to  : " + to);

	        // Now read its lanes
	        NodeList lanes = edge.getElementsByTagName("lane");
	        for (int j = 0; j < lanes.getLength(); j++) {
	            Element lane = (Element) lanes.item(j);

	            String laneId = lane.getAttribute("id");
	            String shape = lane.getAttribute("shape");   // **positions here!!**

	            System.out.println("    LANE ID: " + laneId);
	            System.out.println("    SHAPE:   " + shape);

	            // Optional: Parse coordinates into usable numbers
	            String[] points = shape.split(" ");

	            for (String p : points) {
	                String[] xy = p.split(",");
	                double x = Double.parseDouble(xy[0]);
	                double y = Double.parseDouble(xy[1]);

	                System.out.println("      -> x: " + x + ", y: " + y);
	            }
	        }

	        System.out.println("---------------------------------------------");
	    }
	}
	
}
