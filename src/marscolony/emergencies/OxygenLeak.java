package marscolony.emergencies;
import marscolony.facilities.*;
import marscolony.personnel.*;
import marscolony.resources.*;
import marscolony.conditions.EnvironmentCondition;
import marscolony.missions.*;
import marscolony.Equipment.Equipment;
import marscolony.MarsColony.MarsColony;

public class OxygenLeak extends Emergency {
    EmergencyStatus status = EmergencyStatus.IN_PROGRESS;
    Boolean availableEngineerFound = false;
    Boolean availableRepairRobotFound = false;
    private EnvironmentCondition currentCondition;
    Habitat oxygenLeakHab;

    public OxygenLeak(Habitat oxygenLeakHab)
    {
        super(oxygenLeakHab);
        this.oxygenLeakHab = oxygenLeakHab;
        this.currentCondition =  MarsColony.getEnvCondition();
        oxygenLeakHab.setOperationalStatus(FacilityStatus.UNAVAILABLE);
        int lost = oxygenLeakHab.removeUpTo(ResourceType.OXYGEN, 50);
        System.out.println("Oxygen leak consumed " + lost + " oxygen in " + oxygenLeakHab.getLocationName());
    }

    @Override 
    public void findResponders() {
         for (Agent a : MarsColony.getHumans()) {
            if (a.getClass() == Engineer.class) {
                if (((Engineer) a).canPerform(Capability.SCIENCE)) {
                    this.addResponder(a);
                    a.setAvailable(false);
                    availableEngineerFound = true;
                    break;
                }
            }
            if (a.getClass() == RepairRobot.class) {
                if (((RepairRobot) a).canPerform(Capability.REPAIR)) {
                    this.addResponder(a);
                    a.setAvailable(false);
                    availableRepairRobotFound = true;
                    break;
                }
            }
        }
        if (!availableEngineerFound && !availableRepairRobotFound) {
            status = EmergencyStatus.UNRESOLVED;
            System.out.println("No available Engineer or RepairRobot to respond to the oxygen leak emergency.");
            return;
        }
        respondToEmergency();
    
    }

    @Override 
    public void respondToEmergency() {
         for (Agent a : getResponders()) {
            if (a.isAvailable() && a.getClass() == Engineer.class) {
                availableEngineerFound = true;
                Engineer engineer = (Engineer) a;
                engineer.assignMission();
                //engineer.setAvailable(false); lalready set false in assignMission
                engineer.repairOxygenLeak();
            }
            if (a.isAvailable() && a.getClass() == RepairRobot.class) {
                availableRepairRobotFound = true;
                RepairRobot repairRobot = (RepairRobot) a;
                repairRobot.assignMission(currentCondition);
               // repairRobot.setAvailable(false); already set false in assignMission
                repairRobot.repairOxygenLeak();

            }
        }
        if (!availableEngineerFound && !availableRepairRobotFound) {
            status = EmergencyStatus.UNRESOLVED;
            getFacility().removeUpTo(ResourceType.OXYGEN, 50);
            System.out.println("No available Engineer or RepairRobot to respond to the oxygen leak emergency.");
        }
        else {
            status = EmergencyStatus.RESOLVED;
            getFacility().setOperationalStatus(FacilityStatus.OPERATIONAL);
            System.out.println("Oxygen leak emergency resolved.");
        }

    }

    }

