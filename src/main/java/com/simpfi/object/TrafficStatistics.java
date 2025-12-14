package com.simpfi.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.simpfi.sumo.wrapper.EdgeController;
import com.simpfi.sumo.wrapper.SumoConnectionManager;
import com.simpfi.sumo.wrapper.VehicleController;

/**
 * Computes statistics about average speed, vehicle density, travel times, and congestion hotspots.
 */

public class TrafficStatistics {
    /** Connection to SUMO for managing vehicles. */
    private SumoConnectionManager sumo;

    /** Controller to access edge-related traffic data. */
    private EdgeController ec;

    /** Controller to access vehicle-related traffic data. */
    private VehicleController vc;

    private int lastCongestedEdgeCount = 0;

    /** Stores the current speed of each vehicle by vehicle ID. */
    private Map<String, Double> vehicleSpeeds = new HashMap<>();

    /** Stores the number of vehicles currently on each edge by edge ID. */
    private Map<String, Integer> edgeVehicleCount = new HashMap<>();

    /** Stores the simulation step at which each vehicle entered the network. */
    private Map<String, Long> vehicleStartTime = new HashMap<>();

    /** Stores the travel times of vehicles that have exited the network. */
    private List<Long> travelTimes = new ArrayList<>();

    /** Logger instance for capturing warnings and errors. */
    private static final Logger logger = Logger.getLogger(TrafficStatistics.class.getName());
    
    /**
     * Constructs a TrafficStatistics object with the given edge and vehicle controllers. 
     * 
     * 
     * @param ec the EdgeController for accessing edge data * 
     * @param vc the VehicleController for accessing vehicle data 
     */

    public TrafficStatistics(EdgeController ec, VehicleController vc){
        this.ec = ec;
        this.vc = vc;
    }

    /**
    * Updates the traffic statistics for the current simulation step. 
    * This method updates vehicle speeds, edge densities, and tracks travel times for vehicles exiting the network.
    * @param currentStep the current simulation step 
    */
    public void update(int currentStep) {
        try{
            List<String> vehicleIDs = vc.getAllVehicleIds();
            for (String vid : vehicleIDs) {
                double speed = vc.getSpeed(vid);
                vehicleSpeeds.put(vid, speed);

                if (!vehicleStartTime.containsKey(vid)) {
                    vehicleStartTime.put(vid, (long) currentStep);
                }
            }

            List<String> edgeIDs = ec.getEdgeIDs();
            for (String eid : edgeIDs){
                int count = ec.getEdgeVehicleCount(eid);
                edgeVehicleCount.put(eid, count);
            }
            checkExitedVehicles(currentStep);
        }catch (Exception e){
            logger.log(Level.WARNING,"Error updating statistics: ", e);
        }

        lastCongestedEdgeCount = 0;

        for (String eid : edgeVehicleCount.keySet()) {
            try {
                int count = edgeVehicleCount.get(eid);
                if (count == 0) continue;

                double avgSpeed = ec.getMeanSpeed(eid); 
                if (avgSpeed < 5.0) {
                    lastCongestedEdgeCount++;
                }
            } catch (Exception ex) {
                logger.log(Level.WARNING, "Failed to get mean speed for edge " + eid, ex);
            }
        }
    }

    /**
     * Returns the number of road edges that were detected as congested
     * during the most recent congestion evaluation.
     */
    public int getLastCongestedEdgeCount(){
        return lastCongestedEdgeCount;
    }

    
    /** 
    * Checks which vehicles have exited the network and records their travel times. 
    * @param currentStep the current simulation step 
    * @throws Exception if an error occurs while querying vehicle information 
    */
    private void checkExitedVehicles(int currentStep) throws Exception {
        List<String> activeVehicles = vc.getAllVehicleIds();
        Iterator<Map.Entry<String, Long>> it = vehicleStartTime.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Long> entry = it.next();
            String vid = entry.getKey();
            if(!activeVehicles.contains(vid)){
                long travelTime = currentStep - entry.getValue();
                travelTimes.add(travelTime);
                it.remove();
                vehicleSpeeds.remove(vid);
            }

        }
    }

    /** 
     * Computes the average speed of all active vehicles. 
     *  
     * @return the average speed, or 0.0 if no vehicles are active
     */
    public double getAverageSpeed(){
        if (vehicleSpeeds.isEmpty()){
            return 0.0;
        }
        double sum = vehicleSpeeds.values().stream().mapToDouble(Double::doubleValue).sum();
        return sum/vehicleSpeeds.size();
    }

    /**
     * Retrieves the current vehicle density on each edge. 
     *  
     * @return a map of edge IDs to vehicle counts 
     */
    public Map<String, Integer> getEdgeDensity(){
        return edgeVehicleCount;
    }

    /** 
     * Returns a list of recorded travel times for vehicles that have exited the network. 
     *
     * @return a list of travel times in simulation steps 
     */
    public List<Long> getTravelTimes(){
        return travelTimes;
    }

    /** 
     * Returns the recorded travel times as an array of doubles.
     * 
     * @return travel times converted to a double array 
     */
    public double[] getTravelTimesArray() {
        return travelTimes.stream().mapToDouble(Long::doubleValue).toArray();
    }

    /** 
     * Computes the average speed of vehicles on a specific edge. 
     * 
     * @param eid the edge ID 
     * @return the average speed on that edge, or 0 if no vehicles are present 
     */
    private double getAverageSpeedOnEdge(String eid){
        double sum = 0;
        int count = 0;
        for (String vid : vehicleSpeeds.keySet()){
            try {
                String edge = vc.getRoadID(vid);
                if (edge.equals(eid)){
                    sum += vehicleSpeeds.get(vid);
                    count++;
                }
            }catch (Exception e){
                logger.log(Level.WARNING,"Error getting edge for vehicle " + vid, e);
            }
        }
        if (count > 0){
            return sum/count;
        }else return 0;
    }


    /** 
     * Identifies edges that are considered congested based on a speed threshold. 
     * 
     * @param speedThreshold the speed below which an edge is considered congested 
     * @return a list of edge IDs that are congested 
     */
    public List<String> getCongestedEdges(double speedThreshold) {
    List<String> congested = new ArrayList<>();
    for (String eid : edgeVehicleCount.keySet()) {
        try {
            int vehicleCount = ec.getEdgeVehicleCount(eid); 
            if (vehicleCount == 0) continue; 

            double avrSpeed = getAverageSpeedOnEdge(eid);
            if (avrSpeed < speedThreshold) {
                congested.add(eid);
            }
        } catch (Exception ex) {
            System.err.println("Error getting vehicle count for edge " + eid + ": " + ex.getMessage());
        }
    }
    return congested;
}


    /** 
     * Returns a string representing the top K congestion hotspots sorted by vehicle count. 
     * 
     * @param k the number of top congested edges to return 
     * @return a comma-separated string of edge IDs and their vehicle counts 
     */
    public String getTopCongestionHostspots(int k){
        return edgeVehicleCount.entrySet().stream()
        .sorted((a, b) -> b.getValue() - a.getValue())
        .limit(k)
        .map(e -> e.getKey() + " (" + e.getValue() + ")")
        .reduce((a, b) -> a + ", " + b)
        .orElse("None");
    }
}
