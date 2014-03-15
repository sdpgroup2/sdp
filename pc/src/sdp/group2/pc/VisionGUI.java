package sdp.group2.pc;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sdp.group2.geometry.Point;
import sdp.group2.gui.ColorChecker;
import sdp.group2.gui.HSBPanel;
import sdp.group2.util.Debug;
import sdp.group2.util.Tuple;
import sdp.group2.vision.EntityThresh;
import sdp.group2.vision.Thresholds;
import sdp.group2.vision.VisionService;
import sdp.group2.vision.VisionServiceCallback;

public class VisionGUI extends WindowAdapter implements VisionServiceCallback {
	
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private VisionService visionService;

    private JFrame windowFrame;
    private Dimension frameSize;
    private JLabel imageLabel = new JLabel();
    private ColorChecker colorChecker;
    private EntityThresh[] entities;
    private String[] entityNames;

    private BufferedImage currentImage;	   

	public VisionGUI(VisionService visionService) {
		super();
		this.visionService = visionService;
		this.frameSize = visionService.getSize();
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

        // Image
        imageLabel.setMinimumSize(frameSize);
        imageLabel.setPreferredSize(frameSize);
        imageLabel.setMaximumSize(frameSize);
        contentPanel.add(imageLabel);

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
        entityList.setPreferredSize(new Dimension(200, 100));
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
                        EntityThresh entity = entities[index];
                        minHSBPanel.setValue(entity.mins);
                        maxHSBPanel.setValue(entity.maxs);
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

        // Debug checkboxes
        final JCheckBox fillBox = new JCheckBox("Fill clusters");
        fillBox.setSelected(Debug.VISION_FILL_PIXELS);
        fillBox.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent e) {
                Debug.VISION_FILL_PIXELS = ((JCheckBox) e.getSource()).isSelected();
            }
        });
        final JCheckBox rectBox = new JCheckBox("Draw rectangles");
        rectBox.setSelected(Debug.VISION_DRAW_BOUNDS);
        rectBox.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent e) {
                Debug.VISION_DRAW_BOUNDS = ((JCheckBox) e.getSource()).isSelected();
            }
        });
        final JCheckBox normBox = new JCheckBox("Normalize image");
		normBox.setSelected(Debug.VISION_NORMALIZE_IMAGE);
		normBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Debug.VISION_NORMALIZE_IMAGE = ((JCheckBox) e.getSource()).isSelected();
			}
		});
        controlPanel.add(fillBox);
        controlPanel.add(rectBox);
        controlPanel.add(normBox);

        windowFrame.setContentPane(contentPanel);
    }
	
	public void getImage(BufferedImage image) {
		//System.out.println("Got image.");
		imageLabel.setIcon(new ImageIcon(image));
	}

	@Override
	public void onExceptionThrown(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Point ballCentroid,
			List<Tuple<Point, Point>> yellowRobots,
			List<Tuple<Point, Point>> blueRobots) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepared(Point ballCentroid,
			List<Tuple<Point, Point>> yellowRobots,
			List<Tuple<Point, Point>> blueRobots) {
		// TODO Auto-generated method stub
		
	}
	
}