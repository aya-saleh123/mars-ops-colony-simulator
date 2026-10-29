package marscolony.MarsColony;

import marscolony.Equipment.Equipment;
import marscolony.conditions.EnvironmentCondition;
import marscolony.facilities.Facilities;
import marscolony.facilities.FacilityStatus;
import marscolony.resources.*;
import marscolony.personnel.*;
import marscolony.missions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MarsColony {
    // Related to stimulation part
    private static int colonyTime = 0;
    private static EnvironmentCondition currentCondition = EnvironmentCondition.NORMAL;
    private static List<Mission> missions = new ArrayList<>();
    private static Mission activeMission = null;

    private static List<Facilities> facilities = new ArrayList<>();
    private static List<Human> crewMembers = new ArrayList<>();
    private static List<RoboticUnit> robots = new ArrayList<>();
    private static List<Equipment> equipments = new ArrayList<>();

    public static void addEquipement(Equipment equipment) {
        equipments.add(equipment);
    }

    public static List<Equipment> getEquipments() {
        return equipments;
    }

    public static void addHuman(Human human) {
        crewMembers.add(human);
    }

    public static EnvironmentCondition getEnvCondition() {
        return currentCondition;
    }

    public static void addRoboticUnit(RoboticUnit roboticUnit) {
        robots.add(roboticUnit);
    }

    public static List<Human> getHumans() {
        return crewMembers;
    }

    public static List<RoboticUnit> getRobots() {
        return robots;
    }

    public static List<Facilities> getFacilities() {
        return facilities;
    }

    public static void addFacility(Facilities facility) {
        facilities.add(facility);
    }

    // we need to exclude destroyed and unavailable facilities
    public static int getTotalResourceCount(ResourceType resource) {
        int sum = 0;
        for (Facilities facility : facilities) {
            if (facility.getFacilityOperationalStatus() == FacilityStatus.DESTROYED
                    || facility.getFacilityOperationalStatus() == FacilityStatus.UNAVAILABLE)
                continue;
            sum += facility.getResourceCount(resource);
        }
        return sum;
    }

    public static boolean deductResourceFromFacilities(ResourceType resource, int amount) {
        if (amount > 0) {
            if (amount <= getTotalResourceCount(resource)) {
                findSuitableFacilitiesAndDeduct(resource, amount);
                return true;
            } else
                return false;

        } else
            throw new IllegalArgumentException("amount must be positive");

    }

    private static void findSuitableFacilitiesAndDeduct(ResourceType resource, int amount) {

        while (amount > 0) {
            int maxCount = 0;
            Facilities maxFacility = null;

            for (Facilities facility : facilities) {
                if (facility.getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL) {
                    int count = facility.getResourceCount(resource);

                    if (count > maxCount) {
                        maxFacility = facility;
                        maxCount = count;
                    }
                }
            }

            if (maxFacility == null) {
                return;
            }

            int deducted = Math.min(amount, maxCount);

            maxFacility.removeResource(resource, deducted);

            System.out.println(
                    deducted + " units of " + resource
                            + " deducted from " + maxFacility.getLocationName());

            amount -= deducted;
        }
    }

    public static void advanceTime(int time) {
        if (time <= 0)
            throw new IllegalArgumentException("Time must be positive");
        else {
            for (int i = 0; i < time; i++) {
                colonyTime++;
                if (activeMission != null) {
                    activeMission.advanceTime();
                    if (activeMission.getState() == MissionState.COMPLETED) {
                        activeMission = null;
                    }
                }

            }
        }
    }

    // handling on mission at a time
    public boolean startMission(Mission mission) // ask aya about emergency && remember to ask ABOUT BATTERY
    {
        missions.add(mission);
        if (activeMission != null) {
            mission.rejectMission(FailureReason.ANOTHER_ACTIVE_MISSION);
            return false;
        } else {
            if (mission instanceof Repair) {
                if (mission.getRequiredAmount(ResourceType.BATTERY_POWER) > getTotalResourceCount(
                        ResourceType.BATTERY_POWER)) {
                    mission.fail(FailureReason.INSUFFICIENT_BATTERIES);
                    return false;
                }
                if (mission.getRequiredAmount(ResourceType.SPARE_PARTS) > getTotalResourceCount(
                        ResourceType.SPARE_PARTS)) {
                    mission.fail(FailureReason.INSUFFICIENT_RESOURCES);
                    return false;
                }
                if (!((Repair) mission).checkRequiredForMission()) {
                    mission.fail(FailureReason.UNAVAILABLE_PERSONNEL);
                    return false;
                }

                mission.acceptMission();
                activeMission = mission;
                return true;

            } else if (mission instanceof Research) {
                if (mission.getRequiredAmount(ResourceType.BATTERY_POWER) > getTotalResourceCount(
                        ResourceType.BATTERY_POWER)) {
                    mission.fail(FailureReason.INSUFFICIENT_BATTERIES);
                    return false;
                }
                if (mission.getRequiredAmount(ResourceType.WATER) > getTotalResourceCount(ResourceType.WATER)) {
                    mission.fail(FailureReason.INSUFFICIENT_RESOURCES);
                    return false;
                }
                if (!((Research) mission).checkRequiredForMission()) {
                    mission.fail(FailureReason.UNAVAILABLE_PERSONNEL);
                    return false;
                }

                mission.acceptMission();
                activeMission = mission;
                return true;
            } else if (mission instanceof Rescue) {
                if (currentCondition == EnvironmentCondition.COMMUNICATION_DISRUPTION) {
                    mission.fail(FailureReason.ENVIRONMENTAL_CONDITIONS);
                    return false;
                }
                if (mission.getRequiredAmount(ResourceType.FUEL) > getTotalResourceCount(ResourceType.FUEL)) {
                    mission.fail(FailureReason.INSUFFICIENT_RESOURCES);
                    return false;
                }
                if (!((Rescue) mission).checkRequiredForMission()) {
                    mission.fail(FailureReason.UNAVAILABLE_PERSONNEL);
                    return false;
                }

                mission.acceptMission();
                activeMission = mission;
                return true;
            } else if (mission instanceof Exploration) {
                if (mission.getRequiredAmount(ResourceType.FUEL) > getTotalResourceCount(ResourceType.FUEL)) {
                    mission.fail(FailureReason.INSUFFICIENT_RESOURCES);
                    return false;
                }
                if(currentCondition == EnvironmentCondition.DUST_STORM )
                {
                    mission.fail(FailureReason.ENVIRONMENTAL_CONDITIONS);
                    return false;
                }
                if (!((Exploration) mission).checkRequiredForMission()) {
                    mission.fail(FailureReason.UNAVAILABLE_PERSONNEL);
                    return false;
                }

                mission.acceptMission();
                activeMission = mission;
                return true;
            }
        }
        return false;
    }

    public void colonyResourcesStatusReport() {
        System.out.println("Colony Resources Status Report");
        System.out.println("Oxygen : " + getTotalResourceCount(ResourceType.OXYGEN));
        System.out.println("Water : " + getTotalResourceCount(ResourceType.WATER));
        System.out.println("Food : " + getTotalResourceCount(ResourceType.FOOD));
        System.out.println("Fuel : " + getTotalResourceCount(ResourceType.FUEL));
        System.out.println("Battery : " + getTotalResourceCount(ResourceType.BATTERY_POWER));
        System.out.println("Medical Supplies : " + getTotalResourceCount(ResourceType.MEDICAL_SUPPLIES));
        System.out.println("Spare Parts : " + getTotalResourceCount(ResourceType.SPARE_PARTS));

    }

}