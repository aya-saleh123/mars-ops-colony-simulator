package marscolony.facilities;

public class PowerStation extends Facilities {
    private static char nextChar = 'A';

    public PowerStation(int capacity, boolean operationalStatus, int oxygenCount , int waterCount,int  foodCount , int fuelCount, int batteryCount , int medicalSuppliesCount, int sparePartsCount) {
        super("Power Station "+ nextChar,capacity,operationalStatus,oxygenCount,waterCount,foodCount,fuelCount,batteryCount,medicalSuppliesCount,sparePartsCount);
        nextChar++;
    }   

}
