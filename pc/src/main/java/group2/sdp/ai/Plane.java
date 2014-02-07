
/** @author Jaroslaw Hirniak */

package group2.sdp.ai;

public class Plane
{

    private PointSet outline = null;
    
    public Plane()
    { outline = new PointSet(); }
    
    public boolean isWellFormed()
    { return outline.size() > 2; }
    
    public boolean contains(Point p)
    {
        outline.sort();
        // convex hull test for containment
        return false;
    }
    
}
