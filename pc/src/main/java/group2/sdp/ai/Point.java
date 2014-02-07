
/** @author Jaroslaw Hirniak */

package group2.sdp.ai;

/**
 * 2D Point. 
 * Implements lexicographical order.
 */

public class Point implements Comparable<Point>
{

    private double x, y;

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    { return x; }

    public double getY()
    { return y; }
    
    public void setX(double x)
    { this.x = x; }
    
    public void setY(double y)
    { this.y = y; }

    /**
     * Computes square distance from this to other point.
     * 
     * @param other - point to which distance is measured to from this one
     * @return Square value of distance between this and other point
     */
    public double getDistance(Point other)
    {
        return Math.sqrt(
               (this.x - other.x) * (this.x - other.x) 
             + (this.y - other.y) * (this.y - other.y)
             );
    }

    public boolean less(Point other)
    { return this.compareTo(other) == -1; }
    
    @Override
    public boolean equals(Object other)
    { return this.compareTo((Point) other) == 0; }

    @Override
    public String toString()
    { return "(" + x + ", " + y + ")"; }

    @Override
    public int compareTo(Point other)
    {
        if (this.x < other.getX())
        { return -1; }
        
        else if (this.x == other.x && this.y < other.y)
        { return -1; }

        else if (this.x == other.x && this.y == other.y)
        { return 0; }

        else
        { return 1; }
    }

}
