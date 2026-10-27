package marscolony.emergencies;

import java.util.List;

import marscolony.facilities.Facilities;
import marscolony.personnel.Agent;

public class MedicalEmergency extends Emergency{

public MedicalEmergency(Facilities facility, List<Agent> responders)//do not forget agent
{
    super(facility, responders);
}


}