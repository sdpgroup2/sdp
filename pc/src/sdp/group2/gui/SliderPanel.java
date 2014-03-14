package sdp.group2.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderPanel extends JPanel implements ChangeListener {

	private HSBPanel parent;
	private JSlider slider;
	private JLabel quantity;

	public SliderPanel(HSBPanel parent, String property, int min, int max, int initial, int minorTicks, int majorTicks) {
		this.parent = parent;
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		JLabel propertyLabel = new JLabel(property);
		propertyLabel.setPreferredSize(new Dimension(100, 24));
		slider = new JSlider(JSlider.HORIZONTAL, min, max, initial);
		slider.setMajorTickSpacing(majorTicks);
		slider.setMinorTickSpacing(minorTicks);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(this);
		quantity = new JLabel(""+initial);
		quantity.setPreferredSize(new Dimension(32, 24));

		add(propertyLabel);
		add(slider);
		add(quantity);
	}

	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider) e.getSource();
	    if (!source.getValueIsAdjusting()) {
	        int val = (int) source.getValue();
	        quantity.setText(""+val);
	        parent.updateValue();
	    }
	}

	public int getValue() {
		return slider.getValue();
	}

	public void setValue(int val) {
		slider.setValue(val);
	}

}
