import student.TestCase;

/** Tests every public behavior of OrderListAssembler. */
public class OrderListAssemblerTest extends TestCase {
    private OrderListAssembler assembler;
    private LinkedChain<IceCreamOrder> history;
    private Cone cone;
    private Flavor flavor;
    private Topping sprinkles;
    private Topping fudge;

    /** Creates fresh test objects before each test. */
    public void setUp() {
        history = new LinkedChain<IceCreamOrder>();
        assembler = new OrderListAssembler(history);
        cone = new Cone("Waffle", 1.00);
        flavor = new Flavor("Vanilla", 2.00);
        sprinkles = new Topping("Sprinkles", 0.50);
        fudge = new Topping("Fudge", 0.75);
    }

    /** Tests the initial assembler state. */
    public void testConstructor() {
        assertFalse(assembler.hasCurrentOrder());
        assertNull(assembler.getCurrentOrder());
        assertEquals(0, assembler.getOrderCount());
        assertEquals(0, assembler.getHistoryArray().length);
    }

    /** Tests starting an order and its base price. */
    public void testStartOrder() {
        assembler.startOrder(cone, flavor);
        assertTrue(assembler.hasCurrentOrder());
        assertNotNull(assembler.getCurrentOrder());
        assertEquals(3.00, assembler.getCurrentTotal(), 0.001);
    }

    /** Tests that starting again replaces only the unfinished order. */
    public void testStartOrderAgain() {
        assembler.startOrder(cone, flavor);
        assembler.addTopping(sprinkles);
        assembler.startOrder(cone, flavor);
        assertEquals(0, assembler.getCurrentOrder().getToppingCount());
        assertEquals(0, assembler.getOrderCount());
    }

    /** Tests rejection of missing cone or flavor values. */
    public void testStartOrderNull() {
        try {
            assembler.startOrder(null, flavor);
            fail("A null cone should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
        try {
            assembler.startOrder(cone, null);
            fail("A null flavor should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
    }

    /** Tests adding normal and duplicate toppings. */
    public void testAddToppings() {
        assembler.startOrder(cone, flavor);
        assertTrue(assembler.addTopping(sprinkles));
        assertTrue(assembler.addTopping(sprinkles));
        assertTrue(assembler.addTopping(fudge));
        assertEquals(2,
                assembler.getCurrentOrder().countOf(sprinkles));
        assertEquals(4.75, assembler.getCurrentTotal(), 0.001);
    }

    /** Tests add behavior without an order and with a null topping. */
    public void testAddToppingInvalid() {
        assertFalse(assembler.addTopping(sprinkles));
        assembler.startOrder(cone, flavor);
        assertFalse(assembler.addTopping(null));
        assertEquals(0, assembler.getCurrentOrder().getToppingCount());
    }

    /** Tests removing one duplicate topping. */
    public void testRemoveTopping() {
        assembler.startOrder(cone, flavor);
        assembler.addTopping(sprinkles);
        assembler.addTopping(sprinkles);
        assertTrue(assembler.removeTopping(sprinkles));
        assertEquals(1,
                assembler.getCurrentOrder().countOf(sprinkles));
        assertEquals(3.50, assembler.getCurrentTotal(), 0.001);
    }

    /** Tests removal when no matching topping or order exists. */
    public void testRemoveInvalid() {
        assertFalse(assembler.removeTopping(sprinkles));
        assembler.startOrder(cone, flavor);
        assertFalse(assembler.removeTopping(fudge));
        assertFalse(assembler.removeTopping(null));
    }

    /** Tests submitting an order to shared history. */
    public void testSubmitOrder() {
        assembler.startOrder(cone, flavor);
        IceCreamOrder submitted = assembler.getCurrentOrder();
        assertTrue(assembler.submitOrder());
        assertFalse(assembler.hasCurrentOrder());
        assertSame(submitted, history.get(0));
        assertEquals(1, assembler.getOrderCount());
        assertFalse(assembler.submitOrder());
    }

    /** Tests canceling without changing completed history. */
    public void testCancelOrder() {
        assembler.startOrder(cone, flavor);
        assembler.submitOrder();
        assembler.startOrder(cone, flavor);
        assembler.cancelOrder();
        assertFalse(assembler.hasCurrentOrder());
        assertEquals(1, assembler.getOrderCount());
    }

    /** Tests newest-first history and defensive array conversion. */
    public void testGetHistoryArray() {
        assembler.startOrder(cone, flavor);
        IceCreamOrder first = assembler.getCurrentOrder();
        assembler.submitOrder();
        assembler.startOrder(cone, flavor);
        IceCreamOrder second = assembler.getCurrentOrder();
        assembler.submitOrder();

        IceCreamOrder[] orders = assembler.getHistoryArray();
        assertSame(second, orders[0]);
        assertSame(first, orders[1]);
        orders[0] = null;
        assertSame(second, assembler.getHistoryArray()[0]);
    }

    /** Tests rejection of a null shared history. */
    public void testNullHistory() {
        try {
            new OrderListAssembler(null);
            fail("A null history should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
    }
}
