package marscolony.facilities;
import marscolony.resources.*;
public abstract class Facilities {
    private String location;
    private int capacity;
    private boolean operationalStatus;
    private Resource oxygen;
    private Resource water;
    private Resource food;
    private Resource fuel;
    private Resource battery;
    private Resource medicalSupplies;
    private Resource spareParts;
    //Equipement
    public Facilities(String location, int capacity, boolean operationalStatus, int oxygenCount , int waterCount,
            int  foodCount , int fuelCount, int batteryCount , int medicalSuppliesCount, int sparePartsCount ) {
        this.location = location;
        this.setCapacity(capacity);
        this.operationalStatus = operationalStatus;
        oxygen = new Resource(ResourceType.OXYGEN, oxygenCount);
        water = new Resource(ResourceType.WATER, waterCount);
        food = new Resource(ResourceType.FOOD, foodCount);
        fuel = new Resource(ResourceType.FUEL, fuelCount);
        battery = new Resource(ResourceType.BATTERY_POWER, batteryCount);
        medicalSupplies = new Resource(ResourceType.MEDICAL_SUPPLIES, medicalSuppliesCount);
        spareParts = new Resource(ResourceType.SPARE_PARTS, sparePartsCount);
        MarsColony.addFacility(this);

    }
    // we must decide what is capacity(people,resources,.....)
    private void setCapacity(int capacity)
    {
        if(capacity>0)
            this.capacity = capacity;
        else
            throw new IllegalArgumentException("Capacity must be positive");
    }
    public String getLocation() {
        return location;
    }
    public int getCapacity() {
        return capacity;
    }
    public boolean isOperationalStatus() {
        return operationalStatus;
    }
    public int getOxygenCount() {
        return oxygen.getQuantity();
    }
    public int getWaterCount() {
        return water.getQuantity();
    }
    public int getFood() {
        return food.getQuantity();
    }
    public int getFuel() {
        return fuel.getQuantity();
    }
    public int getBattery() {
        return battery.getQuantity();
    }
    public int getMedicalSupplies() {
        return medicalSupplies.getQuantity();
    }
    public int getSpareParts() {
        return spareParts.getQuantity();
    }

    

    

}
