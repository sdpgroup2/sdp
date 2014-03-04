package sdp.navigation;

import java.io.IOException;

public interface Rotation extends Runnable {
	public void stopRotating() throws IOException;
	public boolean isRotating();
	public void setTargetAngle(double angle, double speed, double startTolerance) throws IOException;    
    public double angleToTarget();
	public void run();
}
