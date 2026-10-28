package marscolony.personnel;
import marscolony.MarsColony.MarsColony;
import marscolony.missions.Capability;

public abstract class Human extends Agent {
    private String name;
    private boolean isHealthy;
    private FatigueLevel fatigueLevel;

    public Human(String name, String idNumber, Capability skill) {
        super(idNumber, skill);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
        this.isHealthy = true; 
        this.fatigueLevel = FatigueLevel.LOW; 
        MarsColony.addAgent(this);
    }

    public String getName() {
        return name;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public FatigueLevel getFatigueLevel() {
        return fatigueLevel;
    }

    public void setHealthy(boolean healthy) {
        if (!healthy && fatigueLevel == FatigueLevel.LOW) {
            throw new IllegalStateException("Cannot set health to unhealthy when fatigue level is low.");
        }
        else if (healthy && fatigueLevel == FatigueLevel.HIGH) {
            throw new IllegalStateException("Cannot set health to healthy when fatigue level is high.");
        }
        isHealthy = healthy;
    }

    public void setFatigueLevel(FatigueLevel fatigueLevel) {
        if (fatigueLevel == null) {
            throw new IllegalArgumentException("Fatigue level cannot be null.");
        }
        this.fatigueLevel = fatigueLevel;
    }

    public abstract void assignMission();

    @Override 
    public boolean canPerform(Capability requiredSkill) {
        return isAvailable() && getSkill() == requiredSkill && isHealthy() && getFatigueLevel() != FatigueLevel.HIGH;
    }

    @Override
    public void justFinished() {
        setAvailable(true);
        if (fatigueLevel == FatigueLevel.LOW) {
            setFatigueLevel(FatigueLevel.MEDIUM);
        } else if (fatigueLevel == FatigueLevel.MEDIUM) {
            setFatigueLevel(FatigueLevel.HIGH);
        }
    }

}
