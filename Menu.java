/**
 * Fixed menu of valid cones, flavors, and toppings.
 */
public class Menu {
    private final Cone[] cones;
    private final Flavor[] flavors;
    private final Topping[] toppings;

    /** Builds the fixed menu. */
    public Menu() {
        cones = new Cone[] {
            new Cone("Cup", 0.00),
            new Cone("Cone", 0.00),
            new Cone("Waffle Cone", 1.00),
            new Cone("Waffle Cup", 1.00)
        };
        flavors = new Flavor[] {
            new Flavor("Vanilla", 3.00),
            new Flavor("Chocolate", 3.25),
            new Flavor("Strawberry", 3.50)
        };
        toppings = new Topping[] {
            new Topping("Sprinkles", 0.50),
            new Topping("Oreo", 0.75),
            new Topping("Brownie", 1.00),
            new Topping("Caramel", 0.65)
        };
    }

    /** Returns a copy of the cone choices. */
    public Cone[] getCones() {
        return cones.clone();
    }

    /** Returns a copy of the flavor choices. */
    public Flavor[] getFlavors() {
        return flavors.clone();
    }

    /** Returns a copy of the topping choices. */
    public Topping[] getToppings() {
        return toppings.clone();
    }

    /** Returns a cone by zero-based index. */
    public Cone getCone(int index) {
        check(index, cones.length);
        return cones[index];
    }

    /** Returns a flavor by zero-based index. */
    public Flavor getFlavor(int index) {
        check(index, flavors.length);
        return flavors[index];
    }

    /** Returns a topping by zero-based index. */
    public Topping getTopping(int index) {
        check(index, toppings.length);
        return toppings[index];
    }

    /** Returns one recommended topping for a flavor. */
    public Topping getRecommendedTopping(Flavor flavor) {
        if (flavor == null) {
            throw new IllegalArgumentException("Flavor cannot be null.");
        }
        if (flavor.getName().equalsIgnoreCase("Chocolate")) {
            return getTopping(2);
        }
        if (flavor.getName().equalsIgnoreCase("Strawberry")) {
            return getTopping(0);
        }
        return getTopping(1);
    }

    /** Returns recommended toppings for a flavor. */
    public Topping[] getRecommendedToppings(Flavor flavor) {
        return new Topping[] { getRecommendedTopping(flavor) };
    }

    private static void check(int index, int length) {
        if (index < 0 || index >= length) {
            throw new IllegalArgumentException("Menu index is out of range.");
        }
    }
}
