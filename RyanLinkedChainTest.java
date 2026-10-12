import student.TestCase;

/** 
* Tests stand-alone reference linked-chain implementation. 
*/
public class RyanLinkedChainTest extends TestCase {

    private RyanLinkedChain<String> chain;

    /** 
    * Makes a fresh chain before each test. 
    */
    public void setUp() {
        chain = new RyanLinkedChain<String>();
    }

    /** 
    * A new chain has no entries. 
    */
    public void testConstructor() {
        assertTrue(chain.isEmpty());
        assertEquals(0, chain.size());
    }

    /** 
    * add puts the newest entry at index zero. 
    */
    public void testAdd() {
        chain.add("Vanilla");
        chain.add("Chocolate");
        assertEquals(2, chain.size());
        assertEquals("Chocolate", chain.get(0));
        assertEquals("Vanilla", chain.get(1));
    }

    /** 
    * addToEnd preserves the original entries' positions. 
    */
    public void testAddToEnd() {
        chain.addToEnd("Vanilla");
        chain.addToEnd("Chocolate");
        assertEquals("Vanilla", chain.get(0));
        assertEquals("Chocolate", chain.get(1));
        assertEquals(2, chain.size());
    }

    /** 
    * Equal entries are not deduplicated. 
    */
    public void testDuplicates() {
        chain.add("Sprinkles");
        chain.add("Sprinkles");
        assertEquals(2, chain.size());
        assertEquals(2, chain.count("Sprinkles"));
    }

    /** 
    * remove() removes and returns the front entry. 
    */
    public void testRemoveFirst() {
        chain.add("Vanilla");
        chain.add("Chocolate");
        assertEquals("Chocolate", chain.remove());
        assertEquals("Vanilla", chain.get(0));
        assertEquals(1, chain.size());
    }

    /** 
    * remove(T) removes a matching entry at the front. 
    */
    public void testRemoveByValueAtFront() {
        chain.add("Vanilla");
        chain.add("Chocolate");
        assertTrue(chain.remove("Chocolate"));
        assertEquals("Vanilla", chain.get(0));
    }

    /** 
    * remove(T) also works in the middle and at the end. 
    */
    public void testRemoveByValueLater() {
        chain.addToEnd("A");
        chain.addToEnd("B");
        chain.addToEnd("C");
        assertTrue(chain.remove("B"));
        assertEquals("C", chain.get(1));
        assertTrue(chain.remove("C"));
        assertEquals(1, chain.size());
        assertEquals("A", chain.get(0));
    }

    /** 
    * Only one duplicate is removed for each call. 
    */
    public void testRemoveDuplicate() {
        chain.add("Sprinkles");
        chain.add("Sprinkles");
        assertTrue(chain.remove("Sprinkles"));
        assertEquals(1, chain.count("Sprinkles"));
    }

    /** 
    * Removing an absent/null value must not change size. 
    */
    public void testRemoveMissing() {
        chain.add("Vanilla");
        assertFalse(chain.remove("Chocolate"));
        assertFalse(chain.remove(null));
        assertEquals(1, chain.size());
    }

    /** 
    * Empty chains can be removed from safely. 
    */
    public void testRemoveEmpty() {
        assertNull(chain.remove());
        assertFalse(chain.remove("Vanilla"));
    }

    /** 
    * count includes duplicates but not nonmatching values. 
    */
    public void testCount() {
        chain.add("Vanilla");
        chain.add("Vanilla");
        chain.add("Chocolate");
        assertEquals(2, chain.count("Vanilla"));
        assertEquals(1, chain.count("Chocolate"));
        assertEquals(0, chain.count("Strawberry"));
        assertEquals(0, chain.count(null));
    }

    /** 
    * contains reports whether a matching entry exists. 
    */
    public void testContains() {
        chain.add("Vanilla");
        assertTrue(chain.contains("Vanilla"));
        assertFalse(chain.contains("Chocolate"));
        assertFalse(chain.contains(null));
    }

    /** 
    * get uses zero-based indexing.
    */
    public void testGet() {
        chain.add("Vanilla");
        chain.add("Chocolate");
        assertEquals("Chocolate", chain.get(0));
        assertEquals("Vanilla", chain.get(1));
    }

    /** 
    * Invalid positive and negative indexes are rejected.
    */
    public void testGetInvalidIndex() {
        try {
            chain.get(5);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected.
        }
        try {
            chain.get(-1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    /** 
    * Copies data into the supplied typed array. 
    */
    public void testToArray() {
        chain.add("Vanilla");
        chain.add("Chocolate");
        String[] target = new String[2];
        String[] result = chain.toArray(target);
        assertSame(target, result);
        assertEquals("Chocolate", result[0]);
        assertEquals("Vanilla", result[1]);
    }

    /** 
    * An oversized array has a null at the first unused position. 
    */
    public void testToArrayLargeArray() {
        chain.add("Vanilla");
        String[] result = chain.toArray(new String[3]);
        assertEquals("Vanilla", result[0]);
        assertNull(result[1]);
    }

    /** 
    * A too-small array or null array is invalid.
    */
    public void testToArrayInvalid() {
        chain.add("Vanilla");
        chain.add("Chocolate");
        try {
            chain.toArray(new String[1]);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected.
        }
        try {
            chain.toArray(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    /** 
    * Neither insertion method accepts null values. 
    */
    public void testAddNull() {
        try {
            chain.add(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            
        }
        try {
            chain.addToEnd(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
         
        }
        assertTrue(chain.isEmpty());
    }

    /** 
    * Clears all values and resets the size. 
    */
    public void testClear() {
        chain.add("Vanilla");
        chain.add("Chocolate");
        chain.clear();
        assertTrue(chain.isEmpty());
        assertEquals(0, chain.size());
        assertNull(chain.remove());
    }
}
