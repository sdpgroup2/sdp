
/** @author Jaroslaw Hirniak */

package sdp.group2.util;

/**
 * Heapsort.
 * O(n * log(n)) sorting method.
 */
public class Sort
{
    
    public static <T extends Comparable<T>> void sort(T[] a) 
    { sort(a, a.length); }
    
    /**
     * @param N - number of elements in the array, useful for sorting arrays
     * where not all items are meaningful, like in resizing array implementation
     * of PointSet
     */
    public static <T extends Comparable<T>> void sort(T[] a, int N) 
    {
        if (a == null)
        { throw new NullPointerException(); }
        
        if (N < 0)
        { 
            throw new NegativeArraySizeException("Expected number of "
                + "elements N to be non-negative value, but was " 
                + String.valueOf(N) + ".");
        }
        
        if (N <= 1) // nothing to be sorted
        { return; }
        
        N--; // first element starts at index 0
        
        for (int k = N / 2; k >= 0; k--)
        { sink(a, k, N); }

        while (N > 0)
        {
            exch(a, 0, N--);
            sink(a, 0, N);
        }
    }

    /* swim method for completeness :-) 
    private static <T extends Comparable<T>> void swim(T[] a, int k, int N)
    {
        while (k > 1 && less(a, k / 2, k))
        {
            exch(a, k / 2, k);
            k /= 2;
        }
    }
    */

    private static <T extends Comparable<T>> void sink(T[] a, int k, int N)
    {
        while (2 * k <= N)
        {
            int j = 2 * k;
            if (j < N && less(a, j, j + 1)) j++;
            if (!less(a, k, j)) break;
            exch(a, k, j);
            k = j;
        }
    }

    private static <T extends Comparable<T>> boolean less(T[] a, int i, int j)
    { return a[i].compareTo(a[j]) == -1; }

    private static <T extends Comparable<T>> void exch(T[] a, int i, int j)
    {
        T tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

}
