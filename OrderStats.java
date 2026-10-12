import java.util.Locale;

/**
 * Provides read-only statistics for a shared chain of completed orders.
 * OrderStats never adds, removes, or changes orders.
 *
 * @author Ashwin
 * @version October 2026
 */
public class OrderStats {
    private final LinkedChain<IceCreamOrder> completedOrders;

    /** Stores a reference to the shared completed-order chain. */
    public OrderStats(LinkedChain<IceCreamOrder> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("Order chain must not be null.");
        }
        completedOrders = orders;
    }

    /** Returns true when at least one completed order exists. */
    public boolean hasOrders() {
        return !completedOrders.isEmpty();
    }

    /** Returns the number of completed orders. */
    public int getTotalOrders() {
        return completedOrders.size();
    }

    /** Returns the most popular flavor, or null when history is empty. */
    public String getMostPopularFlavor() {
        IceCreamOrder[] orders = snapshot();
        if (orders.length == 0) {
            return null;
        }
        String[] names = new String[orders.length];
        int[] counts = new int[orders.length];
        int distinct = 0;

        for (IceCreamOrder order : orders) {
            String flavor = order.getFlavor();
            int slot = indexOf(names, distinct, flavor);
            if (slot < 0) {
                names[distinct] = flavor;
                counts[distinct] = 1;
                distinct++;
            }
            else {
                counts[slot]++;
            }
        }
        return names[maxIndex(counts, distinct)];
    }

    /** Returns the most common topping, or null when none have been ordered. */
    public String getMostCommonTopping() {
        IceCreamOrder[] orders = snapshot();
        int capacity = 0;
        for (IceCreamOrder order : orders) {
            capacity += order.getToppingCount();
        }
        if (capacity == 0) {
            return null;
        }

        String[] names = new String[capacity];
        int[] counts = new int[capacity];
        int distinct = 0;
        for (IceCreamOrder order : orders) {
            for (Topping topping : order.getToppingArray()) {
                int slot = indexOf(names, distinct, topping.getName());
                if (slot < 0) {
                    names[distinct] = topping.getName();
                    counts[distinct] = 1;
                    distinct++;
                }
                else {
                    counts[slot]++;
                }
            }
        }
        return names[maxIndex(counts, distinct)];
    }

    /** Counts completed orders by flavor, ignoring case and whitespace. */
    public int getFlavorCount(String flavor) {
        requireName(flavor);
        String wanted = flavor.trim();
        int count = 0;
        for (IceCreamOrder order : snapshot()) {
            if (order.getFlavor().equalsIgnoreCase(wanted)) {
                count++;
            }
        }
        return count;
    }

    /** Counts topping servings across all completed orders. */
    public int getToppingCount(String name) {
        requireName(name);
        String wanted = name.trim();
        int count = 0;
        for (IceCreamOrder order : snapshot()) {
            for (Topping topping : order.getToppingArray()) {
                if (topping.getName().equalsIgnoreCase(wanted)) {
                    count++;
                }
            }
        }
        return count;
    }

    /** Returns the average order price, or zero when history is empty. */
    public double getAverageOrderPrice() {
        IceCreamOrder[] orders = snapshot();
        if (orders.length == 0) {
            return 0.0;
        }
        double total = 0.0;
        for (IceCreamOrder order : orders) {
            total += order.getTotalPrice();
        }
        return total / orders.length;
    }

    /** Returns a formatted statistics summary. */
    public String getSummary() {
        if (!hasOrders()) {
            return "No orders yet.";
        }
        String topping = getMostCommonTopping();
        return String.format(Locale.US,
                "Total orders: %d%nMost popular flavor: %s%n"
                + "Most common topping: %s%nAverage order price: $%.2f",
                getTotalOrders(), getMostPopularFlavor(),
                topping == null ? "none" : topping,
                getAverageOrderPrice());
    }

    /** Returns a separate array containing the current order history. */
    private IceCreamOrder[] snapshot() {
        return completedOrders.toArray(
                new IceCreamOrder[completedOrders.size()]);
    }

    /** Finds a name in the used portion of an array, ignoring case. */
    private static int indexOf(String[] names, int used, String name) {
        for (int i = 0; i < used; i++) {
            if (names[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    /** Returns the index containing the greatest count. */
    private static int maxIndex(int[] counts, int used) {
        int best = 0;
        for (int i = 1; i < used; i++) {
            if (counts[i] > counts[best]) {
                best = i;
            }
        }
        return best;
    }

    /** Rejects null and blank names. */
    private static void requireName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be blank.");
        }
    }
}
