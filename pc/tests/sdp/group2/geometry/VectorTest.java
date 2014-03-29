package sdp.group2.geometry;

import junit.framework.TestCase;


public class VectorTest extends TestCase {

    public void testAngleDegrees() throws Exception {
        Vector a = new Vector(1.0, 0.0);
        Vector b = new Vector(0.0, 1.0);

        double deg = a.angleDegrees(b);
        System.out.println(deg);
        assertEquals(-90.0, deg);

        a = new Vector(-1.0, 0.0);
        b = new Vector(0.0, 1.0);

        deg = a.angleDegrees(b);
        System.out.println(deg);
        assertEquals(90.0, deg);

        a = new Vector(-1.0, 0.0);
        b = new Vector(-1.0, 1.0);

        deg = a.angleDegrees(b);
        System.out.println(deg);
        assertEquals(45.0, deg);
    }
}
