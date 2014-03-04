package sdp.navigation;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import sdp.gui.MainWindow;
import sdp.navigation.pathfinding.AStarRun;
import sdp.navigation.pathfinding.Node;
import sdp.vision.Circle;
import sdp.vision.Drawable;
import sdp.vision.DrawableRectangle;
import sdp.vision.ImageProcessor;
import sdp.vision.WorldState;
import sdp.vision.DrawableLine;

public class AStarPathfinding implements Pathfinding{
	WorldState mWorldState;
	
	private boolean mAvoidBall = true;
	
	final int mAstarGridHeight = 58;
	final int mAstarGridWidth = 28;	
	
	private final int ROBOT_COST = 1000;
	private final int ROBOT_WIDTH = 100;
	
	private final int BALL_SIZE = 35;
	private final int BALL_COST = 100;

	private Point mTarget;

	public AStarPathfinding(WorldState state){
		mWorldState = state;
	}
	
	public boolean avoidBall(){
		return mAvoidBall;
	}
	
	public void setAvoidBall(boolean avoidBall){
		mAvoidBall = avoidBall;
	}
	

	private Point cordToNode(Point p){
		return new Point((int)((((double)p.x)/mWorldState.getPitchWidth()) * (double)mAstarGridWidth),
				         (int)((((double)p.y)/mWorldState.getPitchHeight())* (double)mAstarGridHeight)); 
	}

	private Point nodeToCord(Node p) {
		return new Point((int)((((double)p.x)/(double)mAstarGridWidth) * mWorldState.getPitchWidth()),
		         (int)((((double)p.y)/(double)mAstarGridHeight) * mWorldState.getPitchHeight())); 
	}

	
	public synchronized void setTarget(Point target){
		mTarget = target != null ? (Point)target.clone() : null;
	}
	
	public synchronized Point getTarget(){
		return mTarget;
	}
	
	private ArrayList<Node> getOppositionPoints(){
		ArrayList<Node> oppositionPoints = new ArrayList<Node>();
		

		Point boxSize = cordToNode(new Point(ROBOT_WIDTH/2, ROBOT_WIDTH/3));
		
		for ( int x = cordToNode(mWorldState.getOppositionPosition()).x - boxSize.x;
				   x < cordToNode(mWorldState.getOppositionPosition()).x + boxSize.x;
				   ++x ){
			for ( int y = cordToNode(mWorldState.getOppositionPosition()).y - boxSize.y;
					  y < cordToNode(mWorldState.getOppositionPosition()).y + boxSize.y;
					  ++y ){
				int weight = ROBOT_COST;
				
				Node n = new Node(new Point(x + 1, y), weight);
				n.setOpposition(true);
				oppositionPoints.add(n);
			}
		}
		return oppositionPoints;
	}
	
	private ArrayList<Node> getBallPoints(){
		ArrayList<Node> ballPoints = new ArrayList<Node>();
		for ( int x = mWorldState.getBallX() - BALL_SIZE; x < mWorldState.getBallX() + BALL_SIZE ; ++x ){
			for ( int y = mWorldState.getBallY() - BALL_SIZE; y < mWorldState.getBallY() + BALL_SIZE; ++y ){
				Node n = new Node(cordToNode(new Point(x, y)), BALL_COST);
				n.setBall(true);
				ballPoints.add(n);
			}
		}
		return ballPoints;
	}
	
	private DrawableRectangle drawableForObject(ArrayList<Node> obj){
		int maxX = Integer.MIN_VALUE,
		maxY = Integer.MIN_VALUE,
		minX = Integer.MAX_VALUE,
		minY = Integer.MAX_VALUE;
	
		for ( Node n : obj ){
			Point p = nodeToCord(n);
			maxX = Math.max(p.x, maxX);
			maxY = Math.max(p.y, maxY);
			minX = Math.min(p.x, minX);
			minY = Math.min(p.y, minY);
		}
		
		return new DrawableRectangle(Color.WHITE,
											new Point(minX, minY),
											maxX - minX,
											maxY - minY);
	}
	
	public ArrayList<Point> getPath(){
		if (getTarget() == null) {
			return new ArrayList<Point>();
		}
		ArrayList<Drawable> drawables = new ArrayList<Drawable>();

		ArrayList<Node> oppositionPoints = getOppositionPoints();
		ArrayList<Node> ballPoints 		 = avoidBall() ? getBallPoints() : new ArrayList<Node>();
		
		drawables.add(drawableForObject(oppositionPoints));
		if ( avoidBall() ){
			drawables.add(drawableForObject(ballPoints));

		}
			
		AStarRun run = new AStarRun(mAstarGridWidth, mAstarGridHeight,
									new Node(cordToNode(new Point(mWorldState.getOurX(), mWorldState.getOurY()))),
									new Node(cordToNode(new Point(getTarget().x, getTarget().y))),
									ballPoints,
									oppositionPoints);
			
		ArrayList<Point> path = new ArrayList<Point>();
		for ( Node n : run.getPath() ){
			path.add(nodeToCord(n));
		}
			
		for ( int i = 0; i < path.size() - 1; ++i ){
			drawables.add(new DrawableLine(Color.WHITE, path.get(i), path.get(i+1)));
		}
			
		MainWindow.addOrUpdateDrawable("pathfinding", drawables);
		return path;
	}

	@Override
	public boolean hasPath() {
		return true;
	}
	 
}
