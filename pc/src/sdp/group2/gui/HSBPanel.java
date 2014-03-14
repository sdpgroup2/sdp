package sdp.group2.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HSBPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private HSBColor color = new HSBColor(0,0,0);

	JLabel titleLabel;
	SliderPanel hue;
	SliderPanel saturation;
	SliderPanel brightness;

	public HSBPanel(String title) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(new EmptyBorder(5,5,5,5));

		titleLabel = new JLabel(title);
		hue = new SliderPanel(this, "Hue", 0, 360, 0, 30, 90);
		saturation = new SliderPanel(this, "Saturation", 0, 100, 0, 5, 20);
		brightness = new SliderPanel(this, "Brightness", 0, 100, 0, 5, 20);

		add(titleLabel);
		add(hue);
		add(saturation);
		add(brightness);
	}

	public void updateValue() {
		color = new HSBColor(hue.getValue(), saturation.getValue(), brightness.getValue());
		titleLabel.setForeground(color.getRGBColor());
	}

	public void setValue(HSBColor color) {
		this.color = color;
		hue.setValue(color.iHue());
		saturation.setValue(color.iSaturation());
		brightness.setValue(color.iBrightness());
		titleLabel.setForeground(color.getRGBColor());
	}

	public HSBColor getValue() {
		return color;
	}

}
