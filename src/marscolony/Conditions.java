package marscolony;


public class Conditions
{

public enum EnvironmentalCondition  {
    NORMAL,
    DUST_STORMS,
    COMMUNICATION_DISRUPTION
}

//Private Missions mission;
private EnvironmentalCondition condition;

public Conditions(EnvironmentalCondition condition)
{
  this.condition=condition;
  //this.mission=mission;

}
   



}