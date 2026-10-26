package marscolony.conditions;
import  marscolony.missions.Mission;

public class Conditions
{

private Mission mission;
private EnvironmentCondition condition;

public Conditions(EnvironmentCondition condition, Mission mission)
{
  this.condition=condition;
  this.mission=mission;
}
  

}