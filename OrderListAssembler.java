/**
 * Builds ice-cream orders and stores submitted orders in a shared linked-chain
 * history.
 */
public class OrderListAssembler {
    private IceCreamOrder currentOrder;
    private final LinkedChain<IceCreamOrder> completedOrders;

    /** Creates an assembler that uses the supplied shared history. */
    public OrderListAssembler(LinkedChain<IceCreamOrder> history) {
        if (history == null) {
            throw new IllegalArgumentException("Order history cannot be null.");
        }
        completedOrders = history;
        currentOrder = null;
    }

    /** Starts a new order and discards any unfinished order. */
    public void startOrder(Cone cone, Flavor flavor) {
        if (cone == null || flavor == null) {
            throw new IllegalArgumentException("Cone and flavor are required.");
        }
        currentOrder = new IceCreamOrder(cone, flavor);
    }

    /** Returns true when an order is currently being edited. */
    public boolean hasCurrentOrder() {
        return currentOrder != null;
    }

    /** Returns the unfinished order, or null when none exists. */
    public IceCreamOrder getCurrentOrder() {
        return currentOrder;
    }

    /** Adds one topping to the unfinished order. */
    public boolean addTopping(Topping topping) {
        return currentOrder != null
                && topping != null
                && currentOrder.addTopping(topping);
    }

    /** Removes one matching topping from the unfinished order. */
    public boolean removeTopping(Topping topping) {
        return currentOrder != null
                && topping != null
                && currentOrder.removeTopping(topping);
    }

    /** Returns the current total, or zero when no order exists. */
    public double getCurrentTotal() {
        return currentOrder == null ? 0.0 : currentOrder.getTotalPrice();
    }

    /** Submits the current order to the front of the shared history. */
    public boolean submitOrder() {
        if (currentOrder == null) {
            return false;
        }
        completedOrders.add(currentOrder);
        currentOrder = null;
        return true;
    }

    /** Discards the current unfinished order. */
    public void cancelOrder() {
        currentOrder = null;
    }

    /** Returns a separate array containing history in newest-first order. */
    public IceCreamOrder[] getHistoryArray() {
        return completedOrders.toArray(
                new IceCreamOrder[completedOrders.size()]);
    }

    /** Returns the number of submitted orders. */
    public int getOrderCount() {
        return completedOrders.size();
    }
}
