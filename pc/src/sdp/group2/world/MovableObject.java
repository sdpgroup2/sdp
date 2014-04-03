package sdp.group2.world;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import sdp.group2.geometry.Point;
import sdp.group2.pc.MasterController;
import sdp.group2.util.Constants;
import sdp.group2.util.Constants.PitchType;

public abstract class MovableObject {

//	private Point position;
	private List<Point> posHistory = new LinkedList<Point>();

	public MovableObject(Point position) {
		posHistory.add(position);
//		this.position = position;
	}
	
	public Point getPosition() {
		return posHistory.get(posHistory.size() - 1);
//		return position;
	}

	public void setPosition(Point position) {
		posHistory.add(position);
		if (posHistory.size() > Constants.HISTORY_SIZE) {
			posHistory.remove(0);
		}
//		this.position = position;
	}
	
	public boolean isStable() {
		double maxX = 0;
		double maxY = 0;
		double minX = Integer.MAX_VALUE;
		double minY = Integer.MAX_VALUE;
		for(int i=0; i < Constants.STABLE_HISTORY_SIZE; i++) {
			Point point = posHistory.get(i);
			if(point.x > maxX) {
				maxX = point.x;
			}
			if(point.x < minX) {
				minX = point.x;
			}
			if(point.y > maxY) {
				maxY = point.y;
			}
			if(point.y < minY) {
				minY = point.y;
			}
		}
		// Finds the euclidean distance between the min and max coordinates in the queue
		double dist = new Point(maxX, maxY).distance(minX, minY);
//		double dist = Math.sqrt(Math.pow((maxX-minX),2) - Math.pow((maxY-minY),2));
//		System.out.println(dist);
		if (dist > Constants.STABLE_DISTANCE) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean enteringZone(int zone) {
		double min, max;
		int[] lines;
		if (MasterController.pitchPlayed == PitchType.MAIN) {
			lines = Constants.MAIN_LINES;
		} else {
			lines = Constants.SIDE_LINES;
		}

		min = zone == 0 ? 0 : lines[zone - 1];
		max = zone == 3 ? Integer.MAX_VALUE : lines[zone];

		int pointCount = posHistory.size() - 1;
		int framesInZone = 0;
		for (int i = pointCount; i >= 0; i--) {
			double x = posHistory.get(i).x;
			if (x >= min && x <= max) {
				framesInZone++;
			} else {
				break;
			}
		}
		return framesInZone > 1 && framesInZone <= 25;
	}
	
	public void printHistory() {
		for(int i=0;i<posHistory.size();i++) {
			System.out.println(i + ": " + posHistory.get(i)+ " ");
		}
	}
}
