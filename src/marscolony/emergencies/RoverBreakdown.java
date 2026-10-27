package marscolony.emergencies;

import java.util.List;

import marscolony.facilities.Facilities;
import marscolony.personnel.Agent;

public class RoverBreakdown extends Emergency{

public RoverBreakdown(Facilities facility, List<Agent> responders)//Do not forget agent
{
    super(facility, responders); 
}



}