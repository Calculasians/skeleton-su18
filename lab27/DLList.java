
/* A doubly-linked list of integers supporting various sorting algorithms. */
public class DLList<T extends Comparable<T>> {

    private class Node {
        T item;
        Node prev;
        Node next;

        Node(T item) {
            this.item = item;
            this.prev = this.next = null;
        }

        Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    /* The sentinel of this DLList. */
    Node sentinel;
    /* The number of items in this DLList. */
    int size;

    /* Constructs an empty DLList. */
    public DLList() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        this.size = 0;
    }

    /* Creates a copy of DLList represented by LST. */
    public DLList(DLList<T> lst) {
        Node ptr = lst.sentinel.next;
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        this.size = 0;
        while (ptr != lst.sentinel) {
            addLast(ptr.item);
            ptr = ptr.next;
        }
    }

    /* Returns true if this DLList is empty. Otherwise, returns false. */
    public boolean isEmpty() {
        return size == 0;
    }

    /* Adds a new node with item ITEM to the front of this DLList. */
    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    /* Adds a new node with item ITEM to the end of this DLList. */
    public void addLast(T item) {
        Node newNode = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    /* Removes the Node referenced by N from this DLList. */
    private void remove(Node n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
        n.next = null;
        n.prev = null;
        size -= 1;
    }

    @Override
    public String toString() {
        String s = "";
        for (Node ptr = sentinel.next; ptr != sentinel; ptr = ptr.next) {
            s = s + ptr.item + " ";
        }
        return s;
    }

    /* Returns the result of sorting the values in this DLList using insertion
       sort. Does not modify this DLList. */
    public DLList<T> insertionSort() {
        DLList<T> toReturn = new DLList<>();
        for (Node ptr = sentinel.next; ptr != sentinel; ptr = ptr.next) {
            toReturn.insertionSortHelper(ptr.prev);
        }
        toReturn = this;
        return toReturn;
    }

    /* Inserts the item of Node N into this DLList such that the values of this
       DLList are in increasing order. */
    private void insertionSortHelper(Node n) {
        if (n.next.item != null) {
            while (n.item != null && n.item.compareTo(n.next.item) > 0) {
                n.prev.next = n.next;
                n.next.prev = n.prev;
                n.next.next.prev = n;
                n.next = n.prev.next.next;
                n.prev.next.next = n;
                n.prev = n.prev.next;

                n = n.prev.prev;
            }
        }
    }

    /* Returns the result of sorting the values in this DLList using selection
       sort. Does not modify this DLList. */
    public DLList<T> selectionSort() {
        DLList<T> copy = new DLList<>(this);
        DLList<T> toReturn = new DLList<>();
        while (copy.size != 0) {
            Node min = copy.sentinel.next;
            for (Node p = copy.sentinel.next; p != copy.sentinel; p = p.next) {
                if (p.item.compareTo(min.item) < 0) {
                    min = p;
                }
            }
            toReturn.addLast(min.item);
            copy.remove(min);
        }
        return toReturn;
    }

    /* Returns the result of sorting the values in this DLList using merge sort.
       Does not modify this DLList. */
    public DLList<T> mergesort() {
        if (size <= 1) {
            return this;
        }
        DLList<T> copy = new DLList<>(this);
        DLList<T> oneHalf = new DLList<>();
        DLList<T> otherHalf = new DLList<>();

        int i;
        for (i = 0; i < this.size / 2; i++) {
            T toAdd = copy.sentinel.next.item;
            oneHalf.addLast(toAdd);
            copy.sentinel.next = copy.sentinel.next.next;
        }
        //System.out.println("this is the first half" + oneHalf);

        for (int j = i; j < this.size; j++) {
            T toAdd = copy.sentinel.next.item;
            otherHalf.addLast(toAdd);
            copy.sentinel.next = copy.sentinel.next.next;
        }
        //System.out.println("this is the second half" + otherHalf);

        return oneHalf.mergesort().merge(otherHalf.mergesort());
    }

    /* Returns the result of merging this DLList with LST. Does not modify the
       two DLLists. Assumes that this DLList and LST are in sorted order. */
    private DLList<T> merge(DLList<T> lst) {
        DLList<T> toReturn = new DLList<T>();
        Node thisPtr = sentinel.next;
        Node lstPtr = lst.sentinel.next;
        while (thisPtr != sentinel && lstPtr != lst.sentinel) {
            if (thisPtr.item.compareTo(lstPtr.item) < 0) {
                toReturn.addLast(thisPtr.item);
                thisPtr = thisPtr.next;
            } else {
                toReturn.addLast(lstPtr.item);
                lstPtr = lstPtr.next;
            }
        }
        while (thisPtr != sentinel) {
            toReturn.addLast(thisPtr.item);
            thisPtr = thisPtr.next;
        }
        while (lstPtr != lst.sentinel) {
            toReturn.addLast(lstPtr.item);
            lstPtr = lstPtr.next;
        }
        return toReturn;
    }

    /* Returns the result of sorting the values in this DLList using quick sort.
       Does not modify this DLList. */
    public DLList<T> quicksort() {
        if (size <= 1) {
            return this;
        }
        // Assume first element is the divider.
        DLList<T> copy = new DLList<>(this);
        DLList<T> smallElements = new DLList<>();
        DLList<T> largeElements = new DLList<>();
        T divider = sentinel.next.item;
        DLList<T> justTheDivider = new DLList<>();
        justTheDivider.addLast(divider);

        for (int i = 0; i < copy.size; i++) {
            if (copy.sentinel.next.next.item == null) {
                break;
            }
            if (copy.sentinel.next.next.item.compareTo(divider) > 0) {
                largeElements.addLast(copy.sentinel.next.next.item);
            } else {
                smallElements.addLast(copy.sentinel.next.next.item);
            }
            copy.sentinel.next.next = copy.sentinel.next.next.next;
        }
        /*
        System.out.println("divider: " + divider);
        System.out.println("smaller: " + smallElements);
        System.out.println("larger: " + largeElements);
        */
        DLList<T> toReturn = new DLList<>();
        toReturn.append(smallElements.quicksort());
        toReturn.addLast(divider);
        toReturn.append(largeElements.quicksort());

        return toReturn;
    }

    /* Appends LST to the end of this DLList. Does modify the original
       DLList. */
    public void append(DLList<T> lst) {
        if (lst.isEmpty()) {
            return;
        }
        if (isEmpty()) {
            sentinel = lst.sentinel;
            size = lst.size;
            return;
        }
        sentinel.prev.next = lst.sentinel.next;
        lst.sentinel.next.prev = sentinel.prev;
        sentinel.prev = lst.sentinel.prev;
        lst.sentinel.prev.next = sentinel;
        size += lst.size;
    }

    /* Returns a random integer between 0 and 99. */
    private static int randomInt() {
        return (int) (100 * Math.random());
    }

    private static DLList<Integer> generateRandomIntegerDLList(int N) {
        DLList<Integer> toReturn = new DLList<>();
        for (int k = 0; k < N; k++) {
            toReturn.addLast((int) (100 * Math.random()));
        }
        return toReturn;
    }

    public static void main(String[] args) {
        DLList values;
        DLList sortedValues;

        System.out.print("Before selection sort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.selectionSort();
        System.out.print("After selection sort: ");
        System.out.println(sortedValues);

        System.out.print("Before insertion sort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.insertionSort();
        System.out.print("After insertion sort: ");
        System.out.println(sortedValues);

        System.out.print("Before merge sort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.mergesort();
        System.out.print("After merge sort: ");
        System.out.println(sortedValues);

        System.out.print("Before quicksort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.quicksort();
        System.out.print("After quicksort: ");
        System.out.println(sortedValues);
    }
}
