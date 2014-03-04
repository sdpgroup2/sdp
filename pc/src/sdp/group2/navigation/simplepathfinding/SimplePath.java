package sdp.navigation.simplepathfinding;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

import sdp.geom.Circle2D;

import static sdp.geom.LineMethods.*;

public class SimplePath {
	private Rectangle mPitch;
	private static int MAX_DEPTH = 4;
	
	public SimplePath(Rectangle pitch){
		mPitch = pitch;
	}
	
	private Collection<Point2D> getTangentLinePoints(Point2D start, Circle2D c){
		return c.intersections(new Circle2D(start, start.distance(c.getCenter())));
	}
	
	private Collection<Point2D> getNewMidpoints(Circle2D object, Point2D start, Point2D end){

        //Length of the normal doesn't matter, as we assume that the lines are infinate
        Line2D normalLine = normalLine(new Line2D.Double(start, object.getCenter()), object.getCenter(), 10);
		
		Collection<Point2D> toReturn = new LinkedList<Point2D>();
		for ( Point2D point : getTangentLinePoints(start, object) ){
            //we have three lines. One from the center of the circle, normal to the line start, center of cirlce
            //and a line to the tanget point of the cirlce.
            //the new waypoint should be the intersection of the normal line and the line from the start to the tangent point
            Point2D newPoint = infiniteLineIntersections(new Line2D.Double(start, point), normalLine);
            if ( mPitch.contains(newPoint) ){
                toReturn.add(newPoint);
            }
		}
		return toReturn;
	}

	public ArrayList<Point> getPath(List<Circle2D> objects, Point start, Point end){
		ArrayList<List<Line2D>> allPaths = getPaths(objects, pointToPoint2d(start), pointToPoint2d(end), 0);
		
		List<Line2D> bestPath = bestPath(allPaths);
		if ( bestPath == null ){
			return null;
		}
		ArrayList<Point> toReturn = new ArrayList<Point>();
		toReturn.add(point2dToPoint(bestPath.get(0).getP1()));
		for ( Line2D line : bestPath ){
			toReturn.add(point2dToPoint(line.getP2()));
		}
		return toReturn;
	}
	
	private Point point2dToPoint(Point2D p){
		return new Point((int)p.getX(), (int)p.getY());
	}
	
	private Point2D pointToPoint2d(Point p){
		return new Point2D.Double(p.getX(), p.getY());
	}
	
	private List<Line2D> bestPath(Collection<List<Line2D>> paths){
		double minDist = Double.MAX_VALUE;
		List<Line2D> bestLine = null;
		for ( List<Line2D> path : paths ){
			double dist = 0;
			for ( Line2D line : path ){
				dist += line.getP1().distanceSq(line.getP2());
			}
			if ( dist < minDist ){
				minDist = dist;
				bestLine = path;
			}
			
		}
		return bestLine;
	}
	
	public ArrayList<List<Line2D>> getPaths(List<Circle2D> objects, Point2D start, Point2D end, int depth){
		
		if ( depth == MAX_DEPTH ){
			return new ArrayList<List<Line2D>>();
		}
		
		Line2D possibleLine = new Line2D.Double(start, end);
		
		for ( Circle2D r : objects ){
			if ( r.intersects(possibleLine) ){
				//get the new midpoint of the line
				Collection<Point2D> newPoints = getNewMidpoints(r, start, end);
				
				//for all paths, add the path that already have (start to midpoint)
				ArrayList<List<Line2D>> paths = new ArrayList<List<Line2D>>();
				for ( Point2D newPoint : newPoints ){
					ArrayList<List<Line2D>> pathsFromNewPoint = getPaths(objects,
																		 newPoint,
																		 end,
																		 depth+1);
					
					if ( pathsFromNewPoint.size() == 0 ){
						continue;
					}
					
					//add the line that we already have
					for ( List<Line2D> path : pathsFromNewPoint ){
						path.add(0, new Line2D.Double(start, newPoint));
					}
					
					paths.addAll(pathsFromNewPoint);
				}
				return paths;
			}
		}
				
		ArrayList<List<Line2D>> paths = new ArrayList<List<Line2D>>();
		ArrayList<Line2D> path = new ArrayList<Line2D>();
		path.add(possibleLine);
		paths.add(path);
		
		return paths;
	}
}
