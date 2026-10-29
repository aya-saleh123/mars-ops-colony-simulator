package marscolony.missions;
import java.util.List;
import java.util.ArrayList;
import marscolony.MarsColony.MarsColony;
import marscolony.resources.ResourceType;
import marscolony.personnel.*;
import marscolony.facilities.*;

public abstract class Mission {
    private FailureReason reasonOfFailure;
    private int elapsed;
    protected List<Agent> agents;
    private Facilities target;
    private int  missionDuration;
    private MissionState state;
    private String name;

    public Mission(String name, int missionDuration) {
        this.missionDuration = missionDuration;
        this.state = MissionState.PENDING;
        this.elapsed = 0;
        this.agents = new ArrayList<>();
        this.name=name;
        this.reasonOfFailure = FailureReason.NONE;
    }
    
    public abstract int getRequiredAmount(ResourceType type);

    protected void completed() {
        for(Agent agent : agents)
        {
            if(agent instanceof Human)
            {
                ((Human)agent).incFatigueLevel();
            }
            else if(agent instanceof RoboticUnit)
            {
                ((RoboticUnit)agent).decBatteryLevel();
            }
        }
        this.setState(MissionState.COMPLETED);
        this.releaseAgents();
        MarsColony.activeMission = null;


    }

    public void assignAgent(Agent a) {
        if(agents.contains(a)){
            System.out.println("Agent is already assigned to this mission");
            return ;}
        if (a.isAvailable() == false)
            throw new IllegalStateException("Agent " + a.getIdNumber() + " is not available");
        else
        {
            a.setAvailable(false);
            System.out.println("Agent " + a.getIdNumber() +" has been assigned to a mission");
            agents.add(a);
        }

    }

    public void releaseAgents() {
        for (Agent a : agents) {
            if (a.isAvailable() == false)
            {
                a.setAvailable(true);
            }
                
        }
        agents.clear();

    }

    public void acceptMission() {
        if (state != MissionState.PENDING)
            throw new IllegalStateException("you can only accept a pending mission ");
        else
            state = MissionState.ASSIGNED;
        System.out.println("Mission is accepted ");
    }

    public void fail(FailureReason reason) {
        if (isFinished())
            return;
        state = MissionState.FAILED;
        reasonOfFailure = reason;
        releaseAgents();
        System.out.println("Mission is failed due to " + reason);
        if (MarsColony.activeMission == this) {
            MarsColony.activeMission = null;
        }
    }

    public void setTarget(Facilities target) {
        this.target = target;
    }

    public void advanceTime() {
        if (state == MissionState.COMPLETED || state == MissionState.FAILED) {
            return;
        }

        if (this.state == MissionState.ASSIGNED) {
            state = MissionState.IN_PROGRESS;
        }

        if(this.state == MissionState.IN_PROGRESS)
        {
            elapsed++;
            if (elapsed >= missionDuration) {
                completed();
                System.out.println("Mission Completed Successfully");
            }
        }
    }

    public boolean isFinished() {
        if (state == MissionState.COMPLETED || state == MissionState.FAILED)
            return true;
        else
            return false;

    }

    public abstract boolean checkRequiredForMission();

    public int getElapsed() {
        return elapsed;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public Facilities getTarget() {
        return target;
    }

    public FailureReason getReasonOfFailure() {
        return reasonOfFailure;
    }

    public int getMissionDuration() {
        return missionDuration;
    }

    public MissionState getState() {
        return state;
    }

    public void setState(MissionState state) {
        this.state = state;
    }

    public String getName() {
        return name;
    } 
    public abstract FailureReason getMissionFailureReason();

}
