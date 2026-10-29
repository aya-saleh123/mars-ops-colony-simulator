package marscolony.missions;
import java.util.List;

import marscolony.Equipment.Equipment;
import marscolony.Equipment.EquipmentType;
import marscolony.MarsColony.MarsColony;
import marscolony.conditions.EnvironmentCondition;
import marscolony.resources.ResourceType;
import marscolony.personnel.*;
import marscolony.facilities.Facilities;
import marscolony.facilities.FacilityStatus;
import marscolony.facilities.Laboratory;

public class Exploration extends Mission {
    private String destination;
    private ExplorationRover rover;
    private MissionState state;

    public Exploration(String name, int missionDuration, String destination) {
        super(name, missionDuration);
        this.destination = destination;
        this.state = MissionState.PENDING;
    }

    public ExplorationRover getRover() {
        return rover;
    }

    public void setState(MissionState state) {
        this.state = state;
    }
   
   @Override
   public int getRequiredAmount(ResourceType type) { // add only battery ya Ta2touzaaaa
    throw new UnsupportedOperationException("TODO");
    //until writing body
   }

     @Override
    public List<Capability> getRequiredCapabilities() {
     throw new UnsupportedOperationException("TODO");
    //until writing body

    }
    
    @Override 
    public  String getEnvironmentProblem(EnvironmentCondition env) {
        if(env==EnvironmentCondition.DUST_STORM)
            return "Surface operations are not allowed during a dust storm";
        else
          return null;
    }

    public String getDestination() {
        return destination;
    }

    public boolean checkRequiredForMission() // He says req scientific skills??????
    {
        boolean robotStatus = false , equipmentStatus = false ,labStatus = false  ;
        for(RoboticUnit robot : MarsColony.getRobots())
        {
            if(robot instanceof ExplorationRover)
            {
                robotStatus = robot.canPerform(Capability.SURFACE_TRAVEL); 
                if(robotStatus)
                {
                    this.agents.add(robot); // find a way to release if failed or succeeded
                    break;
                }
            }
            
        }
        for(Equipment equipment : MarsColony.getEquipments() )
        {
            if( equipment.getType() == EquipmentType.EXPLORATION_EQUIPMENT && equipment.isOperationalStatus())
            {
                equipmentStatus = true;
                break;
            }
        }

        
        if(robotStatus && equipmentStatus){
            return true;
        }
        else
        {
            releaseAgents();
            return false;
        }
    }



       

    
      
}

