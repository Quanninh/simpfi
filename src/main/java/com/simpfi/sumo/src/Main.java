import wrapper.SumoConnectionManager;
import wrapper.VehicleController;
import wrapper.TrafficLightController;

import de.tudresden.sumo.cmd.Simulation;

public class Main {
    public static void main(String[] args) throws Exception {

        String cfg = "resource/simulation.sumocfg";

        double stepLen = 0.1;
        long stepMs = (long)(stepLen * 1000);

        // Start SUMO through your wrapper
        SumoConnectionManager sim = new SumoConnectionManager(cfg);

        VehicleController vehicles = new VehicleController(sim.getConnection());
        TrafficLightController tls = new TrafficLightController(sim.getConnection());

        try {
            long next = System.currentTimeMillis();
            System.out.println("hi");
            for (int k = 0; k < 600; k++) {

                sim.doStep();   // <-- now works

                // double time = ((Double) sim.getConnection()
                //         .do_job_get(Simulation.getCurrentTime())) / 1000.0;

                for (String vid : vehicles.getAllVehicleIDs()) {
                    double speed = vehicles.getSpeed(vid);
                    String edge = vehicles.getRoadID(vid);
                    //System.out.printf("t=%.1fs id=%s v=%.2f m/s edge=%s%n", time, vid, speed, edge);
                    System.out.printf("id=%s v=%.2f m/s edge=%s%n",
                             vid, speed, edge);
                }

                next += stepMs;
                // long sleep = next - System.currentTimeMillis();
                // if (sleep > 0) Thread.sleep(sleep);
                Thread.sleep(100);
            }

        } finally {
            sim.close();
        }
    }
}
