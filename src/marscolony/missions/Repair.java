package marscolony.missions;
import marscolony.resources.ResourceType;
import marscolony.MarsColony.MarsColony;
import marscolony.conditions.EnvironmentCondition;
import marscolony.facilities.Facilities;
import marscolony.personnel.*;
import marscolony.Equipment.*;

public class Repair extends Mission {
    private  Facilities facilityToRepair;
    private Equipment equipmentToRepair;

    public Repair(String name,int missionDuration ,Facilities facilityToRepair) {
        super(name, missionDuration);
        this.facilityToRepair = facilityToRepair;
        setTarget(facilityToRepair);
    }
    public Repair(String name,int missionDuration,Equipment equipmentToRepair) {
        super(name, missionDuration);
        this.equipmentToRepair = equipmentToRepair;
        setTarget(null);
    }

    
    public int getRequiredAmount(ResourceType type) {
         if(type==ResourceType.SPARE_PARTS)
            return 2;
        if(type==ResourceType.BATTERY_POWER)
            return 5;
         else
            return 0;

    }

    @Override
    protected void completed() {
        if(facilityToRepair != null) //repair facility
       {
         facilityToRepair.repairFacility();
        for(Agent a:getAgents()){
         if(a instanceof Human )
                {
                Human repairAgent=(Human) a;
                System.out.print("Human Agent " + repairAgent.getName()+ " has repaired " + getTarget().getLocationName());
                }
         if(a instanceof RoboticUnit)
                {
                RoboticUnit repairAgent=(RoboticUnit) a;
                System.out.println("with the help of repair robot" + repairAgent.getIdNumber());
                }
        }
       }
       else 
       {
        equipmentToRepair.fixEquipment();
        for(Agent a:getAgents()){
         if(a instanceof Human )
                {
                Human repairAgent=(Human) a;
                System.out.print("Human Agent " + repairAgent.getName()+ " has repaired " + equipmentToRepair.getType());
                }
         if(a instanceof RoboticUnit)
                {
                RoboticUnit repairAgent=(RoboticUnit) a;
                System.out.println("with the help of repair robot" + repairAgent.getIdNumber());
                }
        }
       }
         super.completed();
     }
   /* @Override 
    public boolean checkRequiredForMission()
    {
        boolean humanStatus = false , robotStatus = false ;
        for(Human human : MarsColony.getHumans())
        {
            humanStatus = human.canPerform(Capability.REPAIR); 
            if(humanStatus)
            {
                this.assignAgent(human);
                break;
            }
        }
        for(RoboticUnit robot : MarsColony.getRobots() )
        {
            
            robotStatus = robot.canPerform(Capability.REPAIR);
            if(robotStatus)
            {
                this.assignAgent(robot);
                break;
            }
        }
        if(humanStatus && robotStatus){
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
    boolean availableHumanExists = false;
    boolean suitableHumanExists = false;
    boolean availableRobotExists = false;
    boolean suitableRobotExists = false;
    boolean lowBatteryRepairRobotExists = false;
    for (Human human : MarsColony.getHumans()) {
        if (human.isAvailable()) {
            availableHumanExists = true;

            if (human.canPerform(Capability.REPAIR)) {
                suitableHumanExists = true;
            }
        }
    }
    for (RoboticUnit robot : MarsColony.getRobots()) {
        if (robot.isAvailable()) {
            availableRobotExists = true;

            if (robot.canPerform(Capability.REPAIR)) {
                suitableRobotExists = true;
            }

            if (robot.getSkill() == Capability.REPAIR && robot.isOperational()&& robot.getBatteryLevel() == BatteryLevel.LOW) {
                lowBatteryRepairRobotExists = true;
            }
        }
    }

    if (!availableHumanExists || !availableRobotExists) {
        return FailureReason.UNAVAILABLE_PERSONNEL;
    }

    if (!suitableHumanExists) {
        return FailureReason.UNSUITABLE_AGENT;
    }

    if (!suitableRobotExists) {
        if (lowBatteryRepairRobotExists) {
            return FailureReason.INSUFFICIENT_BATTERIES;
        }
        return FailureReason.UNSUITABLE_AGENT;
    }

    return FailureReason.NONE;
}

@Override
public boolean checkRequiredForMission() {
    if (getMissionFailureReason() != FailureReason.NONE) {
        return false;
    }
    for (Human human : MarsColony.getHumans()) {
        if (human.canPerform(Capability.REPAIR)) {
            assignAgent(human);
            break;
        }
    }
    for (RoboticUnit robot : MarsColony.getRobots()) {
        if (robot.canPerform(Capability.REPAIR)) {
            assignAgent(robot);
            break;
        }
    }
    setTarget(facilityToRepair);
    return true;
}


}