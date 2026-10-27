package marscolony.missions;
import java.util.List;

import marscolony.conditions.EnvironmentCondition;
import marscolony.resources.ResourceType;

public abstract class Mission {
    private String failureReason; //make it enum ya toka
    private String name;
    private int elapsed;
   // private List<agent> agents;
   // private Facility target;
   private int  missionDuration;
   private MissionState state;

   public Mission(String name, int missionDuration) {
    this.name = name;
    this.missionDuration = missionDuration;
    this.state=MissionState.PENDING;
    this.elapsed=0;
   // this.agents=new ArrayList<>();
   }
   
    public abstract List<Capability> getRequiredCapabilities();
    public abstract int getRequiredAmount(ResourceType type);

   public  abstract String getEnvironmentProblem(EnvironmentCondition env);
    protected void Completed() { }

   // public void setTarget(Facility target) {}

   // public void assignAgent(Agent a) {  }
    //public void releaseAgents() { for (Agent a : agents) {}

    //public void acceptMission() {  }
   // public void rejectMission(String reason) {  }

   // public void fail(String reason) {}
      
    

    public void advanceTime() {
        if(this.state == MissionState.IN_PROGRESS)
        {
            elapsed++;
            if(elapsed>=missionDuration)
            {
                this.state = MissionState.COMPLETED;
                System.out.println("Mission Completed Successfully!!");
            }
        }
        else
            return ;
    }

   // public boolean isFinished() {}
   // public String getName() { }
   // public int getDuration() {}
   // public int getElapsed() { }
    public MissionState getMissionState() {
        return state;
    }
   // public String getFailureReason() {  }
   // public List<Agent> getAgents() {  }
   // public Facility getTarget() {  }
 

}
