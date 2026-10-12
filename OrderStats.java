/**
 * Read-only statistics over the shared chain of completed orders.
 * OrderStats never adds, removes, or changes orders; it only reads the
 * chain through LinkedChain's public API (size, isEmpty, toArray).
 *
 * The chain is held by reference, so orders submitted later by the
 * OrderListAssembler show up here automatically.
 *
 * @author Ashwin
 * @version October 2026
 */
public class OrderStats
{
    private final LinkedChain<IceCreamOrder> completedOrders;

    /**
     * Stores a reference to the shared history chain.
     *
     * @param orders the completed-order chain
     * @throws IllegalArgumentException if orders is null
     */
    public OrderStats(LinkedChain<IceCreamOrder> orders)
    {
        if (orders == null)
        {
            throw new IllegalArgumentException("Order chain must not be null");
        }
        completedOrders = orders;
    }

    /** @return true if at least one order has been completed. O(1) */
    public boolean hasOrders()
    {
        return !completedOrders.isEmpty();
    }

    /** @return number of completed orders. O(1) */
    public int getTotalOrders()
    {
        return completedOrders.size();
    }

    /**
     * Flavor name that appears in the most orders. Ties go to the flavor
     * seen first (chain order, newest order first).
     *
     * @return the flavor name, or null if there are no orders
     */
    public String getMostPopularFlavor()
    {
        IceCreamOrder[] orders = snapshot();
        if (orders.length == 0)
        {
            return null;
        }
        String[] names = new String[orders.length];
        int[] counts = new int[orders.length];
        int distinct = 0;
        for (IceCreamOrder order : orders)
        {
            String flavor = order.getFlavor().getName();
            int slot = indexOf(names, distinct, flavor);
            if (slot < 0)
            {
                names[distinct] = flavor;
                counts[distinct] = 1;
                distinct++;
            }
            else
            {
                counts[slot]++;
            }
        }
        return names[maxIndex(counts, distinct)];
    }

    /**
     * Topping name used most across all orders, duplicates counted. Ties
     * go to the topping seen first.
     *
     * @return the topping name, or null if no order has any topping
     */
    public String getMostCommonTopping()
    {
        IceCreamOrder[] orders = snapshot();
        int capacity = 0;
        for (IceCreamOrder order : orders)
        {
            capacity += order.getToppingCount();
        }
        if (capacity == 0)
        {
            return null;
        }
        String[] names = new String[capacity];
        int[] counts = new int[capacity];
        int distinct = 0;
        for (IceCreamOrder order : orders)
        {
            for (Topping t : order.getToppingArray())
            {
                int slot = indexOf(names, distinct, t.getName());
                if (slot < 0)
                {
                    names[distinct] = t.getName();
                    counts[distinct] = 1;
                    distinct++;
                }
                else
                {
                    counts[slot]++;
                }
            }
        }
        return names[maxIndex(counts, distinct)];
    }

    /**
     * Number of orders with the given flavor (case-insensitive).
     *
     * @param flavor the flavor name
     * @return orders with that flavor; 0 if never ordered
     * @throws IllegalArgumentException if flavor is null or blank
     */
    public int getFlavorCount(String flavor)
    {
        requireName(flavor);
        String wanted = flavor.trim();
        int count = 0;
        for (IceCreamOrder order : snapshot())
        {
            if (order.getFlavor().getName().equalsIgnoreCase(wanted))
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Times the named topping appears across all orders, duplicates
     * counted (case-insensitive).
     *
     * @param name the topping name
     * @return total occurrences; 0 if never used
     * @throws IllegalArgumentException if name is null or blank
     */
    public int getToppingCount(String name)
    {
        requireName(name);
        String wanted = name.trim();
        int count = 0;
        for (IceCreamOrder order : snapshot())
        {
            for (Topping t : order.getToppingArray())
            {
                if (t.getName().equalsIgnoreCase(wanted))
                {
                    count++;
                }
            }
        }
        return count;
    }

    /** @return mean order total, or 0.0 if there are no orders. O(n) */
    public double getAverageOrderPrice()
    {
        IceCreamOrder[] orders = snapshot();
        if (orders.length == 0)
        {
            return 0.0;
        }
        double sum = 0.0;
        for (IceCreamOrder order : orders)
        {
            sum += order.getTotalPrice();
        }
        return sum / orders.length;
    }

    /**
     * Multi-line report of the statistics.
     *
     * @return the report, or "No orders yet." if there are no orders
     */
    public String getSummary()
    {
        if (!hasOrders())
        {
            return "No orders yet.";
        }
        String topping = getMostCommonTopping();
        StringBuilder sb = new StringBuilder();
        sb.append("Total orders: ").append(getTotalOrders()).append("\n");
        sb.append("Most popular flavor: ").append(getMostPopularFlavor())
          .append("\n");
        sb.append("Most common topping: ")
          .append(topping == null ? "none" : topping).append("\n");
        sb.append(String.format("Average order price: $%.2f",
            getAverageOrderPrice()));
        return sb.toString();
    }

    // ---- helpers ----

    /** Copies the chain into an array once (O(n)); never modifies it. */
    private IceCreamOrder[] snapshot()
    {
        return completedOrders.toArray(
            new IceCreamOrder[completedOrders.size()]);
    }

    private static int indexOf(String[] names, int used, String name)
    {
        for (int i = 0; i < used; i++)
        {
            if (names[i].equalsIgnoreCase(name))
            {
                return i;
            }
        }
        return -1;
    }

    /** Index of the largest count; the earliest index wins ties. */
    private static int maxIndex(int[] counts, int used)
    {
        int best = 0;
        for (int i = 1; i < used; i++)
        {
            if (counts[i] > counts[best])
            {
                best = i;
            }
        }
        return best;
    }

    private static void requireName(String name)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Name must not be blank");
        }
    }
}
