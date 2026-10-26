package marscolony.facilities;

public class Laboratory extends Facilities {
    private static char nextChar = 'A';
    public Laboratory(int capacity, boolean operationalStatus, int oxygenCount , int waterCount,int  foodCount , int fuelCount, int batteryCount , int medicalSuppliesCount, int sparePartsCount) {
        super("Laboratory "+nextChar,capacity,operationalStatus,oxygenCount,waterCount,foodCount,fuelCount,batteryCount,medicalSuppliesCount,sparePartsCount);
        nextChar++;
    }

}
