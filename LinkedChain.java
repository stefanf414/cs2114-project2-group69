/**
 * Generic singly linked chain that stores non-null entries and permits
 * duplicates.
 *
 * @param <T> type of entry stored in the chain
 */
public class LinkedChain<T> {
    private Node<T> firstNode;
    private int numberOfEntries;

    /** Creates an empty chain. */
    public LinkedChain() {
        firstNode = null;
        numberOfEntries = 0;
    }

    /** Adds an entry to the front. */
    public void add(T entry) {
        checkEntry(entry);
        firstNode = new Node<T>(entry, firstNode);
        numberOfEntries++;
    }

    /** Adds an entry to the end. */
    public void addToEnd(T entry) {
        checkEntry(entry);
        Node<T> newNode = new Node<T>(entry, null);
        if (firstNode == null) {
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

    /** Removes and returns the first entry, or null when empty. */
    public T remove() {
        if (firstNode == null) {
            return null;
        }
        T result = firstNode.data;
        firstNode = firstNode.next;
        numberOfEntries--;
        return result;
    }

    /** Removes the first entry equal to the supplied value. */
    public boolean remove(T entry) {
        if (entry == null || firstNode == null) {
            return false;
        }
        if (firstNode.data.equals(entry)) {
            remove();
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

    /** Returns the number of entries equal to the supplied value. */
    public int count(T entry) {
        if (entry == null) {
            return 0;
        }
        int result = 0;
        for (Node<T> current = firstNode; current != null; current = current.next) {
            if (current.data.equals(entry)) {
                result++;
            }
        }
        return result;
    }

    /** Returns whether the chain contains the supplied value. */
    public boolean contains(T entry) {
        return count(entry) > 0;
    }

    /** Returns the entry at a zero-based index. */
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

    /** Copies the entries into the supplied array. */
    public T[] toArray(T[] target) {
        if (target == null || target.length < numberOfEntries) {
            throw new IllegalArgumentException("Array is null or too small.");
        }
        int index = 0;
        for (Node<T> current = firstNode; current != null; current = current.next) {
            target[index++] = current.data;
        }
        if (target.length > numberOfEntries) {
            target[numberOfEntries] = null;
        }
        return target;
    }

    /** Returns the number of entries, including duplicates. */
    public int size() {
        return numberOfEntries;
    }

    /** Returns true when the chain has no entries. */
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    /** Removes every entry. */
    public void clear() {
        firstNode = null;
        numberOfEntries = 0;
    }

    /** Rejects null entries before they are added. */
    private void checkEntry(T entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Entries cannot be null.");
        }
    }

    /** One node in the singly linked chain. */
    private static class Node<E> {
        private final E data;
        private Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }
}
