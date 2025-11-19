package wrapper;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Vehicle;

import java.util.List;

public class VehicleController {

    private final SumoTraciConnection conn;

    public VehicleController(SumoTraciConnection conn) {
        this.conn = conn;
    }

    public List<String> getAllVehicleIDs() throws Exception {
        return (List<String>) conn.do_job_get(Vehicle.getIDList());
    }

    public double getSpeed(String vid) throws Exception {
        return (Double) conn.do_job_get(Vehicle.getSpeed(vid));
    }

    public String getRoadID(String vid) throws Exception {
        return (String) conn.do_job_get(Vehicle.getRoadID(vid));
    }

    public double[] getPosition(String vid) throws Exception {
        return (double[]) conn.do_job_get(Vehicle.getPosition(vid));
    }
}

