package sdp.group2.vision;

import java.awt.Color;
import java.awt.Dimension;
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
     * Returns the color at indices x and y wrapper in Color class.
     * @param x index along x axis
     * @param y index along y axis
     * @return color wrapped in Color class
     */
    public Color getColor(int x, int y) {
        int index = y * getSize().width + x;
        return new Color(rgbArray[index]);
    }

    /**
     * Returns the color at indices x and y wrapper in HSBColor class.
     * @param x index along x axis
     * @param y index along y axis
     * @return color wrapped in HSBColor class
     */
    public HSBColor getHSBColor(int x, int y) {
        int index = y * getSize().width + x;
        return new HSBColor(rgbArray[index]);
    }

    /**
     * Sets the color at the specified index.
     * @param index index of the int array
     * @param color color to be set
     */
    public void setColor(int index, Color color) {
        rgbArray[index] = color.getRGB();
    }

    /**
     * Sets the color at the specified index.
     * @param x index along x axis
     * @param y index along y axis
     * @param color color to be set
     */
    public void setColor(int x, int y, Color color) {
        int index = y * getSize().width + x;
        setColor(index, color);
    }

    /**
     * Sets the color at the specified index.
     * @param index index of the int array
     * @param color color to be set
     */
    public void setHSBColor(int index, HSBColor color) {
        rgbArray[index] = color.getRGB();
    }

    /**
     * Sets the color at the specified index.
     * @param x index along x axis
     * @param y index along y axis
     * @param color color to be set
     */
    public void setHSBColor(int x, int y, HSBColor color) {
        int index = y * getSize().width + x;
        setHSBColor(index, color);
    }

    /**
     * Returns the size of the image as a Dimension.
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
