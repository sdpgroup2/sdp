/**
 * @author Jaroslaw Hirniak, s1143166
 */

package sdp.group2.world;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.PointSet;


/** Note that recommended units for the pitch are in tacho counts. Hence, we
 *  should measure how many pixels are how many tachos and work with those
 *  values. By default pitch accepts pixels and multiplies them by tacho count
 *  to pixel ratio. */
public interface IPitch
{
    // TODO: add synchronization for atomic read/write
    // TODO: measure tacho count to pixel ratio
    
    /** Initialises zone polygon
     *  @param id - id of the zone, should be counted from 0 from origin, note
     *              id of the zone and the robot should be the same for easy
     *              pairing
     *  @param ps - convex hull of the zone, note points can be in any order,
     *              as sorting method is in place. */
    public void setZone(byte id, PointSet ps);
    public Zone getZone(byte id);
    
    /** From the origin, is ally on even or odd number fields
     *  @param even - is on even fields? */
    //public void setAlly(boolean even);
    
    /** Note that you don't need to pass ball's velocity as it will be inferred
     *  inside Pitch class by storing ball movement history. Note, that for the
     *  best results FPS ratio from the camera should be known and constant. */
    public void updateBallPosition(Point p);
    
    /** @param id - should be the same as zone id, counted from 0 from
     *              the origin 
     *  @param p - current position of the robot
     *  @param theta - angle of the robot from the origin */
    public void updateRobotState(byte id, Point p, double theta);
    
    /** Gets singleton instance of the pitch. */
    //public IPitch getInstance();
    
    /** Returns zone in which the ball is. */
    public int getBallZone();
    
    // TODO: Add methods useful for AI
    
    //public boolean isAllyEven();
    
    /** Does the obvious thing, sets the ratio and by default translate pixel
     *  values to tacho counts. But, if for some reason it is passed already as
     *  tacho count then it can be switched of with reset.
     */
    //public void setTachoToPixelValue(double ratio);
    //public double getTachoToPixelRatio();
    //public void setToTachoTranslation();
    //public void resetToTachoTranslation();
    
    public PointSet getTrajectory();

    public Ball getBall();

    public Robot getOurDefender();

    public Robot getOurAttacker();

    public Robot getFoeDefender();

    public Robot getFoeAttacker();

}
