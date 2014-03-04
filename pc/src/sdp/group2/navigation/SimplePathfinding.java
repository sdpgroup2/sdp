package sdp.navigation;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import sdp.geom.Circle2D;
import sdp.gui.MainWindow;
import sdp.navigation.simplepathfinding.SimplePath;
import sdp.vision.Circle;
import sdp.vision.Drawable;
import sdp.vision.ImageProcessor;
import sdp.vision.DrawableLine;
import sdp.vision.WorldState;

public class SimplePathfinding implements Pathfinding{
	
	private static int ROBOT_RADIUS = 35;
	private static int BALL_RADIUS = 10;
	
	private Point mTarget = null;
	private SimplePath mPathGenerator;
	private WorldState mWorldState;
	private boolean mAvoidBall = true;
	private boolean mAvoidOpponent = true;
	private Rectangle mPitch = null;
	
	private boolean mHasPath = true;
	
	public boolean hasPath(){
		return mHasPath;
	}
	
	public SimplePathfinding(WorldState ws){
		mPitch = new Rectangle(ROBOT_RADIUS,
				               ROBOT_RADIUS,
							   (int)ws.getPitchWidth() - ROBOT_RADIUS,
							   (int)ws.getPitchHeight() - ROBOT_RADIUS);
		mPathGenerator = new SimplePath(mPitch);
		mWorldState = ws;
	}

	@Override
	public ArrayList<Point> getPath() {
		if ( mTarget == null ){
			mHasPath = false;
			return null;
		}
		
		ArrayList<Drawable> drawables = new ArrayList<Drawable>();
		
		Point topLeft = new Point((int)mPitch.getMinX(), (int)mPitch.getMinY());
		Point bottomLeft = new Point((int)mPitch.getMinX(), (int)mPitch.getMaxY());
		Point topRight = new Point((int)mPitch.getMaxX(), (int)mPitch.getMinY());
		Point bottomRight = new Point((int)mPitch.getMaxX(), (int)mPitch.getMaxY());
		drawables.add(new DrawableLine(Color.BLUE, topLeft, topRight));
		drawables.add(new DrawableLine(Color.BLUE, topRight, bottomRight));
		drawables.add(new DrawableLine(Color.BLUE, bottomRight, bottomLeft));
		drawables.add(new DrawableLine(Color.BLUE, bottomLeft, topLeft));
		
		int robotRadius = ROBOT_RADIUS + ROBOT_RADIUS;
		int ballRadius = BALL_RADIUS + ROBOT_RADIUS;
		ArrayList<Circle2D> objects = new ArrayList<Circle2D>();
		
		if (mAvoidOpponent) {
			objects.add(new Circle2D(mWorldState.getOppositionPosition(), robotRadius));
			drawables.add(new Circle(Color.BLUE, mWorldState.getOppositionPosition(), robotRadius));
		}
		
		if ( mAvoidBall ){
			objects.add(new Circle2D(mWorldState.getBallPoint(), ballRadius));
			drawables.add(new Circle(Color.BLUE, mWorldState.getBallPoint(), ballRadius));
		}
		
		ArrayList<Point> path = mPathGenerator.getPath(objects,
													   mWorldState.getOurPosition(),
													   mTarget);
		
		if ( path != null ){
			drawables.add(new DrawableLine(Color.WHITE, path.get(0), path.get(1)));
			for ( int i = 0; i < path.size() - 1; ++i ){
				drawables.add(new DrawableLine(Color.WHITE, path.get(i), path.get(i+1)));
			}
		}
		
		MainWindow.addOrUpdateDrawable("pathfinding", drawables);
		
		return path;
	}

	@Override
	public void setAvoidBall(boolean avoidBall) {
		mAvoidBall = avoidBall;
	}

	@Override
	public void setTarget(Point target) {
		mTarget = target;
	}

	public void setAvoidOpponent(boolean avoidOpponent) {
		mAvoidOpponent = avoidOpponent;
	}
}
