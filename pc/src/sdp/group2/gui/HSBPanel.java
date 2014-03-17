package sdp.group2.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sdp.group2.vision.Thresholds;

public class HSBPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private int[] color = new int[3];

	JLabel titleLabel;
	SliderPanel hue;
	SliderPanel saturation;
	SliderPanel brightness;

	public HSBPanel(String title) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(5,5,5,5));

		titleLabel = new JLabel(title);
		hue = new SliderPanel(this, "Hue", 0, 180, 0, 15, 30);
		saturation = new SliderPanel(this, "Saturation", 0, 255, 0, 25, 50);
		brightness = new SliderPanel(this, "Brightness", 0, 255, 0, 25, 50);

		add(titleLabel);
		add(hue);
		add(saturation);
		add(brightness);
	}

	public void updateValue() {
//		System.out.println("Before:\nHue: " + color[0]);
//		System.out.println("Sat: " + color[1]);
//		System.out.println("Val: " + color[2]);
		color[0] = hue.getValue();
		color[1] = saturation.getValue();
		color[2] = brightness.getValue();
//		System.out.println("After:\nHue: " + color[0]);
//		System.out.println("Sat: " + color[1]);
//		System.out.println("Val: " + color[2]);
		//titleLabel.setForeground(color.getRGBColor());
	}

	public void setValue(int[] color) {
		this.color = color;
		hue.setValue(color[0]);
		saturation.setValue(color[1]);
		brightness.setValue(color[2]);
		//titleLabel.setForeground(color.getRGBColor());
	}

	public int[] getValue() {
		return color;
	}

}
