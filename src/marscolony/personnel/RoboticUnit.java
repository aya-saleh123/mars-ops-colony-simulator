package marscolony.personnel;

public abstract class RoboticUnit {
    private String idNumber;
    private int batteryLevel;
    private String location;
    private boolean isAvailable;
    private boolean isOperational;

    public RoboticUnit(String idNumber) {
        this.idNumber = idNumber;
        this.batteryLevel = 100; 
        this.location = "Base"; 
        this.isAvailable = true; 
        this.isOperational = true; 
    }
}
