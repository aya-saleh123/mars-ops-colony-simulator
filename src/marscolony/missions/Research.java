package marscolony.missions;
import marscolony.MarsColony.MarsColony;
import marscolony.facilities.Facilities;
import marscolony.facilities.FacilityStatus;
import marscolony.facilities.Laboratory;
import marscolony.personnel.Human;
import marscolony.resources.ResourceType;
import marscolony.Equipment.*;

public class Research extends Mission {
    Human researcher;
     public Research(String name,int duration) {
        super(name, duration);
    }

    @Override
    public int getRequiredAmount(ResourceType type) {
        if (type == ResourceType.WATER) { return 5; }
        if (type == ResourceType.BATTERY_POWER) { return 10; }
        return 0;
    }


   /*  @Override 
    public boolean checkRequiredForMission()
        {
            boolean humanStatus = false , equipmentStatus = false ,labStatus = false  ;
            for(Human human : MarsColony.getHumans())
            {
                    humanStatus = human.canPerform(Capability.SCIENCE); 
                    if(humanStatus)
                    {
                        this.assignAgent(human);
                        this.researcher = human;
                        break;
                    }
                   
            }
            for(Equipment equipment : MarsColony.getEquipments() )
            {
                if( equipment.getType() == EquipmentType.SCIENTIFIC_EQUIPMENT && equipment.isOperationalStatus())
                {
                    equipmentStatus = true;
                }
            }

            for(Facilities facility : MarsColony.getFacilities())
            {
                if(facility instanceof Laboratory)
                {
                    if(facility.getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL)
                    {
                        setTarget(facility);
                        labStatus = true;
                        break;
                    }
                }
            }
            
            if(humanStatus && equipmentStatus && labStatus){
                return true;
            }
            else
            {
                releaseAgents();
                return false;
            }
        }*/
   @Override
   public boolean checkRequiredForMission() {
       if (getMissionFailureReason() != FailureReason.NONE) {
           return false;
       }
       for (Human human : MarsColony.getHumans()) {
           if (human.isAvailable()&& human.canPerform(Capability.SCIENCE)) {
               assignAgent(human);
               break;
           }
       }
       for (Facilities facility : MarsColony.getFacilities()) {
           if (facility instanceof Laboratory && facility.getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL) {
               setTarget(facility);
               break;
           }
       }

       return true;
   }
    @Override 
   public FailureReason getMissionFailureReason() {
       boolean availableHumanExists = false;
       boolean suitableHumanExists = false;
       boolean equipmentStatus = false;
       boolean labStatus = false;

       for (Human human : MarsColony.getHumans()) {
           if (human.isAvailable()) {
               availableHumanExists = true;
           if (human.canPerform(Capability.SCIENCE)) {
                   suitableHumanExists = true;
               }
           }
       }

       if (!availableHumanExists) {
           return FailureReason.UNAVAILABLE_PERSONNEL;
       }

       if (!suitableHumanExists) {
           return FailureReason.UNSUITABLE_AGENT;
       }

       for (Equipment equipment : MarsColony.getEquipments()) {
           if (equipment.getType() == EquipmentType.SCIENTIFIC_EQUIPMENT && equipment.isOperationalStatus()) {
               equipmentStatus = true;
               break;
           }
       }

       if (!equipmentStatus) {
           return FailureReason.EQUIPMENT_FAILURE;
       }

       for (Facilities facility : MarsColony.getFacilities()) {
           if (facility instanceof Laboratory && facility.getFacilityOperationalStatus() == FacilityStatus.OPERATIONAL) {
               labStatus = true;
               break;
           }
       }
       if (!labStatus) {
           return FailureReason.UNAVAILABLE_FACILITIES;
       }
       return FailureReason.NONE;
   }

   public void completed() {
       super.Completed();
       System.out.println(this.researcher.getName() + " has completed the research mission successfully at "
               + getTarget().getLocationName());

   }

}
