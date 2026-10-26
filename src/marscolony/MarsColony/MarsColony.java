package marscolony.MarsColony;
import marscolony.facilities.Facilities;
import marscolony.resources.*;
import java.util.ArrayList;
import java.util.List;

    
public class MarsColony {
    private static List<Facilities> facilities = new ArrayList<>();
    
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
    }