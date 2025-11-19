package wrapper;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Trafficlight;

public class TrafficLightController {
    private final SumoTraciConnection conn;

    public TrafficLightController(SumoTraciConnection conn) {
        this.conn = conn;
    }

    public String getState(String id) throws Exception {
        return (String) conn.do_job_get(Trafficlight.getRedYellowGreenState(id));
    }
}