import java.util.Locale;

/** Represents one ice-cream order with a cone, flavor, and toppings. */
public class IceCreamOrder {
    private final Cone cone;
    private final Flavor flavor;
    private final LinkedChain<Topping> toppings;

    /** Creates an order from a cone and flavor. */
    public IceCreamOrder(Cone cone, Flavor flavor) {
        if (cone == null || flavor == null) {
            throw new IllegalArgumentException("Cone and flavor are required.");
        }
        this.cone = cone;
        this.flavor = flavor;
        toppings = new LinkedChain<Topping>();
    }

    /** Creates an order in a free cup using a flavor name and price. */
    public IceCreamOrder(String flavorName, double flavorPrice) {
        this(new Cone("Cup", 0.0), new Flavor(flavorName, flavorPrice));
    }

    /** Adds one topping at the end, including duplicate toppings. */
    public boolean addTopping(Topping topping) {
        if (topping == null) {
            return false;
        }
        toppings.addToEnd(topping);
        return true;
    }

    /** Removes one matching topping. */
    public boolean removeTopping(Topping topping) {
        return toppings.remove(topping);
    }

    /** Returns the selected cone. */
    public Cone getCone() {
        return cone;
    }

    /** Returns the selected flavor object. */
    public Flavor getFlavorObject() {
        return flavor;
    }

    /** Returns the selected flavor's name. */
    public String getFlavor() {
        return flavor.getName();
    }

    /** Returns the number of topping servings. */
    public int getToppingCount() {
        return toppings.size();
    }

    /** Alias used by the console interface and its tests. */
    public int toppingCount() {
        return getToppingCount();
    }

    /** Returns the number of occurrences of a topping. */
    public int countOf(Topping topping) {
        return toppings.count(topping);
    }

    /** Returns the topping at a zero-based index. */
    public Topping getTopping(int index) {
        return toppings.get(index);
    }

    /** Returns a separate array containing the toppings in order. */
    public Topping[] getToppingArray() {
        return toppings.toArray(new Topping[toppings.size()]);
    }

    /** Returns the total price of the cone, flavor, and all toppings. */
    public double getTotalPrice() {
        double total = cone.getPrice() + flavor.getPrice();
        for (Topping topping : getToppingArray()) {
            total += topping.getPrice();
        }
        return total;
    }

    /** Alias used by the console interface and its tests. */
    public double total() {
        return getTotalPrice();
    }

    /** Returns a human-readable order summary. */
    public String display() {
        StringBuilder result = new StringBuilder();
        result.append("Flavor: ").append(flavor.getName()).append("\n");
        result.append("Cone: ").append(cone.getName()).append("\n");
        result.append("Toppings: ");
        if (toppings.isEmpty()) {
            result.append("none");
        }
        else {
            for (int i = 0; i < toppings.size(); i++) {
                if (i > 0) {
                    result.append(", ");
                }
                result.append(toppings.get(i).getName());
            }
        }
        result.append(String.format(
                Locale.US, "%nTotal: $%.2f", getTotalPrice()));
        return result.toString();
    }
}
