/** Compatibility wrapper around a completed-order chain. */
public class OrderHistory {
    private final LinkedChain<IceCreamOrder> orders =
            new LinkedChain<IceCreamOrder>();

    /** Adds an order as newest. */
    public void submit(IceCreamOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        orders.add(order);
    }

    /** Returns the number of orders. */
    public int size() {
        return orders.size();
    }

    /** Returns an order by newest-first index. */
    public IceCreamOrder getOrder(int index) {
        return orders.get(index);
    }

    /** Returns the backing chain. */
    public LinkedChain<IceCreamOrder> getChain() {
        return orders;
    }

    /** Returns formatted history. */
    public String display() {
        if (orders.isEmpty()) {
            return "No orders yet.";
        }
        StringBuilder result = new StringBuilder("Completed orders:\n");
        for (int i = 0; i < orders.size(); i++) {
            result.append(i + 1).append(".\n").append(orders.get(i));
            if (i + 1 < orders.size()) {
                result.append("\n");
            }
        }
        return result.toString();
    }
}
