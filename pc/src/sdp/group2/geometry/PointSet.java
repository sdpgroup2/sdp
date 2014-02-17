
/** @author Jaroslaw Hirniak */

package sdp.group2.geometry;

/**
 * PointSet:
 *   - there are no duplicates of points,
 *   - resizing (dynamic) array implementation, guaranteed O(1) amortised time.
 */

import sdp.group2.util.PrettyPrint;
import sdp.group2.util.Sort;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
// import java.util.Random; if we want to generate random points for testing
import java.util.Scanner;

public class PointSet implements Comparable<PointSet>
{

    private Point[] points; // resizing array
    private int     lo, hi;
    private boolean sorted;
    private boolean sortable = true;

    public PointSet()
    {
        points = new Point[8];
        lo = hi = 0;
        this.sorted = true; // empty is sorted
    }
    
    public PointSet(boolean sortable)
    {
    	this();
    	this.sortable = sortable;
    }

    public PointSet(Point[] points)
    {
        this.points = points;
        this.lo = 0;
        this.hi = points.length;
        this.sorted = false;
    }
    
    public PointSet(Point[] points, int lo, int hi, boolean sorted)
    {
        this.points = points;
        this.lo = lo;
        this.hi = hi;
        this.sorted = sorted;
    }

    /**
     * Read points from file
     * 
     * @param file (relative or absolute path to text file)
     */
    public PointSet(String file)
    {
        this();
        read(file);
    }

    /**
     * Creates a subset of PointSet, private because should be invoke only
     * indirectly via subset method, please check [subset] for more information.
     */
    private PointSet(PointSet pointSet, int lo, int hi)
    {
        //this.points = pointSet.getPoints();
        this.points = pointSet.points;
        this.lo = lo;
        this.hi = hi;
    }

    /** Creates a physical copy of the point set (i.e. copies points array). */
    public PointSet clone()
    {
        Point[] pointsCopy = new Point[points.length];
        
        for (int i = 0; i < points.length; i++)
        { pointsCopy[i] = points[i]; }
        
        return new PointSet(pointsCopy, lo, hi, sorted);
    }
    
    public PointSet clone(int lo, int hi)
    {
        Point[] pointsCopy = new Point[hi - lo];
        
        for (int i = lo; i < hi; i++)
        { pointsCopy[i - lo] = points[i]; }
        
        return new PointSet(pointsCopy, 0, hi - lo, sorted);
    }

    /**
     * Create subset view into PointSet.
     * 
     * @param left inclusive (all elements from this one)
     * @param right exclusive (all elements before this one)
     */
    public PointSet subset(int left, int right)
    {
        int lo = this.lo + left;
        int hi = this.lo + right;
        
        if (this.lo > lo || this.hi < hi)
        { 
            String message = String.format("Expected subset, namely set[%d:%d),"
                    + " to be within the set bounds, i.e. [%d, %d), but was"
                    + " not.", lo, hi, this.lo, this.hi);
            throw new ArrayIndexOutOfBoundsException(message);
        }
        
        sort();
        return new PointSet(this, lo, hi);
    }

    public void add(Point p)
    {
        if (!contains(p))
        { addUnsafe(p); }
    }

    /**
     * Unsafe because as the result duplicates can be added. Used only when
     * loading from file to optimise loading time (to avoid checking if element
     * is contained for each loaded value). Loading function should remove all
     * duplicates or raise an error in case there are any.
     */
    private void addUnsafe(Point p)
    {
        // if not enough space resize the array
        if (hi == points.length)
        { resize(2 * points.length); }

        points[hi++] = p;

        if (hi > 1)
        { sorted = false; }
    }

    /**
     * Time complexity: O (n * log(n)) if unsorted, otherwise O(n).
     * Space complexity: O(n) for the created copy of the points array.
     * where n - the size of points array.
     */
    private void removeDuplicates()
    {
        if (!(hi < 2))
        {
            sort();

            PointSet ps = new PointSet();
            ps.addUnsafe(points[0]);

            // All points are sorted a priori, hence all duplicates are
            // adjacent.
            // Therefore to remove duplicates it is enough to check only
            // adjacent values in the sorted array.
            // Takes time proportional to O(n).
            for (int i = 1; i < hi; i++)
            {
                if (!points[i - 1].equals(points[i]))
                { ps.addUnsafe(points[i]); }
            }

            points = ps.getPoints();
            lo = ps.lo;
            hi = ps.hi;
        }
    }

    /**
     * Returns ith point from the sorted order. Takes O (n * log(n)) if the
     * array is not sorted, O(1) otherwise.
     * 
     * @throws ArrayIndexOutOfBoundsException if index i > size of array - 1.
     */
    public Point get(int i)
    {
        sort();

        if (i >= 0 && i < hi - lo)
        { return points[lo + i]; }
        
        else
        {
            throw new ArrayIndexOutOfBoundsException("Expected index i to be"
                    + "within bounds [" + 0 + ", " + (hi - lo) + "), but was "
                    + i + ".");
        }
    }

    public Point left()
    { return points[lo]; }

    public Point right()
    { return points[hi - 1]; }
    
    /**
     * @return array of sorted points without duplicates
     */
    public Point[] getPoints()
    {
        sort();
        Point[] p = new Point[size()];
        for (int i = lo; i < hi; i++)
        { p[i - lo] = points[i]; }
        return p;
    }
    
    public int size()
    { return hi - lo; }

    public void sort()
    {
        if (!sorted)
        {
            Sort.sort(points, size());
            sorted = true;
        }
    }

    /**
     * Resize an existing array to have size of max elements. To guarantee
     * amortised O(1) performance it is advised to double the size every time
     * the array reaches its full capacity.
     */
    private void resize(int max)
    {
        Point[] temp = new Point[max];
        
        for (int i = 0; i < hi; i++)
        { temp[i] = points[i]; }
        
        points = temp;
    }

    /**
     * If the array is sorted then it uses binary search tree method to find an
     * element in the array, with time proportional to O(log(n)), n is the size
     * of array. Otherwise, it uses linear search which takes time proportional
     * to O(n).
     */
    public boolean contains(Point p)
    {
        if (sorted)
        { return binarySearchTreeContains(p); }
        
        else
        {
            for (int i = lo; i < hi; i++)
            { if (points[i].equals(p)) { return true; } }
            return false;
        }
    }

    /**
     * Binary search tree method to check if an array contains point p. Takes
     * time proportional to O(log(n)), where n is the size of array.
     */
    private boolean binarySearchTreeContains(Point p)
    {
        int lo = this.lo,  hi = this.hi - 1;
        
        while (lo <= hi)
        {
            int mid = lo + (hi - lo) / 2;
            
            if (p.compareTo(points[mid]) < 0)
            { hi = mid - 1; }
            
            else if (p.compareTo(points[mid]) > 0)
            { lo = mid + 1; }
            
            else
            { return true; }
        }
        
        return false;
    }

    /** Parsing from a text file, recognises points coordinates */
    private boolean isWellFormed(String line)
    { return isWellFormed(line, 10); }
    
    private boolean isWellFormed(String line, int radix)
    {
        Scanner sc = new Scanner(line.trim());
        
        if (!sc.hasNextDouble()) { return false; }
        sc.nextDouble();
        if (!sc.hasNextDouble()) { return false; }
        sc.nextDouble();
        
        return !sc.hasNextDouble();
    }
    
    private Point parseLine(String line)
    {
        Scanner sc = new Scanner(line.trim());
        
        double x = sc.nextDouble();
        double y = sc.nextDouble();
        
        return new Point(x, y);
    }
    
    /**
     * Read and parse points from ASCII text file. Points should be represented
     * as a pair of digits, each point per line.
     */
    public void read(String file)
    {
        BufferedReader fs = null;
        
        try
        {
            fs = new BufferedReader(new FileReader(file));

            String line;
            
            while ((line = fs.readLine()) != null)
            {
                if (isWellFormed(line))
                { addUnsafe(parseLine(line)); }
            }
            
            removeDuplicates();
        }
        
        catch (FileNotFoundException e)
        { e.printStackTrace(); }
        
        catch (IOException e)
        { e.printStackTrace(); }
        
        finally
        {
            try
            {
                if (fs != null)
                { fs.close(); }
            }
            
            catch (IOException e)
            { e.printStackTrace(); }
        }
    }
    
    /** Save current point set as pairs of digit, each pair per one line. */
    public void save(String file)
    {
        PrettyPrint pp = new PrettyPrint();
        
        pp.evaluateWidth(this);
        pp.setColumnsNumber(2);
        pp.appendTitle("Point Set");
        pp.appendRow("x", "y");
        sort();
        for (Point p : getPoints())
        { pp.appendRow(p.getX(), p.getY()); }
        
        pp.save(file);
    }
    
    /** For plotting, intended to show AI part on output from camera. */
    public double[][] getXY()
    {
        double[][] xy = new double[2][size()];
        
        for (int i = lo; i < hi; i++)
        {
            xy[0][i - lo] = (double) points[i].getX();
            xy[1][i - lo] = (double) points[i].getY();
        }
        
        return xy;
    }
    
    public boolean less(PointSet other)
    { return this.compareTo(other) == -1; }
    
    @Override
    public int compareTo(PointSet other)
    {
        if (this.size() < other.size()) { return -1; }
        else if (this.size() == other.size()) { return 0; }
        else { return 1; }
    }
    
    public boolean isWithinTimestamp(int i, long significantTime)
    { 
    	if (size() < 2)
    	{ return true; }
    	else
    	{ return right().getTimestamp() - get(i).getTimestamp() < significantTime; }
    }
    
}
