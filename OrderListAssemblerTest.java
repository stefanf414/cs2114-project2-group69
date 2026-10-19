import student.TestCase;

/** Tests current-order construction and completed-order history. */
public class OrderListAssemblerTest extends TestCase {
    public void testSubmitNewestFirst() {
        LinkedChain<IceCreamOrder> history =
                new LinkedChain<IceCreamOrder>();
        OrderListAssembler assembler = new OrderListAssembler(history);
        Cone cone = new Cone("Cup", 0.0);
        Flavor flavor = new Flavor("Vanilla", 3.0);

        assembler.startOrder(cone, flavor);
        IceCreamOrder first = assembler.getCurrentOrder();
        assertTrue(assembler.submitOrder());

        assembler.startOrder(cone, flavor);
        IceCreamOrder second = assembler.getCurrentOrder();
        assertTrue(assembler.submitOrder());

        assertSame(second, assembler.getHistoryArray()[0]);
        assertSame(first, assembler.getHistoryArray()[1]);
        assertFalse(assembler.submitOrder());
    }

    public void testNullAndCancel() {
        try {
            new OrderListAssembler(null);
            fail("Null history must be rejected.");
        } catch (IllegalArgumentException expected) {
            // Expected.
        }
        OrderListAssembler assembler = new OrderListAssembler(history());
        assembler.cancelOrder();
        assertFalse(assembler.hasCurrentOrder());
    }

    private LinkedChain<IceCreamOrder> history() {
        return new LinkedChain<IceCreamOrder>();
    }
}
