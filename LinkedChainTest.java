import student.TestCase;

/** Tests every public behavior of LinkedChain. */
public class LinkedChainTest extends TestCase {
    private LinkedChain<String> chain;

    /** Creates an empty chain before each test. */
    public void setUp() {
        chain = new LinkedChain<String>();
    }

    /** Tests initial size and empty status. */
    public void testConstructor() {
        assertTrue(chain.isEmpty());
        assertEquals(0, chain.size());
    }

    /** Tests front insertion, end insertion, and indexing. */
    public void testAddAndAddToEnd() {
        chain.add("Vanilla");
        chain.add("Chocolate");
        chain.addToEnd("Strawberry");
        assertEquals("Chocolate", chain.get(0));
        assertEquals("Vanilla", chain.get(1));
        assertEquals("Strawberry", chain.get(2));
        assertEquals(3, chain.size());
    }

    /** Tests duplicate entries, counting, and containment. */
    public void testDuplicatesAndCount() {
        chain.add("Sprinkles");
        chain.add("Sprinkles");
        assertEquals(2, chain.count("Sprinkles"));
        assertTrue(chain.contains("Sprinkles"));
        assertFalse(chain.contains("Fudge"));
    }

    /** Tests removing the front and a matching value. */
    public void testRemove() {
        chain.addToEnd("A");
        chain.addToEnd("B");
        assertEquals("A", chain.remove());
        assertTrue(chain.remove("B"));
        assertFalse(chain.remove("missing"));
        assertTrue(chain.isEmpty());
        assertNull(chain.remove());
    }

    /** Tests valid and invalid indexes. */
    public void testGetAndInvalidIndex() {
        chain.add("Vanilla");
        assertEquals("Vanilla", chain.get(0));
        try {
            chain.get(-1);
            fail("Negative index should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
        try {
            chain.get(1);
            fail("Index equal to size should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
    }

    /** Tests typed array conversion and clearing. */
    public void testToArrayAndClear() {
        chain.addToEnd("Vanilla");
        chain.addToEnd("Chocolate");
        String[] result = chain.toArray(new String[3]);
        assertEquals("Vanilla", result[0]);
        assertEquals("Chocolate", result[1]);
        assertNull(result[2]);
        chain.clear();
        assertTrue(chain.isEmpty());
    }

    /** Tests invalid array arguments. */
    public void testInvalidArray() {
        chain.add("Vanilla");
        try {
            chain.toArray(null);
            fail("A null array should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
        try {
            chain.toArray(new String[0]);
            fail("A small array should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
    }

    /** Tests the class's null-entry behavior. */
    public void testNullsAreRejected() {
        try {
            chain.add(null);
            fail("Null entries should be rejected.");
        }
        catch (IllegalArgumentException expected) {
            // Expected exception.
        }
        assertFalse(chain.contains(null));
        assertFalse(chain.remove(null));
        assertEquals(0, chain.count(null));
    }
}
