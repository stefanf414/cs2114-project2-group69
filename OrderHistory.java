import java.util.Locale;

/** Stores submitted ice-cream orders in newest-first order. */
public class OrderHistory {
    private final LinkedChain<IceCreamOrder> orders;

    /** Creates an empty order history. */
    public OrderHistory() {
        orders = new LinkedChain<IceCreamOrder>();
    }

    /** Adds an order as the newest completed order. */
    public void submit(IceCreamOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        orders.add(order);
    }

    /** Returns the number of completed orders. */
    public int size() {
        return orders.size();
    }

    /** Returns the order at a zero-based index, newest first. */
    public IceCreamOrder getOrder(int index) {
        return orders.get(index);
    }

    /** Returns a formatted list of completed orders. */
    public String display() {
        if (orders.isEmpty()) {
            return "Completed orders: none";
        }
        StringBuilder result = new StringBuilder("Completed orders:\n");
        for (int i = 0; i < orders.size(); i++) {
            IceCreamOrder order = orders.get(i);
            result.append(i + 1).append(". ").append(order.getFlavor());
            result.append(String.format(
                    Locale.US, " ($%.2f)", order.total()));
            if (i + 1 < orders.size()) {
                result.append("\n");
            }
        }
        return result.toString();
    }
}
