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
        if (facility == null) {
            throw new IllegalArgumentException("Emergency facility cannot be null.");
        }
        this.facility = facility;
    }

    public void triggerEmergency() {
        System.out.println("Emergency: " + getClass().getSimpleName() + " at " + facility.getLocationName());
    }

    public Facilities getFacility() {
        return facility;
    }

    public FailureReason getReason() {
        return failReason;
    }

    protected void setReason(FailureReason failReason) {
        this.failReason = failReason;
    }

    public List<Agent> getResponders() {
        return responders;
    }

    public EmergencyStatus getStatus() {
        return status;
    }

    protected void setStatus(EmergencyStatus status) {
        this.status = status;
    }

    public String getLocation() { 
        return facility.getLocationName();
    }

    public void addResponder(Agent responder) {
        if (responder != null && !responders.contains(responder)) {
            responders.add(responder);
        }
    }

    protected void dispatchResponder(Agent responder) {
        if (responder == null || !responder.isAvailable()) {
            throw new IllegalArgumentException("Only an available responder can be dispatched.");
        }
        addResponder(responder);
        responder.setAvailable(false);
        responder.setLocation(getLocation());
    }

    protected void releaseResponders() {
        for (Agent responder : responders) {
            if (responder != null && !responder.isAvailable()) {
                responder.justFinished();
            }
        }
    }

    public abstract void respondToEmergency(); 

    public abstract void findResponders(); 

}