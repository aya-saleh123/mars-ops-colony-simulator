package marscolony.missions;
import marscolony.MarsColony.MarsColony;
import marscolony.conditions.EnvironmentCondition;
import marscolony.facilities.Facilities;
import marscolony.facilities.FacilityStatus;
import marscolony.facilities.Laboratory;
import marscolony.personnel.Engineer;
import marscolony.personnel.Human;
import marscolony.personnel.RepairRobot;
import marscolony.personnel.RoboticUnit;
import marscolony.personnel.Scientist;
import marscolony.resources.ResourceType;
import marscolony.Equipment.*;
import java.util.ArrayList;
import java.util.List;
public class Research extends Mission {
    
     public Research(int duration) {
        super("Research", duration);
    }

    @Override
    public int getRequiredAmount(ResourceType type) {
        if (type == ResourceType.WATER) { return 5; }
        if (type == ResourceType.BATTERY_POWER) { return 10; }
        return 0;
    }

    @Override
    public List<Capability> getRequiredCapabilities() {
         List <Capability> list=new ArrayList<>();
         list.add(Capability.SCIENCE);
         return list;

    }



     public String getEnvironmentProblem(EnvironmentCondition env) {
     
        if (env == EnvironmentCondition.COMMUNICATION_DISRUPTION) {
            return "Research needs communication but it is disrupted";
        }
        else
            return null;
    
    }


    @Override 
    public boolean checkRequiredForMission()
        {
            boolean humanStatus = false , equipmentStatus = false ,labStatus = false  ;
            for(Human human : MarsColony.getHumans())
            {
                    humanStatus = human.canPerform(Capability.SCIENCE); 
                    if(humanStatus)
                    {
                        this.assignAgent(human);
                        break;
                    }
            }
            for(Equipment equipment : MarsColony.getEquipments() )
            {
                if( equipment.getType() == EquipmentType.SCIENTIFIC_EQUIPMENT && equipment.isOperationalStatus())
                {
                    equipmentStatus = true;
                }
            }

            for(Facilities facility : MarsColony.getFacilities())
            {
                if(facility instanceof Laboratory)
                {
                    if(facility.getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL)
                    {
                        setTarget(facility);
                        labStatus = true;
                        break;
                    }
                }
            }

            
            if(humanStatus && equipmentStatus && labStatus){
                return true;
            }
            else
            {
                releaseAgents();
                return false;
            }
        }

}
