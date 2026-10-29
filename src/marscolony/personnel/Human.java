package marscolony.personnel;
import marscolony.MarsColony.MarsColony;
import marscolony.missions.Capability;

public abstract class Human extends Agent {
    private String name;
    private HealthStatus healthStatus;
    private FatigueLevel fatigueLevel;

    public Human(String name, String idNumber, Capability skill) {
        super(idNumber, skill);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
        this.healthStatus = HealthStatus.HEALTHY;
        this.fatigueLevel = FatigueLevel.LOW; 
        MarsColony.addHuman(this);
    }

    public String getName() {
        return name;
    }

    public HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public FatigueLevel getFatigueLevel() {
        return fatigueLevel;
    }

    public void setHealthStatus(HealthStatus h) { 
        if (h == null) 
            throw new IllegalArgumentException("Health cannot be null."); 
        healthStatus = h; 
    }

    public void improveHealth() {
        if (healthStatus == HealthStatus.CRITICAL) 
            healthStatus = HealthStatus.INJURED;
        else if (healthStatus == HealthStatus.INJURED) 
            healthStatus = HealthStatus.HEALTHY;
    }

    public void worsenHealth() {
        if (healthStatus == HealthStatus.HEALTHY) 
            healthStatus = HealthStatus.INJURED;
        else if (healthStatus == HealthStatus.INJURED) 
            healthStatus = HealthStatus.CRITICAL;
    }

    public void setFatigueLevel(FatigueLevel fatigueLevel) {
        if (fatigueLevel == null) {
            throw new IllegalArgumentException("Fatigue level cannot be null.");
        }
        this.fatigueLevel = fatigueLevel;
    }

    public void incFatigueLevel() {
        if (fatigueLevel == FatigueLevel.LOW) {
            fatigueLevel = FatigueLevel.MEDIUM;
        } else if (fatigueLevel == FatigueLevel.MEDIUM) {
            fatigueLevel = FatigueLevel.HIGH;
        }
    }

    public void rest() {
        if (fatigueLevel == FatigueLevel.LOW) {
            System.out.println("Human " + getName() + " is already at low fatigue level. No need to rest.");
            return;
        }
        System.out.println(getName() + " is resting to recover from fatigue.");
        decFatigueLevel();
        decFatigueLevel();
        System.out.println(getName() + "has had a nice break and is at low fatigue level.");
    }
    
    public void decFatigueLevel() {
        if (fatigueLevel == FatigueLevel.HIGH) {
            fatigueLevel = FatigueLevel.MEDIUM;
        } else if (fatigueLevel == FatigueLevel.MEDIUM) {
            fatigueLevel = FatigueLevel.LOW;
        }
    }

    public void assignMission() {
        if (isAvailable() && getFatigueLevel() != FatigueLevel.HIGH) {
            System.out.println("Assigning mission to: " + getName());
            this.setAvailable(false);
        } else {
            System.out.println(getName() + " is currently unavailable for missions.");
        }
    }

    @Override 
    public boolean canPerform(Capability requiredSkill) {
        return isAvailable() && getSkill() == requiredSkill && getHealthStatus() == HealthStatus.HEALTHY && getFatigueLevel() != FatigueLevel.HIGH;
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
