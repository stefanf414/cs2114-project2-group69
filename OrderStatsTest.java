import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for OrderStats: one normal and one bad-input case per public
 * method, following the spec's test plan (section 8.6).
 */
public class OrderStatsTest
{
    private static final Cone CONE = new Cone("Waffle", 1.00);
    private static final Flavor VANILLA = new Flavor("Vanilla", 2.00);
    private static final Flavor CHOCOLATE = new Flavor("Chocolate", 2.00);
    private static final Topping SPRINKLES = new Topping("Sprinkles", 0.50);
    private static final Topping FUDGE = new Topping("Fudge", 0.75);

    private LinkedChain<IceCreamOrder> history;
    private OrderStats stats;

    @BeforeEach
    public void setUp()
    {
        history = new LinkedChain<IceCreamOrder>();
        stats = new OrderStats(history);
    }

    private IceCreamOrder order(Flavor f, Topping... toppings)
    {
        IceCreamOrder o = new IceCreamOrder(CONE, f);
        for (Topping t : toppings)
        {
            o.addTopping(t);
        }
        return o;
    }

    // constructor
    @Test
    public void constructorAcceptsChain()
    {
        assertNotNull(new OrderStats(new LinkedChain<IceCreamOrder>()));
    }

    @Test
    public void constructorNullThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new OrderStats(null));
    }

    // hasOrders
    @Test
    public void hasOrdersTrueWithTwoOrders()
    {
        history.add(order(VANILLA));
        history.add(order(CHOCOLATE));
        assertTrue(stats.hasOrders());
    }

    @Test
    public void hasOrdersFalseWhenEmpty()
    {
        assertFalse(stats.hasOrders());
    }

    // getTotalOrders
    @Test
    public void totalOrdersThree()
    {
        history.add(order(VANILLA));
        history.add(order(VANILLA));
        history.add(order(CHOCOLATE));
        assertEquals(3, stats.getTotalOrders());
    }

    @Test
    public void totalOrdersEmptyIsZero()
    {
        assertEquals(0, stats.getTotalOrders());
    }

    // getMostPopularFlavor
    @Test
    public void mostPopularFlavorVanilla()
    {
        history.add(order(VANILLA));
        history.add(order(VANILLA));
        history.add(order(CHOCOLATE));
        assertEquals("Vanilla", stats.getMostPopularFlavor());
    }

    @Test
    public void mostPopularFlavorEmptyIsNull()
    {
        assertNull(stats.getMostPopularFlavor());
    }

    @Test
    public void mostPopularFlavorTieReturnsFirstSeen()
    {
        history.addToEnd(order(CHOCOLATE));
        history.addToEnd(order(VANILLA));
        assertEquals("Chocolate", stats.getMostPopularFlavor());
    }

    // getMostCommonTopping
    @Test
    public void mostCommonToppingCountsDuplicates()
    {
        history.add(order(VANILLA, SPRINKLES, SPRINKLES));
        history.add(order(CHOCOLATE, FUDGE));
        assertEquals("Sprinkles", stats.getMostCommonTopping());
    }

    @Test
    public void mostCommonToppingNoToppingsIsNull()
    {
        history.add(order(VANILLA));
        assertNull(stats.getMostCommonTopping());
    }

    // getFlavorCount
    @Test
    public void flavorCountTwoOfThree()
    {
        history.add(order(VANILLA));
        history.add(order(VANILLA));
        history.add(order(CHOCOLATE));
        assertEquals(2, stats.getFlavorCount("Vanilla"));
        assertEquals(2, stats.getFlavorCount("  vanilla "));
    }

    @Test
    public void flavorCountNeverOrderedIsZero()
    {
        history.add(order(VANILLA));
        assertEquals(0, stats.getFlavorCount("Mango"));
    }

    @Test
    public void flavorCountBlankOrNullThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> stats.getFlavorCount("  "));
        assertThrows(IllegalArgumentException.class,
            () -> stats.getFlavorCount(null));
    }

    // getToppingCount
    @Test
    public void toppingCountAcrossOrders()
    {
        history.add(order(VANILLA, SPRINKLES, SPRINKLES, SPRINKLES));
        history.add(order(CHOCOLATE, SPRINKLES));
        assertEquals(4, stats.getToppingCount("Sprinkles"));
    }

    @Test
    public void toppingCountUnknownIsZero()
    {
        history.add(order(VANILLA, SPRINKLES));
        assertEquals(0, stats.getToppingCount("Oreos"));
    }

    @Test
    public void toppingCountNullOrBlankThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> stats.getToppingCount(null));
        assertThrows(IllegalArgumentException.class,
            () -> stats.getToppingCount(""));
    }

    // getAverageOrderPrice
    @Test
    public void averageOrderPrice()
    {
        // 1.00 + 2.00 = 3.00 ; 3.00 + 3 x 1.00 toppings = 6.00 -> avg 4.50
        Topping dollar = new Topping("Caramel", 1.00);
        history.add(order(VANILLA));
        history.add(order(VANILLA, dollar, dollar, dollar));
        assertEquals(4.50, stats.getAverageOrderPrice(), 0.001);
    }

    @Test
    public void averageOrderPriceEmptyIsZero()
    {
        assertEquals(0.0, stats.getAverageOrderPrice(), 0.001);
    }

    // getSummary
    @Test
    public void summaryContainsKeyFacts()
    {
        history.add(order(VANILLA, SPRINKLES));
        String s = stats.getSummary();
        assertTrue(s.contains("Total orders: 1"));
        assertTrue(s.contains("Vanilla"));
        assertTrue(s.contains("Sprinkles"));
    }

    @Test
    public void summaryEmptyChain()
    {
        assertEquals("No orders yet.", stats.getSummary());
    }

    // shared-chain / read-only behavior
    @Test
    public void statsSeesLaterOrdersAndNeverChangesChain()
    {
        history.add(order(VANILLA));
        stats.getSummary();
        stats.getMostCommonTopping();
        assertEquals(1, history.size());
        history.add(order(CHOCOLATE));
        assertEquals(2, stats.getTotalOrders());
    }

    // LinkedChain reuse: same generic class, two element types
    @Test
    public void sameLinkedChainHoldsTwoElementTypes()
    {
        LinkedChain<Topping> toppings = new LinkedChain<Topping>();
        toppings.add(SPRINKLES);
        toppings.add(SPRINKLES);
        history.add(order(VANILLA));
        assertEquals(2, toppings.count(SPRINKLES));
        assertEquals(1, history.size());
    }
}
