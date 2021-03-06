package sdp.group2.geometry;

import java.awt.geom.Rectangle2D;


public class Rect extends Rectangle2D.Double implements Comparable<Rect> {

    private static final long serialVersionUID = 1L;

    public Rect(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public Point getCenter() {
        return new Point(this.getCenterX(), this.getCenterY());
    }

    @Override
    public String toString() {
        return String.format("Rect((%.2f, %.2f)(%.2f, %.2f))", x, y, width, height);
    }

    public Rect expand(int multiplier) {
        Point center = this.getCenter();
        double width = this.width * multiplier;
        double height = this.height * multiplier;
        return new Rect(
                center.x - (width / 2),
                center.y - (height / 2),
                width,
                height
        );
    }

    public boolean contains(Rect rect) {
        return (x <= rect.x && y <= y && (x + width) >= (rect.x + rect.width)
                && (y + height) >= (rect.y + rect.height));
    }

    @Override
    public int compareTo(Rect o) {
        if (this.x < o.x) {
            return -1;
        } else if (this.x == o.x) {
            return 0;
        } else {
            return 1;
        }
    }

}
