package marscolony.emergencies;

import java.util.List;

import marscolony.facilities.*;
import marscolony.personnel.Agent;

public class OxygenLeak extends Emergency {
    Habitat hab;
    public OxygenLeak(Facilities facility, List<Agent> responders, Habitat hab)//do not forget agent
    {
        super(facility, responders);
        this.hab=hab;
    }

    




}