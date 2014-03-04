package sdp.navigation;

import java.awt.Point;
import java.util.ArrayList;

public interface Pathfinding {
	public ArrayList<Point> getPath();
	public void setAvoidBall(boolean avoidBall);
	public void setTarget(Point target);
	public boolean hasPath();
}
