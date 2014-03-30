package sdp.group2.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

public class SliderPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSlider slider;

	public SliderPanel(ChangeListener changeListener, String property, int min, int max, int initial, int minorTicks, int majorTicks) {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		JLabel propertyLabel = new JLabel(property);
		propertyLabel.setPreferredSize(new Dimension(100, 24));
		slider = new JSlider(JSlider.HORIZONTAL, min, max, initial);
		slider.setName(property);
		slider.setMajorTickSpacing(majorTicks);
		slider.setMinorTickSpacing(minorTicks);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(changeListener);
		
		add(propertyLabel);
		add(slider);
	}

	public int getValue() {
		return slider.getValue();
	}

	public void setValue(int val) {
		slider.setValue(val);
	}

}
