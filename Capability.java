
package marscolony;

public class Resource {

    private ResourceType type;
    private int quantity;

    public Resource(ResourceType type, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(
                "Resource quantity cannot be negative"
            );
        }

        this.type = type;
        this.quantity = quantity;
    }

    public ResourceType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(
                "Amount cannot be negative"
            );
        }

        quantity += amount;
    }

    public boolean removeQuantity(int amount) {
        if (amount < 0 || amount > quantity) {
            return false;
        }

        quantity -= amount;
        return true;
    }
}
