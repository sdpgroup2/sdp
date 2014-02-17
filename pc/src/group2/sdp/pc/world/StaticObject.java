package group2.sdp.pc.world;

import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;

public interface StaticObject {

	public Point getPosition();

	public void setPosition(Point pt);

	public Rect getBoundingRect();

}
