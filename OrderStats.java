import java.util.Locale;

/** Read-only statistics over completed orders. */
public class OrderStats {
    private final LinkedChain<IceCreamOrder> completedOrders;

    /** Creates statistics backed by a non-null order chain. */
    public OrderStats(LinkedChain<IceCreamOrder> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("Order chain must not be null.");
        }
        completedOrders = orders;
    }

    /** Returns whether history contains an order. */
    public boolean hasOrders() {
        return !completedOrders.isEmpty();
    }

    /** Returns the number of completed orders. */
    public int getTotalOrders() {
        return completedOrders.size();
    }

    /** Returns the most popular flavor; first seen wins ties. */
    public String getMostPopularFlavor() {
        IceCreamOrder[] orders = snapshot();
        String[] names = new String[orders.length];
        int[] counts = new int[orders.length];
        int used = 0;
        for (IceCreamOrder order : orders) {
            String name = order.getFlavor().getName();
            int index = find(names, used, name);
            if (index < 0) {
                names[used] = name;
                counts[used++] = 1;
            } else {
                counts[index]++;
            }
        }
        return used == 0 ? null : names[maxIndex(counts, used)];
    }

    /** Returns the most common topping, counting duplicates. */
    public String getMostCommonTopping() {
        IceCreamOrder[] orders = snapshot();
        int capacity = 0;
        for (IceCreamOrder order : orders) {
            capacity += order.getToppingCount();
        }
        String[] names = new String[capacity];
        int[] counts = new int[capacity];
        int used = 0;
        for (IceCreamOrder order : orders) {
            for (Topping topping : order.getToppingArray()) {
                int index = find(names, used, topping.getName());
                if (index < 0) {
                    names[used] = topping.getName();
                    counts[used++] = 1;
                } else {
                    counts[index]++;
                }
            }
        }
        return used == 0 ? null : names[maxIndex(counts, used)];
    }

    /** Counts orders with a flavor name, ignoring case. */
    public int getFlavorCount(String flavor) {
        requireName(flavor);
        int result = 0;
        for (IceCreamOrder order : snapshot()) {
            if (order.getFlavor().getName().equalsIgnoreCase(flavor.trim())) {
                result++;
            }
        }
        return result;
    }

    /** Counts topping servings with a name, ignoring case. */
    public int getToppingCount(String name) {
        requireName(name);
        int result = 0;
        for (IceCreamOrder order : snapshot()) {
            for (Topping topping : order.getToppingArray()) {
                if (topping.getName().equalsIgnoreCase(name.trim())) {
                    result++;
                }
            }
        }
        return result;
    }

    /** Returns average order price, or zero if empty. */
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

    /** Returns the required summary report. */
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

    private IceCreamOrder[] snapshot() {
        return completedOrders.toArray(
                new IceCreamOrder[completedOrders.size()]);
    }

    private static int find(String[] names, int used, String wanted) {
        for (int i = 0; i < used; i++) {
            if (names[i].equalsIgnoreCase(wanted)) {
                return i;
            }
        }
        return -1;
    }

    private static int maxIndex(int[] counts, int used) {
        int best = 0;
        for (int i = 1; i < used; i++) {
            if (counts[i] > counts[best]) {
                best = i;
            }
        }
        return best;
    }

    private static void requireName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be blank.");
        }
    }
}
