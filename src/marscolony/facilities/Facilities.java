package marscolony.facilities;
import marscolony.Equipment.Equipment;
import marscolony.Equipment.EquipmentType;
import marscolony.MarsColony.MarsColony;
import marscolony.resources.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Facilities {

    private String locationName;
    private int capacity;
    private FacilityStatus operationalStatus;
    // list of all resources
    private List<Resource> resources;
    //list of equipement
    private List<Equipment> equipments;


    public Facilities(
            String locationName,
            int capacity,
            int oxygenCount,
            int waterCount,
            int foodCount,
            int fuelCount,
            int batteryCount,
            int medicalSuppliesCount,
            int sparePartsCount) {

        this.locationName = locationName;
        setCapacity(capacity);
        this.operationalStatus = FacilityStatus.OPERATIONAL;
        setResourcescount(oxygenCount,waterCount,foodCount,fuelCount,batteryCount,medicalSuppliesCount,sparePartsCount);
        resources = new ArrayList<>();
        
        resources.add(new Resource(ResourceType.OXYGEN, oxygenCount));
        resources.add(new Resource(ResourceType.WATER, waterCount));
        resources.add(new Resource(ResourceType.FOOD, foodCount));
        resources.add(new Resource(ResourceType.FUEL, fuelCount));
        resources.add(new Resource(ResourceType.BATTERY_POWER, batteryCount));
        resources.add(new Resource(ResourceType.MEDICAL_SUPPLIES, medicalSuppliesCount));
        resources.add(new Resource(ResourceType.SPARE_PARTS, sparePartsCount));
        equipments = new ArrayList<>();
        equipments.add(new Equipment(EquipmentType.SCIENTIFIC_EQUIPMENT));
        equipments.add(new Equipment(EquipmentType.EXPLORATION_EQUIPMENT));


        MarsColony.addFacility(this);
    }

    private void setResourcescount(int oxygenCount,int waterCount,int foodCount,int fuelCount,int batteryCount,int medicalSuppliesCount,int sparePartsCount)
    {
        if(oxygenCount<0 || waterCount<0 || foodCount<0 || fuelCount<0 || batteryCount<0 || medicalSuppliesCount<0 || sparePartsCount<0)
            throw new IllegalArgumentException("Resources count can't be negative");
        if((oxygenCount+waterCount+foodCount+fuelCount+batteryCount+medicalSuppliesCount+sparePartsCount)>capacity)
            throw new IllegalArgumentException("Total of resources can't exceed the set capacity");
    }

    private void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            throw new IllegalArgumentException(
                "Capacity must be a positive number"
            );
        }
    }

    public String getLocationName() {
        return locationName;
    }

    public int getCapacity() {
        return capacity;
    }

    public FacilityStatus getFacilityOperationalStatus() {
        return operationalStatus;
    }

    public int getResourceCount(ResourceType type) {
        for (Resource resource : resources) {
            if (resource.getType() == type) {
                return resource.getQuantity();
            }
        }

        throw new IllegalArgumentException("Resource type not found: " + type);
    }

    public void removeResource(ResourceType type, int amount) {
        for (Resource resource : resources) {
            if (resource.getType() == type) {
                resource.removeQuantity(amount);
                return ;
            }
        }
        
    }

    public void addResource(ResourceType type, int amount) {
        if((calculateCurrentTotal()+amount)> capacity )
            throw new IllegalArgumentException("Can't add as it exceeds this facility's max capacity");
        else
            for (Resource resource : resources) {
                if (resource.getType() == type) {
                    resource.addQuantity(amount);
                    return ;
                }
            }

    }

    public int calculateCurrentTotal()
    {
        int sum = 0;
        for(Resource resource: resources)
        {
            sum+= resource.getQuantity();
        }
        return sum;
    }

    public void setOperationalStatus(FacilityStatus operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public List<Equipment> getEquipments() {
    return equipments;
}

public  void destroyFacility()
{
    if (operationalStatus == FacilityStatus.DESTROYED)
        System.out.println(getLocationName() +" is already destroyed");
    else
        operationalStatus = FacilityStatus.DESTROYED;
}

public void repairFacility()
{
    if (operationalStatus == FacilityStatus.OPERATIONAL)
        System.out.println(getLocationName() +" is already operational");
    else
        operationalStatus = FacilityStatus.OPERATIONAL;
}

// Removes up to 'amount' and returns how much was actually removed.
public int removeUpTo(ResourceType type, int amount) {
    int removed = Math.min(amount, getResourceCount(type));
    removeResource(type, removed);
    return removed;
}

}


