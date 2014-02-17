package sdp.group2.light;

/*
 * Interface for listeners of MyLightSensor. 
 */
public interface LightListener {

	public void lightMeasured(int lightVal, MyLightSensor sensor);

}
