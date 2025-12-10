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
    private SumoConnectionManager sumo;
    private EdgeController ec;
    private VehicleController vc;

    private Map<String, Double> vehicleSpeeds = new HashMap<>();
    private Map<String, Integer> edgeVehicleCount = new HashMap<>();
    private Map<String, Long> vehicleStartTime = new HashMap<>();
    private List<Long> travelTimes = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(TrafficStatistics.class.getName());

    public TrafficStatistics(EdgeController ec, VehicleController vc){
        this.ec = ec;
        this.vc = vc;
    }

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
    }

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

    public double getAverageSpeed(){
        if (vehicleSpeeds.isEmpty()){
            return 0.0;
        }
        double sum = vehicleSpeeds.values().stream().mapToDouble(Double::doubleValue).sum();
        return sum/vehicleSpeeds.size();
    }

    public Map<String, Integer> getEdgeDensity(){
        return edgeVehicleCount;
    }

    public List<Long> getTravelTimes(){
        return travelTimes;
    }

    public double[] getTravelTimesArray() {
        return travelTimes.stream().mapToDouble(Long::doubleValue).toArray();
    }

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

    public List<String> getCongestedEdges(double speedThreshold){
        List<String> congested = new ArrayList<>();
        for (String eid : edgeVehicleCount.keySet()){
            double avrSpeed = getAverageSpeedOnEdge(eid);
            if (avrSpeed < speedThreshold){
                congested.add(eid);
            }
        }
        return congested;
    }

    public String getTopCongestionHostspots(int k){
        return edgeVehicleCount.entrySet().stream()
        .sorted((a, b) -> b.getValue() - a.getValue())
        .limit(k)
        .map(e -> e.getKey() + " (" + e.getValue() + ")")
        .reduce((a, b) -> a + ", " + b)
        .orElse("None");
    }
}
