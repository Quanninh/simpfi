package com.simpfi.util;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class XMLReader {

	protected File file;
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	protected Document document;

	public XMLReader(String fileAddress) throws Exception {
		file = new File(fileAddress);
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		document = builder.parse(file);
	}

	protected Point[] extractPoints(String shape) {
		String[] points = shape.split(" ");
		Point[] pointArr = new Point[points.length];

		for (int k = 0; k < points.length; k++) {
			String[] point = points[k].split(",");

			pointArr[k] = new Point(Double.parseDouble(point[0]),
				Double.parseDouble(point[1]));
		}

		return pointArr;
	}

}
