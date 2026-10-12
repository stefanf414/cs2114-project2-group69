import student.TestCase;

/**
 * Tests RyanOrderListAssembler 
 */
public class RyanOrderListAssemblerTest extends TestCase {

    private RyanOrderListAssembler assembler;
    private LinkedChain<IceCreamOrder> history;
    private Cone cone;
    private Flavor flavor;
    private Topping sprinkles;
    private Topping fudge;

    /** 
    * Creates fresh test objects before each test. 
    */
    public void setUp() {
        history = new LinkedChain<IceCreamOrder>();
        assembler = new RyanOrderListAssembler(history);
        cone = new Cone("Waffle", 1.00);
        flavor = new Flavor("Vanilla", 2.00);
        sprinkles = new Topping("Sprinkles", 0.50);
        fudge = new Topping("Fudge", 0.75);
    }

    /** 
    * Tests initial assembler state. 
    */
    public void testConstructor() {
        assertFalse(assembler.hasCurrentOrder());
        assertNull(assembler.getCurrentOrder());
        assertEquals(0, assembler.getOrderCount());
        assertEquals(0, assembler.getHistoryArray().length);
    }

    /** 
    * Tests creation of a cone-and-flavor order.
    */
    public void testStartOrder() {
        assembler.startOrder(cone, flavor);
        assertTrue(assembler.hasCurrentOrder());
        assertNotNull(assembler.getCurrentOrder());
        assertEquals(3.00, assembler.getCurrentTotal(), 0.001);
    }

    /** 
    * Starting another order discards the unfinished one. 
    */
    public void testStartOrderAgain() {
        assembler.startOrder(cone, flavor);
        assembler.addTopping(sprinkles);
        assembler.startOrder(cone, flavor);
        assertEquals(0, assembler.getCurrentOrder().getToppingCount());
        assertEquals(0, assembler.getOrderCount());
    }

    /** 
    * Rejects a null cone or flavor. 
    */
    public void testStartOrderNull() {
        try {
            assembler.startOrder(null, flavor);
            fail("Expected IllegalArgumentException for null cone");
        }
        catch (IllegalArgumentException e) {
            
        }
        try {
            assembler.startOrder(cone, null);
            fail("Expected IllegalArgumentException for null flavor");
        }
        catch (IllegalArgumentException e) {
            
        }
    }

    /** 
    * Invalid start does not discard an existing valid current order.
    */
    public void testInvalidStartKeepsCurrentOrder() {
        assembler.startOrder(cone, flavor);
        IceCreamOrder original = assembler.getCurrentOrder();
        try {
            assembler.startOrder(null, flavor);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected.
        }
        assertSame(original, assembler.getCurrentOrder());
    }

    /** 
    * Adds multiple different toppings and updates the total. 
    */
    public void testAddTopping() {
        assembler.startOrder(cone, flavor);
        assertTrue(assembler.addTopping(sprinkles));
        assertTrue(assembler.addTopping(fudge));
        assertEquals(2, assembler.getCurrentOrder().getToppingCount());
        assertEquals(4.25, assembler.getCurrentTotal(), 0.001);
    }

    /** 
    * Counts and prices duplicate topping entries separately.
    */
    public void testAddDuplicateTopping() {
        assembler.startOrder(cone, flavor);
        assertTrue(assembler.addTopping(sprinkles));
        assertTrue(assembler.addTopping(sprinkles));
        assertEquals(2, assembler.getCurrentOrder().countOf(sprinkles));
        assertEquals(4.00, assembler.getCurrentTotal(), 0.001);
    }

    /** 
    * Cannot add a topping if an order is absent or the topping is null. 
    */
    public void testAddToppingInvalid() {
        assertFalse(assembler.addTopping(sprinkles));
        assembler.startOrder(cone, flavor);
        assertFalse(assembler.addTopping(null));
        assertEquals(0, assembler.getCurrentOrder().getToppingCount());
    }

    /** 
    * Removes exactly one copy of a duplicated topping.
    */
    public void testRemoveTopping() {
        assembler.startOrder(cone, flavor);
        assembler.addTopping(sprinkles);
        assembler.addTopping(sprinkles);
        assertTrue(assembler.removeTopping(sprinkles));
        assertEquals(1, assembler.getCurrentOrder().countOf(sprinkles));
        assertEquals(3.50, assembler.getCurrentTotal(), 0.001);
    }

    /** 
    * Removing a missing/null topping leaves the order intact. 
    */
    public void testRemoveMissingTopping() {
        assembler.startOrder(cone, flavor);
        assertFalse(assembler.removeTopping(fudge));
        assertFalse(assembler.removeTopping(null));
        assertEquals(0, assembler.getCurrentOrder().getToppingCount());
        assertEquals(3.00, assembler.getCurrentTotal(), 0.001);
    }

    /** 
    * Removing before an order is started returns false. 
    */
    public void testRemoveWithNoOrder() {
        assertFalse(assembler.removeTopping(sprinkles));
        assertFalse(assembler.removeTopping(null));
    }

    /** 
    * Total is zero without an order, otherwise includes selected items.
    */
    public void testGetCurrentTotal() {
        assertEquals(0.0, assembler.getCurrentTotal(), 0.001);
        assembler.startOrder(cone, flavor);
        assembler.addTopping(fudge);
        assertEquals(3.75, assembler.getCurrentTotal(), 0.001);
    }

    /** 
    * Submitting adds a completed order to the shared history. 
    */
    public void testSubmitOrder() {
        assembler.startOrder(cone, flavor);
        assembler.addTopping(sprinkles);
        IceCreamOrder submitted = assembler.getCurrentOrder();
        assertTrue(assembler.submitOrder());
        assertFalse(assembler.hasCurrentOrder());
        assertNull(assembler.getCurrentOrder());
        assertEquals(1, assembler.getOrderCount());
        assertSame(submitted, history.get(0));
    }

    /** 
    * A valid cone-and-flavor order needs no toppings to be submitted. 
    */
    public void testSubmitNoToppings() {
        assembler.startOrder(cone, flavor);
        assertTrue(assembler.submitOrder());
        assertEquals(1, assembler.getOrderCount());
    }

    /** 
    * Cannot submit when there is no current order. 
    */
    public void testSubmitNoOrder() {
        assertFalse(assembler.submitOrder());
        assertEquals(0, assembler.getOrderCount());
    }

    /**
    * Cancel discards the current order but not history. 
    */
    public void testCancelOrder() {
        assembler.startOrder(cone, flavor);
        assembler.submitOrder();
        assembler.startOrder(cone, flavor);
        assembler.addTopping(fudge);
        assembler.cancelOrder();
        assertFalse(assembler.hasCurrentOrder());
        assertEquals(1, assembler.getOrderCount());
        assertEquals(0.0, assembler.getCurrentTotal(), 0.001);
        assembler.cancelOrder(); // Repeating cancel is safe.
        assertEquals(1, assembler.getOrderCount());
    }

    /** 
    * History is newest first and returned as a separate array. 
    */
    public void testGetHistoryArray() {
        assembler.startOrder(cone, flavor);
        IceCreamOrder first = assembler.getCurrentOrder();
        assembler.submitOrder();

        assembler.startOrder(cone, flavor);
        assembler.addTopping(fudge);
        IceCreamOrder second = assembler.getCurrentOrder();
        assembler.submitOrder();

        IceCreamOrder[] orders = assembler.getHistoryArray();
        assertEquals(2, orders.length);
        assertSame(second, orders[0]);
        assertSame(first, orders[1]);
        orders[0] = null;
        assertSame(second, assembler.getHistoryArray()[0]);
    }

    /** 
    * Constructor rejects a null history. 
    */
    public void testNullHistory() {
        try {
            new RyanOrderListAssembler(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected.
        }
    }
}
