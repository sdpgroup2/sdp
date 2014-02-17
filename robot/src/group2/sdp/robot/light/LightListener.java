package group2.sdp.robot.light;

/*
 * Interface for listeners of MyLightSensor. 
 */
public interface LightListener {

	public void lightMeasured(int lightVal, MyLightSensor sensor);

}
