package marscolony.MarsColony;
import marscolony.facilities.Facilities;
import marscolony.resources.*;
import marscolony.personnel.*;
import java.util.ArrayList;
import java.util.List;

    
public class MarsColony {
    private static List<Facilities> facilities = new ArrayList<>();
    private static List<Agent> agents = new ArrayList<>();

    public static void addAgent(Agent agent) {
        agents.add(agent);
    }

    public static void addFacility(Facilities facility) {
        facilities.add(facility);
    }
    // we need to exclude destroyed and unavailable facilities
    private static int getTotalResourceCount(ResourceType resource)
    {
        int sum = 0;
        for (Facilities facility : facilities)
        {
            if(!facility.isOperationalStatus()) //IDK the destroyed for now
                continue;
            sum += facility.getResourceCount(resource);
        }
        return sum;
    }
    public static boolean deductResourceFromFacilities(ResourceType resource,int amount){
        if(amount>0)
        {
            if (amount<= getTotalResourceCount(resource))
            {
                findSuitableFacilitiesAndDeduct(resource, amount);
                return true;
            }
            else
                return false;

        }
        else
            throw new IllegalArgumentException("amount must be positive");

    }

    
    
private static void findSuitableFacilitiesAndDeduct(ResourceType resource, int amount) {

    while (amount > 0) {
        int maxCount = 0;
        Facilities maxFacility = null;

        for (Facilities facility : facilities) {
            if (facility.isOperationalStatus()) {
                int count = facility.getResourceCount(resource);

                if (count > maxCount) {
                    maxFacility = facility;
                    maxCount = count;
                }
            }
        }

        if (maxFacility == null) {
            return;
        }

        int deducted = Math.min(amount, maxCount);

        maxFacility.removeResource(resource, deducted);

        System.out.println(
            deducted + " units of " + resource
            + " deducted from " + maxFacility.getLocationName()
        );

        amount -= deducted;
    }
}

}