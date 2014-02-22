package sdp.group2.vision;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;


/**
 * Wrapper class for the image we get from the video feed.
 */
public class Image {

    private int[] rgbArray;
    private Dimension size;


    public int[] getRgbArray() {
        return rgbArray;
    }

    public void setRgbArray(int[] rgbArray) {
        this.rgbArray = rgbArray;
    }

    /**
     * Clears the image - sets everything to black.
     */
    public void clear() {
        for (int i = 0; i < rgbArray.length; i++) {
            setColor(i, Color.BLACK);
        }
    }

    /**
     * Returns the color at indices x and y as an int
     *
     * @param index index of the int array
     * @return RGB color as an int
     */
    public int getRGB(int index) {
        return rgbArray[index];
    }

    /**
     * Returns the color at indices x and y as an int
     *
     * @param x index along x axis
     * @param y index along y axis
     * @return RGB color as an int
     */
    public int getRGB(int x, int y) {
        int index = y * getSize().width + x;
        return getRGB(index);
    }

    /**
     * Returns the color at indices x and y wrapped in HSBColor class.
     *
     * @param index index of the int array
     * @return color wrapped in HSBColor class
     */
    public Color getColor(int index) {
        return new Color(rgbArray[index]);
    }

    /**
     * Returns the color at indices x and y wrapped in Color class.
     *
     * @param x index along x axis
     * @param y index along y axis
     * @return color wrapped in Color class
     */
    public Color getColor(int x, int y) {
        int index = y * getSize().width + x;
        return getColor(index);
    }

    /**
     * Returns the color at indices x and y wrapped in HSBColor class.
     *
     * @param index index of the int array
     * @return color wrapped in HSBColor class
     */
    public HSBColor getHSBColor(int index) {
        return new HSBColor(rgbArray[index]);
    }

    /**
     * Returns the color at indices x and y wrapped in HSBColor class.
     *
     * @param x index along x axis
     * @param y index along y axis
     * @return color wrapped in HSBColor class
     */
    public HSBColor getHSBColor(int x, int y) {
        int index = y * getSize().width + x;
        return getHSBColor(index);
    }

    /**
     * Sets the color at the specified index.
     *
     * @param index index of the int array
     * @param color color to be set
     */
    public void setColor(int index, Color color) {
        rgbArray[index] = color.getRGB();
    }

    /**
     * Sets the color at the specified index.
     *
     * @param x     index along x axis
     * @param y     index along y axis
     * @param color color to be set
     */
    public void setColor(int x, int y, Color color) {
        int index = y * getSize().width + x;
        setColor(index, color);
    }

    /**
     * Sets the color at the specified index.
     *
     * @param index index of the int array
     * @param color color to be set
     */
    public void setHSBColor(int index, HSBColor color) {
        rgbArray[index] = color.getRGB();
    }

    /**
     * Sets the color at the specified index.
     *
     * @param x     index along x axis
     * @param y     index along y axis
     * @param color color to be set
     */
    public void setHSBColor(int x, int y, HSBColor color) {
        int index = y * getSize().width + x;
        setHSBColor(index, color);
    }

    /**
     * Normalises the image so that it's not affected by lighting conditions.
     */
    public void normaliseImage(float meanBright, float meanSat) {
        // Colours work on PC4 where meanSat = 0.11869 and meanBright = 0.15539
        for (int i = 0; i < rgbArray.length; i++) {
            HSBColor color = getHSBColor(i);
            color.offset(0, color.s - meanSat, color.b - meanBright);
            setHSBColor(i, color);
        }
    }

    public BufferedImage getBufferedImage() {
        int width = getSize().width;
        int height = getSize().height;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, width, height, rgbArray, 0, width);
        return image;
    }

    /**
     * Returns the size of the image as a Dimension.
     *
     * @return dimension of the image
     */
    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    /**
     * Returns an iterator that iterates through the pixels in an x-first
     * y-second order.
     *
     * @return iterator
     */
    public PixelIterator<HSBColor> getHSBColorPixelIterator() {
        return new PixelIterator<HSBColor>() {
            @Override
            public HSBColor next() {
                HSBColor color = getHSBColor(x, y);
                if (x + 1 == getSize().width) {
                    /* When x reaches the last column we go to the next row
                     * and reset x to 0. */
                    y++;
                    x = 0;
                } else {
                    /* Otherwise we just increment x.
                     * */
                    x++;
                }
                return color;
            }
        };
    }

    /**
     * Returns an iterator that iterates through the pixels in an x-first
     * y-second order.
     *
     * @return iterator
     */
    public PixelIterator<Color> getColorPixelIterator() {
        return new PixelIterator<Color>() {
            @Override
            public Color next() {
                Color color = getColor(x, y);
                if (x + 1 == getSize().width) {
                    /* When x reaches the last column we go to the next row
                     * and reset x to 0. */
                    y++;
                    x = 0;
                } else {
                    /* Otherwise we just increment x.
                     * */
                    x++;
                }
                return color;
            }
        };
    }

    /**
     * Returns an iterator that iterates through the pixels in an x-first
     * y-second order.
     */
    public abstract class PixelIterator<T> implements Iterable<T>, Iterator<T> {
        protected int x;
        protected int y;

        @Override
        public Iterator<T> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return x < getSize().width && y < getSize().height;
        }

        @Override
        public abstract T next();

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
