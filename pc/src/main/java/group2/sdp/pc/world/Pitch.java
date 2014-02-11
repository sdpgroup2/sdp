package group2.sdp.pc.world;

import group2.sdp.pc.geom.*;
import group2.sdp.pc.strategy.Zone;


public class Pitch extends Plane implements IPitch {
	
	private Zone[] zones = new Zone[4];
	private Ball ball = new Ball();
	private boolean even;
	
	public Pitch()
	{ super("Pitch"); }

	@Override
	public void setZone(byte id, PointSet ps) {
		for (int i = 0; i < ps.size(); i++)
		{ zones[id].addPoint(ps.get(i)); }
	}

	@Override
	public void setAlly(boolean even)
	{ this.even = even; }

	@Override
	public void updateBallPosition(Point p)
	{ ball.updatePoisition(p); }

	@Override
	public void updateRobotState(byte id, Point p, double theta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPitch getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte getBallZone() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isAllyEven() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTachoToPixelValue(double ratio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getTachoToPixelRatio() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setToTachoTranslation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetToTachoTranslation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Zone getZone(byte id) {
		return zones[id];
	}

}
