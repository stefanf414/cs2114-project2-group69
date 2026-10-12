import java.util.Locale;

/**
 * Represents one immutable ice-cream topping and the price charged for one
 * serving. Equal toppings have the same trimmed name and price.
 */
public class Topping {
    private final String name;
    private final double price;

    /** Creates a validated topping. */
    public Topping(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Topping name cannot be blank.");
        }
        if (Double.isNaN(price) || Double.isInfinite(price) || price < 0.0) {
            throw new IllegalArgumentException(
                    "Topping price must be valid and nonnegative.");
        }
        this.name = name.trim();
        this.price = price;
    }

    /** Returns the topping name. */
    public String getName() {
        return name;
    }

    /** Returns the topping price. */
    public double getPrice() {
        return price;
    }

    /** Returns whether another object has the same name and price. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Topping)) {
            return false;
        }
        Topping topping = (Topping)other;
        return name.equals(topping.name)
                && Double.compare(price, topping.price) == 0;
    }

    /** Returns a hash code consistent with equals. */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        long bits = Double.doubleToLongBits(price);
        return 31 * result + (int)(bits ^ (bits >>> 32));
    }

    /** Returns display text containing the name and price. */
    @Override
    public String toString() {
        return String.format(Locale.US, "%s ($%.2f)", name, price);
    }
}
