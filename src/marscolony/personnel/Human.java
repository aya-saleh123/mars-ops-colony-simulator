package marscolony.personnel;

import marscolony.missions.Capability;

public abstract class Human {
    private String name;
    private String idNumber;
    private boolean isHealthy;
    private String location;
    private Capability skill;
    private FatigueLevel fatigueLevel;
    private boolean isAvailable;

    public Human(String name, String idNumber) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        if (idNumber == null || idNumber.isEmpty()) {
            throw new IllegalArgumentException("ID number cannot be null or empty.");
        }
        this.name = name;
        this.idNumber = idNumber;
        this.isHealthy = true; 
        this.location = "Base"; 
        this.skill = null;
        this.fatigueLevel = FatigueLevel.LOW; 
        this.isAvailable = true;
    }

    public String getName() {
        return name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public String getLocation() {
        return location;
    }

    public Capability getSkill() {
        return skill;
    }

    public FatigueLevel getFatigueLevel() {
        return fatigueLevel;
    }

    public boolean isAvailable() {
        return isAvailable;
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

    public void setLocation(String location) {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty.");
        }
        this.location = location;
    }

    public void setSkill(Capability skill) {
        if (skill == null) {
            throw new IllegalArgumentException("Skill cannot be null.");
        }
        this.skill = skill;
    }

    public void setFatigueLevel(FatigueLevel fatigueLevel) {
        if (fatigueLevel == null) {
            throw new IllegalArgumentException("Fatigue level cannot be null.");
        }
        this.fatigueLevel = fatigueLevel;
    }

    public void setAvailable(boolean available) {
        if (isAvailable && available) {
            throw new IllegalStateException("Already marked as available.");
        }
        else if (!isAvailable && !available) {
            throw new IllegalStateException("Already marked as unavailable.");
        }
        isAvailable = available;
    }

    public abstract void assignMission();

}
