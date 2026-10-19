import student.TestCase;

/** Tests order contents, duplicate toppings, and pricing. */
public class IceCreamOrderTest extends TestCase {
    public void testDuplicatesRemovalAndPrice() {
        IceCreamOrder order = new IceCreamOrder(
                new Cone("Waffle Cone", 1.00),
                new Flavor("Vanilla", 2.00));
        Topping topping = new Topping("Sprinkles", 0.50);
        order.addTopping(topping);
        order.addTopping(topping);
        assertEquals(2, order.countOf(topping));
        assertEquals(4.00, order.getTotalPrice(), 0.001);
        assertTrue(order.removeTopping(topping));
        assertEquals(1, order.countOf(topping));
    }

    public void testFlavorAccessorAndDisplay() {
        IceCreamOrder order = new IceCreamOrder(
                new Cone("Cup", 0.00), new Flavor("Chocolate", 3.25));
        assertEquals("Chocolate", order.getFlavor().getName());
        assertTrue(order.toString().contains("Total: $3.25"));
    }
}
