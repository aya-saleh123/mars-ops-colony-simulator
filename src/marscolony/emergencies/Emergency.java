package marscolony.emergencies;

import java.util.ArrayList;
import java.util.List;
import marscolony.facilities.Facilities;
import marscolony.personnel.Agent;
import marscolony.missions.*;

public abstract class Emergency {
    private Facilities facility;
    private List<Agent> responders = new ArrayList<>();
    protected EmergencyStatus status = EmergencyStatus.IN_PROGRESS;
    protected FailureReason failReason = null;

    protected Emergency(Facilities facility) {
        this.facility = facility;
    }

    public void triggerEmergency() {
        System.out.println("Emergency: " + getClass().getSimpleName() + " at " + facility.getLocationName());
    }

    public Facilities getFacility() {
        return facility;
    }

    public List<Agent> getResponders() {
        return responders;
    }

    public String getLocation() { // get the location of the emergency
        return facility.getLocationName();
    }

    public void addResponder(Agent responder) { // add a responder to the emergency
        responders.add(responder);
    }

    public abstract void respondToEmergency(); // respond to the emergency
    public abstract void findResponders(); // find responders for the emergency

}