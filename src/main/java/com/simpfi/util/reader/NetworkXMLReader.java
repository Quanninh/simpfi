package com.simpfi.util.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.simpfi.object.Edge;
import com.simpfi.object.Junction;
import com.simpfi.object.Lane;
import com.simpfi.object.Phase;
import com.simpfi.object.TrafficLight;
import com.simpfi.util.XMLReader;

/**
 * Creates class {@code NetworkXMLReader} that inherits
 * {@link com.simpfi.util.XMLReader} used to parse network components such as
 * Edge and Junction.
 */
public class NetworkXMLReader extends XMLReader {

	public NetworkXMLReader(String fileAddress) throws Exception {

		super(fileAddress);
	}

	private Map<String, Lane> laneMap = new HashMap<String, Lane>();

	/**
	 * Used to parse edges by leveraging methods from {@link org.w3c.dom.Element}.
	 * 
	 * @param junctions list of junctions used to get {@code from} &amp; {@code to}
	 *                  attributes of Edge.
	 * @return list of edges from the XML file.
	 * @throws Exception if the XML structure is invalid.
	 */
	public List<Edge> parseEdge(List<Junction> junctions) throws Exception {

		NodeList edgeNodeList = document.getElementsByTagName("edge");
		laneMap.clear();

		List<Edge> edges = new ArrayList<>();

		for (int i = 0; i < edgeNodeList.getLength(); i++) {
			Element edge = (Element) edgeNodeList.item(i);

			NodeList lanes = edge.getElementsByTagName("lane");
			Lane[] laneArr = new Lane[lanes.getLength()];

			for (int j = 0; j < lanes.getLength(); j++) {
				Element lane = (Element) lanes.item(j);

				String shape = lane.getAttribute("shape");

				laneArr[j] = new Lane(lane.getAttribute("id"), extractPoints(shape));
				laneMap.put(laneArr[j].getLaneId(), laneArr[j]);
			}

			Junction from = searchForJunction(edge.getAttribute("from"), junctions);
			Junction to = searchForJunction(edge.getAttribute("to"), junctions);

			Edge e = new Edge(edge.getAttribute("id"), from, to, laneArr);
			edges.add(e);
		}

		return edges;
	}

	/**
	 * Used to parse junctions by leveraging methods from
	 * {@link org.w3c.dom.Element}.
	 * 
	 * @return list of junctions from the XML file.
	 * @throws Exception if the XML structure is invalid.
	 */
	public List<Junction> parseJunction() throws Exception {

		NodeList junctionNodeList = document.getElementsByTagName("junction");

		List<Junction> junctions = new ArrayList<>();

		for (int i = 0; i < junctionNodeList.getLength(); i++) {
			Element junction = (Element) junctionNodeList.item(i);

			String junctionId = junction.getAttribute("id");
			String junctionType = junction.getAttribute("type");

			if (junctionType.equals("internal")) {
				continue;
			}

			String shape = junction.getAttribute("shape");

			Junction j = new Junction(junctionId, junctionType, extractPoints(shape));

			String[] incomingLaneIds = junction.getAttribute("incLanes").trim().split("\\s+");

			for (String id : incomingLaneIds) {
				j.addIncomingLane(id);
			}
			junctions.add(j);
		}
		return junctions;

	}

	public List<TrafficLight> parseTrafficLight(List<Junction> junctions, List<Edge> edges) throws Exception {
		List<TrafficLight> trafficLights = new ArrayList<TrafficLight>();

		NodeList trafficLightLogic = document.getElementsByTagName("tlLogic");

		for (int i = 0; i < trafficLightLogic.getLength(); i++) {
			Element traffcLight = (Element) trafficLightLogic.item(i);
			String idJunction = traffcLight.getAttribute("id");

			NodeList phaseNodeList = traffcLight.getElementsByTagName("phase");
			Phase[] listOfPhase = new Phase[phaseNodeList.getLength()];

			for (int j = 0; j < phaseNodeList.getLength(); j++) {
				Element phase = (Element) phaseNodeList.item(j);

				Double duration = Double.parseDouble(phase.getAttribute("duration"));
				String state = phase.getAttribute("state");

				listOfPhase[j] = new Phase(duration, state);
			}

			Junction junction = searchForJunction(idJunction, junctions);
			String junctionType = junction.getType();

			if (!junctionType.equals("traffic_light")) {
				continue;
			}

			List<String> incomingLanes = junction.getIncomingLane();
			Lane[] lanes = new Lane[incomingLanes.size()];

			for (int k = 0; k < incomingLanes.size(); k++) {
				Lane lane = Lane.searchForLane(incomingLanes.get(k), edges);
				lanes[k] = lane;
			}

			TrafficLight tl = new TrafficLight(junction, "static", lanes);
			tl.setPhase(listOfPhase);
			trafficLights.add(tl);
		}
		return trafficLights;
	}

	/**
	 * Used to search over a list of junctions to find one with the matched id.
	 * 
	 * @param id        id of the junction that users look for.
	 * @param junctions given list of junctions.
	 * @return the junction with the passed id, {@code null} if not found.
	 */
	public Junction searchForJunction(String id, List<Junction> junctions) {
		for (int i = 0; i < junctions.size(); i++) {
			if (junctions.get(i).getId().equals(id)) {
				return junctions.get(i);
			}
		}
		return null;
	}

}
