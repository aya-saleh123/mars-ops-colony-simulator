package marscolony.Equipment;

public class Equipment {
    private EquipmentType type;
    private boolean operationalStatus;


    public Equipment(EquipmentType type) {
        this.type = type;
        operationalStatus = true;
    }

    public void fixEquipment()
    {
        operationalStatus = true;
    }

    public void damageEquipment()
    {
        operationalStatus = false;
    }

    public EquipmentType getType() {
        return type;
    }

    public boolean isOperationalStatus() {
        return operationalStatus;
    }

    


}
