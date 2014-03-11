package sdp.group2.geometry;

import junit.framework.TestCase;


/**
 * Created by mark on 11/03/2014.
 */
public class RectTest extends TestCase {

    private Rect rect;

    @Override
    protected void setUp() throws Exception {
        this.rect = new Rect(0.0, 5.0, 10.0, 20.0);
    }

    public void testGetCenter() throws Exception {
        Point center = rect.getCenter();
        assertEquals(new Point(5.0, 15.0), center);
    }

    public void testExpand() throws Exception {
    }

    public void testContains() throws Exception {
        Point pt = new Point(3.0, 8.0);
        assertTrue(rect.contains(pt));
    }
}
