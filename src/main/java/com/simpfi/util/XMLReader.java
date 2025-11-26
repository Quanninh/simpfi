package com.simpfi.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * Creates XMLReader used to read and parse XML files.
 */
public class XMLReader {

	protected File file;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	protected Document document;

	/**
	 * Constructor used to initialize and parse XML files.
	 * 
	 * @param fileAddress directory of the XML file
	 * @throws Exception if the file address is invalid.
	 */
	public XMLReader(String fileAddress) throws Exception {
		file = new File(fileAddress);
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		document = builder.parse(file);
	}

	/**
	 * Used to convert a String of multiple pairs of coordinates to an array of
	 * Point.
	 * 
	 * @param shape the String that contains pairs of coordinates with the format:
	 *              "x,y u,t a,b ...".
	 * @return Array of Points.
	 */
	protected Point[] extractPoints(String shape) {
		String[] points = shape.split(" ");
		Point[] pointArr = new Point[points.length];

		for (int k = 0; k < points.length; k++) {
			String[] point = points[k].split(",");

			pointArr[k] = new Point(Double.parseDouble(point[0]), Double.parseDouble(point[1]));
		}

		return pointArr;
	}

}
