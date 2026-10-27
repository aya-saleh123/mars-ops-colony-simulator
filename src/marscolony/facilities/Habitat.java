package marscolony.facilities;

public class Habitat extends Facilities{
    private static char nextChar = 'A';

    public Habitat(int capacity, int oxygenCount , int waterCount,int  foodCount , int fuelCount, int batteryCount , int medicalSuppliesCount, int sparePartsCount) {
        super("Habitat "+ nextChar ,capacity,oxygenCount,waterCount,foodCount,fuelCount,batteryCount,medicalSuppliesCount,sparePartsCount);
        nextChar++;
    }

    

}
