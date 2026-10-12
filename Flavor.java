import java.util.Locale;

/** Represents an immutable ice-cream flavor and its price. */
public class Flavor {
    private final String name;
    private final double price;

    /** Creates a validated flavor. */
    public Flavor(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Flavor name cannot be blank.");
        }
        if (Double.isNaN(price) || Double.isInfinite(price) || price < 0.0) {
            throw new IllegalArgumentException(
                    "Flavor price must be valid and nonnegative.");
        }
        this.name = name.trim();
        this.price = price;
    }

    /** Returns the flavor name. */
    public String getName() {
        return name;
    }

    /** Returns the flavor price. */
    public double getPrice() {
        return price;
    }

    /** Returns display text containing the name and price. */
    @Override
    public String toString() {
        return String.format(Locale.US, "%s ($%.2f)", name, price);
    }
}
