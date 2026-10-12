/**
 * Builds ice cream orders and stores submitted orders
 * in a shared history. 
 *
 * 
 */
public class RyanOrderListAssembler {

    private IceCreamOrder currentOrder;
    private final LinkedChain<IceCreamOrder> completedOrders;

    /**
     * Creates an assembler that uses the team's shared order history.
     *
     * @param history the chain where submitted orders are stored
     * @throws IllegalArgumentException if history is null
     */
    public RyanOrderListAssembler(LinkedChain<IceCreamOrder> history) {
        if (history == null) {
            throw new IllegalArgumentException("Order history cannot be null.");
        }
        completedOrders = history;
        currentOrder = null;
    }

    /**
     * Starts a new order. Any unfinished order is discarded.
     *
     * @param cone the customer's cone choice
     * @param flavor the customer's flavor choice
     * @throws IllegalArgumentException if cone or flavor is null
     */
    public void startOrder(Cone cone, Flavor flavor) {
        if (cone == null || flavor == null) {
            throw new IllegalArgumentException("Cone and flavor are required.");
        }
        currentOrder = new IceCreamOrder(cone, flavor);
    }

    /**
     * @return true if a customer has started an order
     */
    public boolean hasCurrentOrder() {
        return currentOrder != null;
    }

    /**
     * @return the unfinished order, or null if none exists
     */
    public IceCreamOrder getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Adds one copy of a topping to the unfinished order.
     *
     * @param topping topping to add
     * @return false if no order exists or topping is null
     */
    public boolean addTopping(Topping topping) {
        if (currentOrder == null || topping == null) {
            return false;
        }
        return currentOrder.addTopping(topping);
    }

    /**
     * Removes one copy of the given topping from the unfinished order.
     *
     * @param topping topping to remove
     * @return false if there is no order or the topping is not found
     */
    public boolean removeTopping(Topping topping) {
        if (currentOrder == null || topping == null) {
            return false;
        }
        return currentOrder.removeTopping(topping);
    }

    /**
     * @return the unfinished order's total, or 0.0 if none exists
     */
    public double getCurrentTotal() {
        if (currentOrder == null) {
            return 0.0;
        }
        return currentOrder.getTotalPrice();
    }

    /**
     * Submits the unfinished order to history, newest first
     *
     * @return true if an order was submitted, false if no order exists
     */
    public boolean submitOrder() {
        if (currentOrder == null) {
            return false;
        }
        completedOrders.add(currentOrder);
        currentOrder = null;
        return true;
    }

    /**
     * Discards the unfinished order without modifying order history.
     */
    public void cancelOrder() {
        currentOrder = null;
    }

    /**
     * Returns completed orders newest first, as a separate array.
     *
     * @return a copy of the order history
     */
    public IceCreamOrder[] getHistoryArray() {
        IceCreamOrder[] result = new IceCreamOrder[completedOrders.size()];
        return completedOrders.toArray(result);
    }

    /**
     * @return number of previously submitted orders
     */
    public int getOrderCount() {
        return completedOrders.size();
    }
}
