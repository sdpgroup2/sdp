package group2.sdp.pc.ai;

import group2.sdp.pc.geom.Line;
import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.PointSet;
import group2.sdp.pc.strategy.Zone;
import group2.sdp.pc.world.IPitch;

public class DefensivePlanner extends Planner {

	private Zone defenseZone;
	
	public DefensivePlanner(IPitch pitch, byte zoneId)
	{ 
		super(pitch);
		this.defenseZone = getPitch().getZone(zoneId);
	}

	public void intercept()
	{
		// if (defenseZone.contains())
	}
	
	public Line recognizeDanger()
	{
		PointSet trajectory = getPitch().getTrajectory();
		for (int i = 1; i < trajectory.size(); i++)
		{
			Point p0 = trajectory.get(i - 1);
			Point p1 = trajectory.get(i);
			if (defenseZone.isInterestedByLine(p0, p1))
			{ return new Line(p0, p1); }
		}
	}
	
}
