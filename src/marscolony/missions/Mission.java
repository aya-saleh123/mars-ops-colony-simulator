package marscolony.missions;

import java.util.List;
import java.util.ArrayList;
import marscolony.conditions.EnvironmentCondition;
import marscolony.resources.ResourceType;
import marscolony.personnel.Agent;
import marscolony.facilities.*;

public abstract class Mission {
    private FailureReason reasonOfFailure;
    private String name;
    private int elapsed;
    protected List<Agent> agents;
    private Facilities target;
    private int  missionDuration;
    private MissionState state;

    public Mission(String name, int missionDuration) {
        this.name = name;
        this.missionDuration = missionDuration;
        this.state = MissionState.PENDING;
        this.elapsed = 0;
        this.agents = new ArrayList<>();
        this.reasonOfFailure = FailureReason.NONE;
    }

    public abstract List<Capability> getRequiredCapabilities();

    public abstract int getRequiredAmount(ResourceType type);

    public abstract String getEnvironmentProblem(EnvironmentCondition env);

    protected void Completed() {
    }

    public void assignAgent(Agent a) {
        if (a.isAvailable() == false)
            throw new IllegalStateException("the agent " + a.getIdNumber() + " is not available");
        else
            a.setAvailable(false);
        agents.add(a);

    }

    public void releaseAgents() {
        for (Agent a : agents) {
            if (a.isAvailable() == false)
                a.setAvailable(true);
        }

    }

    public void acceptMission() {
        if (state != MissionState.PENDING)
            throw new IllegalStateException("you can only assign a pending mission ");
        else
            state = MissionState.ASSIGNED;
        System.out.println("Mission is accepted ");
    }

    public void rejectMission(FailureReason reason) {
        if (state != MissionState.PENDING)
            throw new IllegalStateException("you can only reject a pending mission ");
        else
            state = MissionState.FAILED;
        reasonOfFailure = reason;
        System.out.println("Mission is rejected due to " + reason);
    }

    public void fail(FailureReason reason) {
        if (isFinished())
            return;
        state = MissionState.FAILED;
        reasonOfFailure = reason;
        releaseAgents();
        System.out.println("Mission is failed due to " + reason);
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

        elapsed++;
        if (elapsed >= missionDuration) {
            this.state = MissionState.COMPLETED;
            releaseAgents();
            Completed();
            System.out.println("Mission Completed Successfully");
        }
    }

    public boolean isFinished() {
        if (state == MissionState.COMPLETED || state == MissionState.FAILED)
            return true;
        else
            return false;

    }

    public String getName() {
        return name;
    }

    public String checkAgents(List<Agent> agentsToCheck) {
        for (Agent a : agentsToCheck) { // Aya: El 7eta di kolaha 3ala fekra 7atedrab error 3alafekra, kalemini
            if (!a.canPerform(getRequiredCapabilities().get(0))) { // BE CAREFUL!!!!!! Assuming the first capability is
                                                                   // representative of the required capabilities
                return "Agent " + a.getIdNumber() + " does not have the required capability";
            }
            if (a.isAvailable() == false) {
                return "Agent " + a.getIdNumber() + " is not available";
            }
        }

        for (Capability c : getRequiredCapabilities()) {
            int req = 0;
            int actual = 0;
            for (Capability x : getRequiredCapabilities()) {
                if (x == c) {
                    req++;
                }
            }
            for (Agent a : agentsToCheck) {
                if (a.getSkill() == c) {
                    actual++;
                }
            }
            if (actual < req) {
                return "The agent does not have this capability" + c;
            }
        }
        return null;
    }

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

}
