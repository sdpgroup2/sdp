package sdp.group2.pc;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sdp.group2.geometry.Point;
import sdp.group2.gui.ColorChecker;
import sdp.group2.gui.HSBPanel;
import sdp.group2.util.Tuple;
import sdp.group2.vision.EntityThresh;
import sdp.group2.vision.Thresholds;
import sdp.group2.vision.VisionService;
import sdp.group2.vision.VisionServiceCallback;

public class VisionGUI extends WindowAdapter {
	
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 600;


    private JFrame windowFrame;
    private Dimension frameSize;
    private JLabel imageLabel = new JLabel();
    private ColorChecker colorChecker;
    private EntityThresh[] entities;
    private String[] entityNames;
    private int selectedTab;

    private BufferedImage currentImage;	   

	public VisionGUI(int width, int height) {
		super();
		this.frameSize = new Dimension(width, height);
	}
	
	public int getSelectedTab() {
		return selectedTab;
	}

	public void start() {
	    JPanel contentPanel;
	    entities = Thresholds.entities;
	    entityNames = new String[]{entities[0].name,entities[1].name, entities[2].name, entities[3].name};
        final HSBPanel minHSBPanel = new HSBPanel("Min color");
        final HSBPanel maxHSBPanel = new HSBPanel("Max color");
	    colorChecker = new ColorChecker();
	    final JList<String> entityList = new JList<String>(entityNames);
        windowFrame = new JFrame("Vision");
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.addWindowListener(this); 
        windowFrame.setVisible(true);
        windowFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Main container
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(10,10,10,10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));

        // Images
        imageLabel.setMinimumSize(frameSize);
        imageLabel.setPreferredSize(frameSize);
        imageLabel.setMaximumSize(frameSize);
        
      //Tabs
        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent event) {
				selectedTab = tabbedPane.getSelectedIndex();				
			}
        	
        });
        tabbedPane.addTab("Main view", null, imageLabel,
                          "Shows real image");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        tabbedPane.addTab("Ball view", null, new JLabel(),
        "Shows balls");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        contentPanel.add(tabbedPane);

        // Sidebar
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(new EmptyBorder(10,10,10,10));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        contentPanel.add(controlPanel);

     // Entity list
        JPanel listPanel = new JPanel();
        entityList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        entityList.setLayoutOrientation(JList.VERTICAL);
        entityList.setVisibleRowCount(-1);
        entityList.setPreferredSize(new Dimension(200, 80));
        entityList.setBorder(new LineBorder(Color.black));
        entityList.setSelectedIndex(0);
        minHSBPanel.setValue(entities[0].mins);
        maxHSBPanel.setValue(entities[0].maxs);
        entityList.addListSelectionListener(new ListSelectionListener() {
            @Override
			public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    int index = entityList.getSelectedIndex();
                    if (index >= 0) {
                        minHSBPanel.setValue(entities[index].mins);
                        maxHSBPanel.setValue(entities[index].maxs);
                    }
                }
            }
        });
        listPanel.add(entityList);
        controlPanel.add(listPanel);

        // Sliders
        controlPanel.add(minHSBPanel);
        controlPanel.add(maxHSBPanel);               

     // Update button

        controlPanel.add(colorChecker);// Update button
        JButton button = new JButton("Update");
        button.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                int index = entityList.getSelectedIndex();
                switch (index) {
                	case 0: 
                		Thresholds.activeThresholds.ballMins = minHSBPanel.getValue();
                		Thresholds.activeThresholds.ballMaxs = maxHSBPanel.getValue();
                		break;
                	case 1:
                		Thresholds.activeThresholds.dotMins = minHSBPanel.getValue();
                		Thresholds.activeThresholds.dotMaxs = maxHSBPanel.getValue();
                		break;
                	case 2:
                		Thresholds.activeThresholds.basePlateMins = minHSBPanel.getValue();
                		Thresholds.activeThresholds.basePlateMaxs = maxHSBPanel.getValue();
                		break;
                	case 3:
                		Thresholds.activeThresholds.yellowMins = minHSBPanel.getValue();
                		Thresholds.activeThresholds.yellowMaxs = maxHSBPanel.getValue();
                		break;
                	default:
                		break;
                }
            }
        });
        controlPanel.add(button);        

        windowFrame.setContentPane(contentPanel);
    }
	
	protected JComponent makeTextPanel(String text) {
	    JPanel panel = new JPanel(false);
	    JLabel filler = new JLabel(text);
	    filler.setHorizontalAlignment(JLabel.CENTER);
	    panel.setLayout(new GridLayout(1, 1));
	    panel.add(filler);
	    return panel;
	}
	
	public void setImage(BufferedImage image) {
		//System.out.println("Got image.");
		imageLabel.setIcon(new ImageIcon(image));
	}
	
}
