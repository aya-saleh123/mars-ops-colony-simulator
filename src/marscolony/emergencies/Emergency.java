package marscolony.emergencies;
import java.util.ArrayList;
import java.util.List;

import marscolony.facilities.Facilities;
import marscolony.personnel.Agent;

public abstract class Emergency {

    private Facilities facility;
    private  List<Agent> responders = new ArrayList<>();
    public Emergency( Facilities facility, List<Agent> responders) {
        this.facility = facility;
        this.responders.addAll(responders);
    }
   
     public String getLocation() { //get the location of the emergency
        return facility.getLocationName();   
    }
    public abstract void respondToEmergency(); //respond to the emergency
      
    

}