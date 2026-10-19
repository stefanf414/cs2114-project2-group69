import student.TestCase;

/** Tests history statistics and duplicate topping counts. */
public class OrderStatsTest extends TestCase {
    public void testSummaryAndCounts() {
        LinkedChain<IceCreamOrder> history =
                new LinkedChain<IceCreamOrder>();
        OrderStats stats = new OrderStats(history);
        Cone cone = new Cone("Cup", 0.0);
        Flavor vanilla = new Flavor("Vanilla", 2.0);
        Topping sprinkles = new Topping("Sprinkles", 0.5);

        IceCreamOrder order = new IceCreamOrder(cone, vanilla);
        order.addTopping(sprinkles);
        order.addTopping(sprinkles);
        history.add(order);

        assertEquals("Vanilla", stats.getMostPopularFlavor());
        assertEquals("Sprinkles", stats.getMostCommonTopping());
        assertEquals(1, stats.getFlavorCount(" vanilla "));
        assertEquals(2, stats.getToppingCount("SPRINKLES"));
        assertEquals(3.0, stats.getAverageOrderPrice(), 0.001);
        assertTrue(stats.getSummary().contains("Total orders: 1"));
    }

    public void testEmptyAndInvalidLookup() {
        OrderStats stats = new OrderStats(
                new LinkedChain<IceCreamOrder>());
        assertFalse(stats.hasOrders());
        assertNull(stats.getMostPopularFlavor());
        assertEquals("No orders yet.", stats.getSummary());
        try {
            stats.getFlavorCount(" ");
            fail("Blank names must be rejected.");
        } catch (IllegalArgumentException expected) {
            // Expected.
        }
    }
}
