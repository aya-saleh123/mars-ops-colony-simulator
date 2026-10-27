package marscolony.facilities;

public class CommunicationStation extends Facilities {

    private static char nextChar = 'A';
    public CommunicationStation(int capacity, int oxygenCount , int waterCount,int  foodCount , int fuelCount, int batteryCount , int medicalSuppliesCount, int sparePartsCount) {
        super("Communication Station "+nextChar,capacity,oxygenCount,waterCount,foodCount,fuelCount,batteryCount,medicalSuppliesCount,sparePartsCount);
        nextChar++;
    }
}
