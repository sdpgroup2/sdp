package sdp.group2.gui;

import sdp.group2.vision.HSBColor;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ColorChecker extends JPanel {
	
	JLabel label;
	
	public ColorChecker() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		label = new JLabel();
		add(label);
		updateColor(0);
	}
	
	public void updateColor(int color) {
		HSBColor hsb = new HSBColor(color);
		label.setText(String.format("Color[HSB]: %d %d %d", hsb.iHue(), hsb.iSaturation(), hsb.iBrightness()));
	}
	

}
