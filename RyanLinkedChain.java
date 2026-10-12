/**
 * Implementation of a generic linked chain.
 * Accepts duplicate entries.
 *
 *
 * @param <T> the type of object stored in this chain
 */
public class RyanLinkedChain<T> {

    private Node<T> firstNode;
    private int numberOfEntries;

    /** 
    * Creates an empty chain. 
    */
    public RyanLinkedChain() {
        firstNode = null;
        numberOfEntries = 0;
    }

    /**
     * Inserts one entry at the front of the chain.
     *
     * @param entry object to add
     * @throws IllegalArgumentException if entry is null
     */
    public void add(T entry) {
        checkEntry(entry);
        firstNode = new Node<T>(entry, firstNode);
        numberOfEntries++;
    }

    /**
     * Inserts one entry at the end of the chain.
     *
     * @param entry object to add
     * @throws IllegalArgumentException if entry is null
     */
    public void addToEnd(T entry) {
        checkEntry(entry);
        Node<T> newNode = new Node<T>(entry, null);
        if (isEmpty()) {
            firstNode = newNode;
        }
        else {
            Node<T> current = firstNode;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        numberOfEntries++;
    }

    /**
     * Removes and returns the first entry in the chain.
     *
     * @return the removed entry, or null when the chain is empty
     */
    public T remove() {
        if (isEmpty()) {
            return null;
        }
        T removed = firstNode.data;
        firstNode = firstNode.next;
        numberOfEntries--;
        return removed;
    }

    /**
     * Removes one occurrence of an entry equal to the given entry.
     *
     * @param entry object to remove
     * @return true if an entry was removed, false if not found or null
     */
    public boolean remove(T entry) {
        if (entry == null || isEmpty()) {
            return false;
        }

        if (firstNode.data.equals(entry)) {
            firstNode = firstNode.next;
            numberOfEntries--;
            return true;
        }

        Node<T> current = firstNode;
        while (current.next != null) {
            if (current.next.data.equals(entry)) {
                current.next = current.next.next;
                numberOfEntries--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Counts how many entries equal the given entry.
     *
     * @param entry object to count
     * @return the count, or 0 if entry is null
     */
    public int count(T entry) {
        if (entry == null) {
            return 0;
        }
        int count = 0;
        Node<T> current = firstNode;
        while (current != null) {
            if (current.data.equals(entry)) {
                count++;
            }
            current = current.next;
        }
        return count;
    }

    /**
     * @param entry object to look for
     * @return true if at least one equal entry is stored
     */
    public boolean contains(T entry) {
        if (entry == null) {
            return false;
        }
        Node<T> current = firstNode;
        while (current != null) {
            if (current.data.equals(entry)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Gets an entry by 0-based index without removing it.
     *
     * @param index position, starting at zero
     * @return the entry at the requested index
     * @throws IllegalArgumentException if index is out of range
     */
    public T get(int index) {
        if (index < 0 || index >= numberOfEntries) {
            throw new IllegalArgumentException("Index is out of range.");
        }
        Node<T> current = firstNode;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Copies entries, in chain order, into a caller-provided array.
     *
     * @param target array of the correct type and sufficient size
     * @return the same target array, populated with chain entries
     * @throws IllegalArgumentException if target is null or too small
     */
    public T[] toArray(T[] target) {
        if (target == null || target.length < numberOfEntries) {
            throw new IllegalArgumentException("Array is null or too small.");
        }
        Node<T> current = firstNode;
        int i = 0;
        while (current != null) {
            target[i] = current.data;
            current = current.next;
            i++;
        }
        if (target.length > numberOfEntries) {
            target[numberOfEntries] = null;
        }
        return target;
    }

    /** @return the number of stored entries, including duplicates */
    public int size() {
        return numberOfEntries;
    }

    /** 
    * @return true if the chain has no entries 
    */
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    /** 
    * Removes all stored entries. 
    */
    public void clear() {
        firstNode = null;
        numberOfEntries = 0;
    }

    /** 
    * Rejects entries that cannot be stored in this chain. 
    */
    private void checkEntry(T entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Entries cannot be null.");
        }
    }

    /** 
    * One private node of the singly linked chain. 
    */
    private static class Node<E> {
        private E data;
        private Node<E> next;

        private Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }
}
