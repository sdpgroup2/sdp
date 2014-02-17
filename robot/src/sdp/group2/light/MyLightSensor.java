package sdp.group2.light;

import lejos.nxt.ADSensorPort;
import lejos.nxt.LightSensor;

/**
 * A subclass to make sensing light work on a background thread.
 * 
 * @author Mark Nemec
 * 
 */
public class MyLightSensor extends LightSensor {

	private LightListener listener = null;
	private boolean enabled = true;
	private int delay = 0;
	private String name = null;
	
	public MyLightSensor(String name, ADSensorPort port, int delay) {
		this(port, true, delay);
		this.name = name;
	}
	
	public MyLightSensor(ADSensorPort port, boolean floodlight, int delay) {
		super(port, floodlight);
		this.delay = delay;
		Thread x = new MonitorThread();
		x.setDaemon(true);
		x.start();
	}
	
	public void addListener(LightListener listener) {
		this.listener = listener;
	}

	public void enableDetection(boolean enable) {
		this.enabled = enable;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * Thread to monitor the light.
	 * 
	 */
	private class MonitorThread extends Thread {

		long prev_time;

		@Override
		public void run() {
			while (true) {
				// Only performs scan if detection is enabled.
				int lightVal = enabled ? scan() : -1;
				if (lightVal != -1) {
					notifyListeners(lightVal);
				}

				try {
					long elapsed_time = System.currentTimeMillis() - prev_time;

					long actual_delay = delay - elapsed_time;
					if (actual_delay < 0) {
						actual_delay = 0;
					}

					Thread.sleep(actual_delay);
					prev_time = System.currentTimeMillis();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void notifyListeners(int lightVal) {
		if (this.listener != null) {
			this.listener.lightMeasured(lightVal, this);
		}
	}
	
	public String getName() {
		if (name != null) {
			return name;
		} else {
			return "";
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int scan() {
		int lightVal = this.getLightValue();
		return lightVal;
	}

}
