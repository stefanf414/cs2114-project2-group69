import java.util.Locale;

/** Represents one topping and its price. */
public class Topping {
    private final String name;
    private final double price;

    /** Creates a validated topping. */
    public Topping(String name, double price) {
        if (name == null || name.trim().isEmpty()
                || Double.isNaN(price) || Double.isInfinite(price)
                || price < 0.0) {
            throw new IllegalArgumentException("Invalid topping.");
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Topping)) {
            return false;
        }
        Topping topping = (Topping) other;
        return name.equalsIgnoreCase(topping.name)
                && Double.compare(price, topping.price) == 0;
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
