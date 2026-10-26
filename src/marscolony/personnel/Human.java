package marscolony.personnel;

public abstract class Human {
    private String name;
    private String idNumber;
    private boolean isHealthy;
    private String location;
    private String[] skills;
    private int fatigueLevel;
    private boolean isAvailable;

    public Human(String name, String idNumber) {
        this.name = name;
        this.idNumber = idNumber;
        this.isHealthy = true; 
        this.location = "Base"; 
        this.skills = new String[0]; 
        this.fatigueLevel = 0; 
        this.isAvailable = true;
    }

}
