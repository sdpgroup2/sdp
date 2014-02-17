package sdp.group2.world;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.Rect;

public interface StaticObject {

	public Point getPosition();

	public void setPosition(Point pt);

	public Rect getBoundingRect();

}
