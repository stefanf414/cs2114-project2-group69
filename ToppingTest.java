import student.TestCase;

/** Tests every public behavior of Topping. */
public class ToppingTest extends TestCase {
    /** Tests valid construction, trimming, and accessors. */
    public void testValidConstructionAndAccessors() {
        Topping topping = new Topping("  Sprinkles  ", 0.50);
        assertEquals("Sprinkles", topping.getName());
        assertEquals(0.50, topping.getPrice(), 0.001);
    }

    /** Tests that a zero price is valid. */
    public void testZeroPriceIsAllowed() {
        Topping topping = new Topping("Free topping", 0.0);
        assertEquals(0.0, topping.getPrice(), 0.001);
    }

    /** Tests rejection of null, empty, and whitespace-only names. */
    public void testInvalidNamesAreRejected() {
        String[] names = { null, "", "   " };
        for (String name : names) {
            try {
                new Topping(name, 0.50);
                fail("Invalid names should be rejected.");
            }
            catch (IllegalArgumentException expected) {
                // Expected exception.
            }
        }
    }

    /** Tests rejection of negative, NaN, and infinite prices. */
    public void testInvalidPricesAreRejected() {
        double[] prices = {
            -0.01,
            Double.NaN,
            Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY
        };
        for (double price : prices) {
            try {
                new Topping("Fudge", price);
                fail("Invalid prices should be rejected.");
            }
            catch (IllegalArgumentException expected) {
                // Expected exception.
            }
        }
    }

    /** Tests equality, symmetry, identity, and equal hash codes. */
    public void testEqualsAndHashCode() {
        Topping first = new Topping("Fudge", 0.75);
        Topping second = new Topping("Fudge", 0.75);
        assertTrue(first.equals(first));
        assertTrue(first.equals(second));
        assertTrue(second.equals(first));
        assertEquals(first.hashCode(), second.hashCode());
    }

    /** Tests inequality for different and unrelated values. */
    public void testDifferentValuesAreNotEqual() {
        Topping topping = new Topping("Fudge", 0.75);
        assertFalse(topping.equals(new Topping("Sprinkles", 0.75)));
        assertFalse(topping.equals(new Topping("Fudge", 0.50)));
        assertFalse(topping.equals(null));
        assertFalse(topping.equals("Fudge"));
    }

    /** Tests formatted display output. */
    public void testToString() {
        assertEquals("Chocolate Chips ($0.76)",
                new Topping("Chocolate Chips", 0.755).toString());
    }
}
