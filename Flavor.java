import java.util.Locale;

/** Represents an ice-cream flavor and its base price. */
public class Flavor {
    private final String name;
    private final double price;

    /** Creates a validated flavor. */
    public Flavor(String name, double price) {
        if (name == null || name.trim().isEmpty()
                || Double.isNaN(price) || Double.isInfinite(price)
                || price < 0.0) {
            throw new IllegalArgumentException("Invalid flavor.");
        }
        this.name = name.trim();
        this.price = price;
    }

    /** Returns the flavor name. */
    public String getName() {
        return name;
    }

    /** Returns the base price. */
    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Flavor)) {
            return false;
        }
        Flavor flavor = (Flavor) other;
        return name.equalsIgnoreCase(flavor.name)
                && Double.compare(price, flavor.price) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * name.toLowerCase(Locale.US).hashCode()
                + Double.valueOf(price).hashCode();
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s ($%.2f)", name, price);
    }
}
