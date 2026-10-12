import java.util.Locale;

/** Represents an immutable cone choice and its price. */
public class Cone {
    private final String name;
    private final double price;

    /** Creates a validated cone. */
    public Cone(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Cone name cannot be blank.");
        }
        if (Double.isNaN(price) || Double.isInfinite(price) || price < 0.0) {
            throw new IllegalArgumentException(
                    "Cone price must be valid and nonnegative.");
        }
        this.name = name.trim();
        this.price = price;
    }

    /** Returns the cone name. */
    public String getName() {
        return name;
    }

    /** Returns the cone price. */
    public double getPrice() {
        return price;
    }

    /** Returns display text containing the name and price. */
    @Override
    public String toString() {
        return String.format(Locale.US, "%s ($%.2f)", name, price);
    }
}
