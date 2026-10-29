package marscolony.conditions;

import java.util.ArrayList;
import java.util.List;

import marscolony.missions.FailureReason;
import marscolony.missions.Mission;
import marscolony.missions.MissionState;
import marscolony.personnel.RoboticUnit;

public class Conditions {

    private EnvironmentCondition condition;
    private List<Mission> missions = new ArrayList<>();
    private List<RoboticUnit> robots = new ArrayList<>();

    public Conditions(EnvironmentCondition condition,List<Mission> missions,List<RoboticUnit> robots) {

        if (condition == null) {
            throw new IllegalArgumentException("Environmental condition cannot be null.");
        }

        this.condition = condition;

        if (missions != null) {
            this.missions = missions;
        }

        if (robots != null) {
            this.robots = robots;
        }
    }

    public EnvironmentCondition getCondition() {
        return condition;
    }

    public void setConsequencesToEnvCond() {

        for (Mission mission : missions) {

            if (mission == null) {
                continue;
            }

            MissionState state = mission.getState();

            if (state != MissionState.ASSIGNED && state != MissionState.IN_PROGRESS) {
                continue;
            }

            String problem = mission.getEnvironmentProblem(condition);

            if (problem != null && !problem.isEmpty()) {
                System.out.println("Mission '" + mission.getName() + "' failed due to environmental conditions: " + problem);
                mission.fail(FailureReason.ENVIRONMENTAL_CONDITIONS);
            }
        }
    }
}