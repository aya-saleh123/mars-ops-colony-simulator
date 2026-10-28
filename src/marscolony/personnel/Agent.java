package marscolony.personnel;
import marscolony.missions.Capability;
import marscolony.missions.Mission;

public abstract class Agent {
    private String idNumber;
    private String location;
    private Capability skill;
    private boolean isAvailable;

    public Agent(String idNumber, Capability skill) {
        if (idNumber == null || idNumber.isEmpty()) {
            throw new IllegalArgumentException("ID number cannot be null or empty.");
        }
        if (skill == null) {
            throw new IllegalArgumentException("Skill cannot be null.");
        }
        this.idNumber = idNumber;
        this.location = "Base";
        this.skill = skill;
        this.isAvailable = true;
        this.skill = skill;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getLocation() {
        return location;
    }

    public Capability getSkill() {
        return skill;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setLocation(String location) {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty.");
        }
        this.location = location;
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

    public void setSkill(Capability skill) {
        if (skill == null) {
            throw new IllegalArgumentException("Skill cannot be null.");
        }
        this.skill = skill;
    }

    public boolean canPerform(Mission m){
        return m.getRequiredCapabilities().contains(getSkill());
    }

}
