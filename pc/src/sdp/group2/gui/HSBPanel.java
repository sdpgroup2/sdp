package sdp.group2.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sdp.group2.vision.EntityThresh;

public class HSBPanel extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;

	JLabel titleLabel;
	SliderPanel hue;
	SliderPanel saturation;
	SliderPanel brightness;
	EntityThresh entity;
	int minMax;

	public HSBPanel(String title, int minMax, EntityThresh entity) {
		// min is 0 max is 1
		this.minMax = minMax;
		this.entity = entity;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(5, 5, 5, 5));

		titleLabel = new JLabel(title);
		// -180 is a hack for da ball
		hue = new SliderPanel(this, "Hue", -180, 180, 0, 30, 90);
		saturation = new SliderPanel(this, "Saturation", 0, 255, 0, 25, 50);
		brightness = new SliderPanel(this, "Brightness", 0, 255, 0, 25, 50);
		
		add(titleLabel);
		add(hue);
		add(saturation);
		add(brightness);
		
		setValues();
	}

	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider) e.getSource();
	    if (!source.getValueIsAdjusting()) {
			if (minMax == 0) {
				if (source.getName() == "Hue") {
					entity.mins[0] = hue.getValue();
				} else if (source.getName() == "Saturation") {
					entity.mins[1] = saturation.getValue();
				} else {
					entity.mins[2] = brightness.getValue();
				}
			} else {
				if (source.getName() == "Hue") {
					entity.maxs[0] = hue.getValue();
				} else if (source.getName() == "Saturation") {
					entity.maxs[1] = saturation.getValue();
				} else {
					entity.maxs[2] = brightness.getValue();
				}
			}
	    }
	}

	public void setValues() {
		int[] newColor = minMax == 0 ? entity.mins : entity.maxs;
		System.out.println("Set H: " + newColor[0] + "S: " + newColor[1] + "V: " + newColor[2]);
		hue.setValue(newColor[0]);
		saturation.setValue(newColor[1]);
		brightness.setValue(newColor[2]);
	}

}
