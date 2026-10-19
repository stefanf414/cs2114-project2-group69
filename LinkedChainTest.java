import student.TestCase;

/** Tests the generic chain contract. */
public class LinkedChainTest extends TestCase {
    private LinkedChain<String> chain;

    public void setUp() {
        chain = new LinkedChain<String>();
    }

    public void testAddDuplicatesAndRemove() {
        chain.add("A");
        chain.add("A");
        chain.addToEnd("B");
        assertEquals(2, chain.count("A"));
        assertTrue(chain.remove("A"));
        assertEquals(1, chain.count("A"));
        assertEquals("A", chain.get(0));
        assertEquals("B", chain.get(1));
    }

    public void testNullAndArrayValidation() {
        try {
            chain.add(null);
            fail("Null entries must be rejected.");
        } catch (IllegalArgumentException expected) {
            // Expected.
        }
        try {
            chain.toArray(null);
            fail("Null arrays must be rejected.");
        } catch (IllegalArgumentException expected) {
            // Expected.
        }
    }
}
