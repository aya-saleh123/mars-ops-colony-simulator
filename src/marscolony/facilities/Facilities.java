package marscolony.facilities;
import marscolony.resources.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Facilities {

    private String locationName;
    private int capacity;
    private boolean operationalStatus;
    // list of all resources
    private List<Resource> resources;

    public Facilities(
            String locationName,
            int capacity,
            boolean operationalStatus,
            int oxygenCount,
            int waterCount,
            int foodCount,
            int fuelCount,
            int batteryCount,
            int medicalSuppliesCount,
            int sparePartsCount) {

        this.locationName = locationName;
        setCapacity(capacity);
        this.operationalStatus = operationalStatus;

        resources = new ArrayList<>();

        resources.add(new Resource(ResourceType.OXYGEN, oxygenCount));
        resources.add(new Resource(ResourceType.WATER, waterCount));
        resources.add(new Resource(ResourceType.FOOD, foodCount));
        resources.add(new Resource(ResourceType.FUEL, fuelCount));
        resources.add(new Resource(ResourceType.BATTERY_POWER, batteryCount));
        resources.add(new Resource(ResourceType.MEDICAL_SUPPLIES, medicalSuppliesCount));
        resources.add(new Resource(ResourceType.SPARE_PARTS, sparePartsCount));
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

    public boolean isOperationalStatus() {
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
}

