package sdp.group2.vision;

import junit.framework.TestCase;


/**
 * Created by mark on 22/02/2014.
 */
public class ImageTest extends TestCase {

    private Image currentImage = new Image();

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testGetRgbArray() throws Exception {

    }

    public void testSetRgbArray() throws Exception {

    }

    public void testGetColor() throws Exception {

    }

    public void testGetHSBColor() throws Exception {

    }

    public void testGetSize() throws Exception {

    }

    public void testSetSize() throws Exception {

    }

    public void testGetHSBColorPixelIterator() throws Exception {
        int count = 0;
        for (HSBColor color : currentImage.getHSBColorPixelIterator()) {
            count += 1;
        }
        assertEquals(count, currentImage.getRgbArray().length);
    }

    public void testGetColorPixelIterator() throws Exception {

    }
}
