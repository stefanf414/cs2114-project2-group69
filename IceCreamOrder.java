import java.util.Locale;

/** One order containing a cone, flavor, and duplicate-permitting toppings. */
public class IceCreamOrder {
    private final Cone cone;
    private final Flavor flavor;
    private final LinkedChain<Topping> toppings;

    /** Creates an order with no toppings. */
    public IceCreamOrder(Cone cone, Flavor flavor) {
        if (cone == null || flavor == null) {
            throw new IllegalArgumentException("Cone and flavor are required.");
        }
        this.cone = cone;
        this.flavor = flavor;
        toppings = new LinkedChain<Topping>();
    }

    /** Returns the selected cone/container. */
    public Cone getCone() {
        return cone;
    }

    /** Returns the selected flavor object. */
    public Flavor getFlavor() {
        return flavor;
    }

    /** Returns the selected flavor name. */
    public String getFlavorName() {
        return flavor.getName();
    }

    /** Adds one topping; duplicates are allowed. */
    public boolean addTopping(Topping topping) {
        if (topping == null) {
            return false;
        }
        toppings.addToEnd(topping);
        return true;
    }

    /** Removes one matching topping occurrence. */
    public boolean removeTopping(Topping topping) {
        return toppings.remove(topping);
    }

    /** Counts all topping servings. */
    public int getToppingCount() {
        return toppings.size();
    }

    /** Compatibility alias for topping count. */
    public int toppingCount() {
        return getToppingCount();
    }

    /** Counts matching topping servings. */
    public int countOf(Topping topping) {
        return toppings.count(topping);
    }

    /** Returns one topping by index. */
    public Topping getTopping(int index) {
        return toppings.get(index);
    }

    /** Returns a copy of all toppings. */
    public Topping[] getToppingArray() {
        return toppings.toArray(new Topping[toppings.size()]);
    }

    /** Calculates the complete price, counting duplicate toppings. */
    public double getTotalPrice() {
        double total = cone.getPrice() + flavor.getPrice();
        for (Topping topping : getToppingArray()) {
            total += topping.getPrice();
        }
        return total;
    }

    /** Compatibility alias for total price. */
    public double total() {
        return getTotalPrice();
    }

    /** Returns an itemized order description. */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Cone: ").append(cone).append("\n");
        result.append("Flavor: ").append(flavor).append("\n");
        for (Topping topping : getToppingArray()) {
            result.append("Topping: ").append(topping).append("\n");
        }
        result.append(String.format(Locale.US, "Total: $%.2f",
                getTotalPrice()));
        return result.toString();
    }

    /** Compatibility alias for display text. */
    public String display() {
        return toString();
    }
}
