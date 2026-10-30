package marscolony.conditions;
import java.util.List;
import marscolony.MarsColony.MarsColony;
import marscolony.missions.Exploration;
import marscolony.missions.FailureReason;
import marscolony.missions.Mission;
import marscolony.missions.MissionState;
import marscolony.missions.Repair;
import marscolony.missions.Rescue;
import marscolony.missions.Research;
import marscolony.personnel.RoboticUnit;

public class Conditions {

  private  static EnvironmentCondition condition;

  public Conditions(EnvironmentCondition condition,List<Mission> missions,List<RoboticUnit> robots) {

    if (condition == null) {
      throw new IllegalArgumentException("Environmental condition cannot be null.");
    }

    this.condition = condition;
  }

  public EnvironmentCondition getCondition() {
    return condition;
  }

  public void worsenEnvironmentCondition(EnvironmentCondition newCondition) {

    if (newCondition == null) {
      throw new IllegalArgumentException("Environmental condition cannot be null.");
    }

    condition = newCondition;
    MarsColony.currentCondition = condition;

    System.out.println("Environment changed to " + newCondition);

    Mission activeMission = MarsColony.activeMission;

    if (activeMission == null || activeMission.isFinished()) {
      MarsColony.activeMission = null;
      return;
    }

    if (isAffectedByCondition(activeMission, newCondition)) {
      activeMission.fail(FailureReason.ENVIRONMENTAL_CONDITIONS);

      System.out.println("Mission " + activeMission.getName()+ " failed because of " + newCondition);
    }
  }

  private boolean isAffectedByCondition(Mission mission,EnvironmentCondition envCondition) {

    switch (envCondition) {

      case NORMAL:
        return false;

      case DUST_STORM:
        return mission instanceof Exploration || mission instanceof Rescue || mission instanceof Repair;

      case COMMUNICATION_DISRUPTION:
        return mission instanceof Research || mission instanceof Rescue;

      default:
        return false;
    }
  }

  public static boolean isMissionAllowed(Mission mission) {

    if (mission == null) {
      throw new IllegalArgumentException("Mission cannot be null.");
    }

    condition = MarsColony.getEnvCondition();

    if (!isAffectedByCondition(mission, condition)) {
      return true;
    }

    System.out.println("Mission " + mission.getName() + " cannot start because of " + condition);
    if (mission.getState() == MissionState.IN_PROGRESS || mission.getState() == MissionState.PENDING || mission.getState() == MissionState.ASSIGNED) {
      mission.fail(FailureReason.ENVIRONMENTAL_CONDITIONS);
      // 34an el reject mission elli konna 3amlenha nafs el implementation beta3 fail fa fokek
    }
    return false;
  }

  public void resetEnvironmentCondition() {
    condition = EnvironmentCondition.NORMAL;
    MarsColony.currentCondition = EnvironmentCondition.NORMAL;
    System.out.println("Environmental condition reset to NORMAL.");
  }
}