package marscolony.facilities;

public class StorageFacility extends Facilities {

     private static char nextChar = 'A';
    public StorageFacility( int capacity, boolean operationalStatus, int oxygenCount , int waterCount,int  foodCount , int fuelCount, int batteryCount , int medicalSuppliesCount, int sparePartsCount) {
        super("Storage Facility "+nextChar,capacity,operationalStatus,oxygenCount,waterCount,foodCount,fuelCount,batteryCount,medicalSuppliesCount,sparePartsCount);
        nextChar++;
    }

}
