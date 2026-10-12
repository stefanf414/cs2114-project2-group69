import student.TestCase;

/** Tests every public behavior of OrderStats. */
public class OrderStatsTest extends TestCase {
    private Cone cone;
    private Flavor vanilla;
    private Flavor chocolate;
    private Topping sprinkles;
    private Topping fudge;
    private LinkedChain<IceCreamOrder> history;
    private OrderStats stats;

    /** Creates fresh test objects before each test. */
    public void setUp() {
        cone = new Cone("Waffle", 1.00);
        vanilla = new Flavor("Vanilla", 2.00);
        chocolate = new Flavor("Chocolate", 2.00);
        sprinkles = new Topping("Sprinkles", 0.50);
        fudge = new Topping("Fudge", 0.75);
        history = new LinkedChain<IceCreamOrder>();
        stats = new OrderStats(history);
    }

    /** Creates an order with zero or more toppings. */
    private IceCreamOrder order(Flavor flavor, Topping... toppings) {
        IceCreamOrder result = new IceCreamOrder(cone, flavor);
        for (Topping topping : toppings) {
            result.addTopping(topping);
        }
        return result;
    }

    /** Tests construction and rejection of a null chain. */
    public void testConstructor() {
        assertNotNull(new OrderStats(new LinkedChain<IceCreamOrder>()));
        try {
            new OrderStats(null);
            fail("A null order chain should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
    }

    /** Tests empty and nonempty order status. */
    public void testHasOrdersAndTotal() {
        assertFalse(stats.hasOrders());
        assertEquals(0, stats.getTotalOrders());
        history.add(order(vanilla));
        assertTrue(stats.hasOrders());
        assertEquals(1, stats.getTotalOrders());
    }

    /** Tests the most popular flavor and the empty result. */
    public void testMostPopularFlavor() {
        assertNull(stats.getMostPopularFlavor());
        history.add(order(vanilla));
        history.add(order(vanilla));
        history.add(order(chocolate));
        assertEquals("Vanilla", stats.getMostPopularFlavor());
    }

    /** Tests most-common-topping counting, including duplicates. */
    public void testMostCommonTopping() {
        history.add(order(vanilla));
        assertNull(stats.getMostCommonTopping());
        history.add(order(vanilla, sprinkles, sprinkles));
        history.add(order(chocolate, fudge));
        assertEquals("Sprinkles", stats.getMostCommonTopping());
    }

    /** Tests case-insensitive flavor and topping counts. */
    public void testCountsAreCaseInsensitive() {
        history.add(order(vanilla, sprinkles, sprinkles));
        assertEquals(1, stats.getFlavorCount(" vanilla "));
        assertEquals(2, stats.getToppingCount("sprinkles"));
        assertEquals(0, stats.getFlavorCount("Chocolate"));
    }

    /** Tests rejection of null and blank search names. */
    public void testBlankNamesAreRejected() {
        try {
            stats.getFlavorCount(" ");
            fail("A blank flavor should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
        try {
            stats.getToppingCount(null);
            fail("A null topping name should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
    }

    /** Tests average order prices and the empty average. */
    public void testAveragePrice() {
        assertEquals(0.0, stats.getAverageOrderPrice(), 0.001);
        history.add(order(vanilla));
        history.add(order(vanilla,
                new Topping("Caramel", 1.00),
                new Topping("Caramel", 1.00),
                new Topping("Caramel", 1.00)));
        assertEquals(4.50, stats.getAverageOrderPrice(), 0.001);
    }

    /** Tests empty and populated summary output. */
    public void testSummary() {
        assertEquals("No orders yet.", stats.getSummary());
        history.add(order(vanilla, sprinkles));
        String summary = stats.getSummary();
        assertTrue(summary.contains("Total orders: 1"));
        assertTrue(summary.contains("Vanilla"));
        assertTrue(summary.contains("Sprinkles"));
        assertTrue(summary.contains("$3.50"));
    }
}
