package marscolony.conditions;
import java.util.ArrayList;
import java.util.List;
import marscolony.missions.*;
import marscolony.personnel.*;

public class Conditions {

  private EnvironmentCondition condition;
  private List<Mission> missions = new ArrayList<>();
  private List<RoboticUnit> robots = new ArrayList<>();

  public Conditions(EnvironmentCondition condition, List<Mission> missions, List<RoboticUnit> robots) {
    this.condition = condition;
    this.missions = missions;
    this.robots = robots;

  }

  public EnvironmentCondition getCondition() {
    return condition;
  }

  public void setConsequencesToEnvCond() {
    for (Mission m : missions) {
      if (condition == EnvironmentCondition.DUST_STORM) {
        m.getEnvironmentProblem(EnvironmentCondition.DUST_STORM);

      }
      if (condition == EnvironmentCondition.COMMUNICATION_DISRUPTION) {
        m.getEnvironmentProblem(EnvironmentCondition.COMMUNICATION_DISRUPTION);

      }

    }
    for (RoboticUnit r : robots) {
      if (condition == EnvironmentCondition.DUST_STORM) {
        r.assignMission(EnvironmentCondition.DUST_STORM);
      }
      if (condition == EnvironmentCondition.COMMUNICATION_DISRUPTION) {
        r.assignMission(EnvironmentCondition.COMMUNICATION_DISRUPTION);
      }
    }

  }

}