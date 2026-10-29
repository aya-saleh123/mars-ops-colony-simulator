package marscolony.emergencies;

import marscolony.MarsColony.MarsColony;
import marscolony.facilities.FacilityStatus;
import marscolony.facilities.Habitat;
import marscolony.missions.Capability;
import marscolony.missions.FailureReason;
import marscolony.personnel.Agent;
import marscolony.personnel.Engineer;
import marscolony.personnel.RepairRobot;
import marscolony.resources.ResourceType;

public class OxygenLeak extends Emergency {
    private static final int OXYGEN_LOSS = 50;
    private static final int SPARE_PARTS_COST = 2;

    private final Habitat oxygenLeakHab;
    private Engineer engineer;
    private RepairRobot repairRobot;

    public OxygenLeak(Habitat oxygenLeakHab) {
        super(oxygenLeakHab);
        this.oxygenLeakHab = oxygenLeakHab;
        oxygenLeakHab.setOperationalStatus(FacilityStatus.UNAVAILABLE);
        int lost = oxygenLeakHab.removeUpTo(ResourceType.OXYGEN, OXYGEN_LOSS);
        System.out.println("Oxygen leak detected in "+ oxygenLeakHab.getLocationName()+ ". Oxygen lost: " + lost + ".");
    }

    @Override
    public void findResponders() {
        engineer = null;
        repairRobot = null;

        for (Agent agent : MarsColony.getHumans()) {
            if (agent.canPerform(Capability.REPAIR)) {
                engineer = (Engineer) agent;
                break;
            }
        }

        for (Agent agent : MarsColony.getRobots()) {
            if (agent.canPerform(Capability.REPAIR)) {
                repairRobot = (RepairRobot) agent;
                break;
            }
        }

        if (engineer == null || repairRobot == null) {
            setStatus(EmergencyStatus.UNRESOLVED);
            setReason(FailureReason.UNAVAILABLE_PERSONNEL);
            chargeUnresolvedLeakLoss();
            System.out.println("Oxygen leak unresolved: " + getReason());
            return;
        }

        if (!MarsColony.deductResourceFromFacilities(ResourceType.SPARE_PARTS, SPARE_PARTS_COST)) {
            setStatus(EmergencyStatus.UNRESOLVED);
            setReason(FailureReason.INSUFFICIENT_RESOURCES);
            chargeUnresolvedLeakLoss();
            System.out.println("Oxygen leak unresolved: " + getReason());
            return;
        }

        dispatchResponder(engineer);
        dispatchResponder(repairRobot);
        respondToEmergency();
    }

    @Override
    public void respondToEmergency() {
        if (engineer == null || repairRobot == null) {
            setStatus(EmergencyStatus.UNRESOLVED);
            if (getReason()==null) {
                setReason(FailureReason.UNAVAILABLE_PERSONNEL);
            }
            return;
        }
        else {
            engineer.repairOxygenLeak();
            repairRobot.repairOxygenLeak();
            oxygenLeakHab.setOperationalStatus(FacilityStatus.OPERATIONAL);
            setStatus(EmergencyStatus.RESOLVED);
            setReason(FailureReason.NONE);
            System.out.println("Oxygen leak resolved at "+ oxygenLeakHab.getLocationName() + ".");
            releaseResponders();
        }
    }

    private void chargeUnresolvedLeakLoss() {
        oxygenLeakHab.removeUpTo(ResourceType.OXYGEN, OXYGEN_LOSS);
        oxygenLeakHab.setOperationalStatus(FacilityStatus.UNAVAILABLE);
    }
}