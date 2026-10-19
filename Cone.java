import java.util.Locale;

/** Represents a cone or container choice and its additional price. */
public class Cone {
    private final String type;
    private final double price;

    /** Creates a validated cone or container. */
    public Cone(String type, double price) {
        if (type == null || type.trim().isEmpty()
                || Double.isNaN(price) || Double.isInfinite(price)
                || price < 0.0) {
            throw new IllegalArgumentException("Invalid cone.");
        }
        this.type = type.trim();
        this.price = price;
    }

    /** Returns the cone/container type. */
    public String getType() {
        return type;
    }

    /** Alias for code that calls the choice a name. */
    public String getName() {
        return type;
    }

    /** Returns the additional price. */
    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Cone)) {
            return false;
        }
        Cone cone = (Cone) other;
        return type.equalsIgnoreCase(cone.type)
                && Double.compare(price, cone.price) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * type.toLowerCase(Locale.US).hashCode()
                + Double.valueOf(price).hashCode();
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s ($%.2f)", type, price);
    }
}
