package marscolony.facilities;

public class Hospital extends Habitat {

    public Hospital(
            int capacity,
            int oxygenCount,
            int waterCount,
            int foodCount,
            int fuelCount,
            int batteryCount,
            int medicalSuppliesCount,
            int sparePartsCount) {

        super(
            capacity,
            oxygenCount,
            waterCount,
            foodCount,
            fuelCount,
            batteryCount,
            medicalSuppliesCount,
            sparePartsCount
        );
    }
}