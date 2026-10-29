package marscolony.missions;
import marscolony.Equipment.Equipment;
import marscolony.Equipment.EquipmentType;
import marscolony.MarsColony.MarsColony;
import marscolony.MarsColony.MarsSurface;
import marscolony.conditions.EnvironmentCondition;
import marscolony.resources.ResourceType;
import marscolony.personnel.*;

public class Exploration extends Mission {
    private MarsSurface destination;

    public Exploration(String name ,int missionDuration, MarsSurface destination) {
        super(name, missionDuration);
        this.destination = destination;
    }

    public ExplorationRover getRover() { 
       for(Agent a:getAgents()){
        if(a instanceof ExplorationRover)
            return (ExplorationRover) a;
       }
       return null;
    }

   public int getRequiredAmount(ResourceType type) {
    if(type==ResourceType.FUEL)
        return 10;
    else 
        return 0;
   }

    public MarsSurface getDestination() {
        return destination;
    }

    /*@Override 
    public boolean checkRequiredForMission()
    {
        boolean robotStatus = false , equipmentStatus = false ,humanStatus = false;

        for (Human human : MarsColony.getHumans()) {
                humanStatus = human.canPerform(Capability.SCIENCE); 
                if(humanStatus)
                {
                    this.assignAgent(human);
                    break;
                }
            }

        for(RoboticUnit robot : MarsColony.getRobots())
        {
                robotStatus = robot.canPerform(Capability.SURFACE_TRAVEL); 
                if(robotStatus)
                {
                    this.assignAgent(robot);
                    break;
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
        if(robotStatus && equipmentStatus && humanStatus){
            return true;
        }
        else
        {
            releaseAgents();
            return false;
        }
    }*/
   @Override 
   public FailureReason getMissionFailureReason() {
    if (MarsColony.getEnvCondition() == EnvironmentCondition.DUST_STORM) {
        return FailureReason.ENVIRONMENTAL_CONDITIONS;
    }

    boolean availableHumanExists = false;
    boolean suitableHumanExists = false;
    boolean availableRobotExists = false;
    boolean suitableRobotExists = false;
    boolean lowBatteryRoverExists = false;
    boolean equipmentStatus = false;
    for (Human human : MarsColony.getHumans()) {
        if (human.isAvailable()) {
            availableHumanExists = true;

            if (human.canPerform(Capability.SCIENCE)) {
                suitableHumanExists = true;
            }
        }
    }

    if (!availableHumanExists) {
        return FailureReason.UNAVAILABLE_PERSONNEL;
    }

    if (!suitableHumanExists) {
        return FailureReason.UNSUITABLE_AGENT;
    }

    for (RoboticUnit robot : MarsColony.getRobots()) {
        if (robot.isAvailable()) {
            availableRobotExists = true;

            if (robot.canPerform(Capability.SURFACE_TRAVEL)) {
                suitableRobotExists = true;
            }

            if (robot.getSkill() == Capability.SURFACE_TRAVEL&& robot.isOperational()&& robot.getBatteryLevel() == BatteryLevel.LOW) {
                lowBatteryRoverExists = true;
            }
        }
    }

    if (!availableRobotExists) {
        return FailureReason.UNAVAILABLE_PERSONNEL;
    }

    if (!suitableRobotExists) {
        if (lowBatteryRoverExists) {
            return FailureReason.INSUFFICIENT_BATTERIES;
        }
        return FailureReason.UNSUITABLE_AGENT;
    }
    for (Equipment equipment : MarsColony.getEquipments()) {
        if (equipment.getType() == EquipmentType.EXPLORATION_EQUIPMENT && equipment.isOperationalStatus()) {
            equipmentStatus = true;
            break;
        }
    }
    if (!equipmentStatus) {
        return FailureReason.EQUIPMENT_FAILURE;
    }
    return FailureReason.NONE;
}

@Override
public boolean checkRequiredForMission() {
    if (getMissionFailureReason() != FailureReason.NONE) {
        return false;
    }

    for (Human human : MarsColony.getHumans()) {
        if (human.canPerform(Capability.SCIENCE)) {
            assignAgent(human);
            break;
        }
    }
    for (RoboticUnit robot : MarsColony.getRobots()) {
        if (robot.canPerform(Capability.SURFACE_TRAVEL)) {
            assignAgent(robot);
            break;
        }
    }

    return true;
}
    @Override
    protected void completed() {
      
        for(Agent a:getAgents()){
         if(a instanceof Human )
                {
                Human explorer=(Human) a;
                System.out.print("Human Agent " + explorer.getName()+ " has explored " + destination);
                }
         if(a instanceof RoboticUnit)
                {
                RoboticUnit explorerAgent=(RoboticUnit) a;
                System.out.println("using exploration robot" + explorerAgent.getIdNumber());
                }
       }
         super.completed();
     }


    @Override 
    public void acceptMission(){
        super.acceptMission();
        for(Agent a: getAgents())
            a.setLocation("Martian Surface");
    }

      
}

