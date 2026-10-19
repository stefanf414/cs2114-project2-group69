/**
 * Builds the current order and stores completed orders newest-first.
 */
public class OrderListAssembler {
    private final LinkedChain<IceCreamOrder> completedOrders;
    private IceCreamOrder currentOrder;

    /** Creates an assembler backed by a shared history chain. */
    public OrderListAssembler(LinkedChain<IceCreamOrder> history) {
        if (history == null) {
            throw new IllegalArgumentException("Order history cannot be null.");
        }
        completedOrders = history;
    }

    /** Compatibility constructor accepting a menu collaborator. */
    public OrderListAssembler(Menu menu, LinkedChain<IceCreamOrder> history) {
        this(history);
        if (menu == null) {
            throw new IllegalArgumentException("Menu cannot be null.");
        }
    }

    /** Starts a new order, discarding any unfinished order. */
    public void startOrder(Cone cone, Flavor flavor) {
        currentOrder = new IceCreamOrder(cone, flavor);
    }

    /** Returns whether an order is in progress. */
    public boolean hasCurrentOrder() {
        return currentOrder != null;
    }

    /** Returns the current order, or null. */
    public IceCreamOrder getCurrentOrder() {
        return currentOrder;
    }

    /** Adds one topping to the current order. */
    public boolean addTopping(Topping topping) {
        return currentOrder != null && currentOrder.addTopping(topping);
    }

    /** Removes one topping occurrence. */
    public boolean removeTopping(Topping topping) {
        return currentOrder != null && currentOrder.removeTopping(topping);
    }

    /** Returns the current total, or zero without an order. */
    public double getCurrentTotal() {
        return currentOrder == null ? 0.0 : currentOrder.getTotalPrice();
    }

    /** Submits the current order and clears it. */
    public boolean submitOrder() {
        if (currentOrder == null) {
            return false;
        }
        completedOrders.add(currentOrder);
        currentOrder = null;
        return true;
    }

    /** Cancels the current order. */
    public void cancelOrder() {
        currentOrder = null;
    }

    /** Returns all completed orders, newest first. */
    public IceCreamOrder[] getHistoryArray() {
        return completedOrders.toArray(
                new IceCreamOrder[completedOrders.size()]);
    }

    /** Returns the number of completed orders. */
    public int getOrderCount() {
        return completedOrders.size();
    }
}
