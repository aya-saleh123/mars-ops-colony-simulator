package marscolony.missions;
import marscolony.resources.ResourceType;
import marscolony.MarsColony.MarsColony;
import marscolony.conditions.EnvironmentCondition;
import marscolony.personnel.*;
import marscolony.facilities.Facilities;
import marscolony.facilities.FacilityStatus;
import marscolony.facilities.Habitat;

public class Rescue extends Mission{
    private Agent inCrisisAgent;
    private String rescueLocation;
    private String safeLocation;

    public Rescue(String name,int missionDuration , Agent inCrisisAgent) {
        super(name, missionDuration);
        this.inCrisisAgent = inCrisisAgent;
        this.rescueLocation = inCrisisAgent.getLocation();
        this.setSafeLocation();
    }
     public String getSafeLocation()
    {
        return safeLocation;
    }
    public String getRescueLocation()
    {
        return rescueLocation;
    }
    
    @Override
    public int getRequiredAmount(ResourceType type) {
         if(type==ResourceType.FUEL)
            return 10;
        else return 0;
    }

      @Override
    protected void completed() {
        super.completed();
        inCrisisAgent.setLocation(safeLocation);
        System.out.println(inCrisisAgent.getIdNumber() +" has been rescued and brought to "+ safeLocation);
     }


    /* @Override 
     public boolean checkRequiredForMission()
    {
        boolean humanStatus = false , robotStatus = false ;
        for(Human human : MarsColony.getHumans())
        {
                humanStatus = human.canPerform(Capability.FIREFIGHTING); 
                if(humanStatus)
                {
                    this.assignAgent(human);
                    break;
                }
        }
        for(RoboticUnit robot : MarsColony.getRobots() )
        {
                robotStatus = robot.canPerform(Capability.AERIAL);
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

    private void setSafeLocation()
    {
        int flag =0;
        for(Facilities facility : MarsColony.getFacilities())
            {
                if(facility instanceof Habitat)
                {
                    if(facility.getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL)
                    {
                        this.safeLocation = facility.getLocationName();
                        flag = 1;
                        break;
                    }
                }
            }

            if(flag == 0)
            {
                this.safeLocation = "Safe Martian Surface";
            }
    }
    @Override 
public FailureReason getMissionFailureReason() {
    if (MarsColony.getEnvCondition() == EnvironmentCondition.COMMUNICATION_DISRUPTION) {
        return FailureReason.ENVIRONMENTAL_CONDITIONS;
    }

    boolean availableHumanExists = false;
    boolean suitableHumanExists = false;
    boolean availableRobotExists = false;
    boolean suitableRobotExists = false;
    boolean lowBatteryAerialRobotExists = false;

    for (Human human : MarsColony.getHumans()) {
        if (human.isAvailable()) {
            availableHumanExists = true;

            if (human.canPerform(Capability.FIREFIGHTING)) {
                suitableHumanExists = true;
            }
        }
    }
    for (RoboticUnit robot : MarsColony.getRobots()) {
        if (robot.isAvailable()) {
            availableRobotExists = true;

            if (robot.canPerform(Capability.AERIAL)) {
                suitableRobotExists = true;
            }

            if (robot.getSkill() == Capability.AERIAL && robot.isOperational() && robot.getBatteryLevel() == BatteryLevel.LOW) {
                lowBatteryAerialRobotExists = true;
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
        if (lowBatteryAerialRobotExists) {
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
        if (human.canPerform(Capability.FIREFIGHTING)) {
            assignAgent(human);
            break;
        }
    }
    for (RoboticUnit robot : MarsColony.getRobots()) {
        if (robot.canPerform(Capability.AERIAL)) {
            assignAgent(robot);
            break;
        }
    }

    return true;
}
   


}
    


