package marscolony.facilities;
import java.util.ArrayList;
import java.util.List;

public class MarsColony {
    private static List<Facilities> facilities = new ArrayList<>();

    public static void addFacility(Facilities facility) {
        facilities.add(facility);
    }
}
