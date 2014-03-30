package sdp.group2.gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.simple.JSONObject;

import sdp.group2.vision.EntityThresh;
import sdp.group2.vision.Thresholds;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class VisionGUI extends WindowAdapter {
	
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 600;

	public static final int MAIN_INDEX = 0;
	public static final int BALL_INDEX = 1;
	public static final int ROBOT_INDEX = 2;
	public static final int DOT_INDEX = 3;

    private static JFrame windowFrame;
    private static Dimension frameSize;
    private static JLabel imageLabel = new JLabel();
    private static ColorChecker colorChecker;
    private static EntityThresh[] entities;
    private static String[] imageNames = new String[] {"Main", "Ball", "Bases", "Dots"};
    private static String[] entityNames;
    private static JList<String> entityList;
    public static int selectedImage;
    public static boolean drawShit = false;
    
    private static VisionGUI singleton;
    
    static {
    	singleton = new VisionGUI(640, 480);
    }

	public VisionGUI(int width, int height) {
		super();
		frameSize = new Dimension(width, height);
	}

	public void initialise() {
	    JPanel contentPanel;
	    entities = Thresholds.activeThresholds.entities;
	    entityNames = new String[]{entities[0].name,entities[1].name, entities[2].name, entities[3].name};
        final HSBPanel minHSBPanel = new HSBPanel("Min color");
        final HSBPanel maxHSBPanel = new HSBPanel("Max color");
        
	    colorChecker = new ColorChecker();
	    entityList = new JList<String>(entityNames);
	    final JList<String> imageList = new JList<String>(imageNames);
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
        contentPanel.add(imageLabel);

        // Sidebar
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(new EmptyBorder(10,10,10,10));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        contentPanel.add(controlPanel);
        
        // Draw Shit
        JCheckBox drawBox = new JCheckBox();
        drawBox.setSelected(drawShit);
        drawBox.setText("Draw Shit!");
        drawBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				drawShit = !drawShit;
			}
		});
        controlPanel.add(drawBox);

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
                    System.out.println("Selected option "+index);
                    if (index >= 0) {
                        minHSBPanel.setValue(entities[index].mins);
                        maxHSBPanel.setValue(entities[index].maxs);
                    }
                }
            }
        });
        listPanel.add(entityList);
        controlPanel.add(listPanel);
        
        // Image List
        JPanel imagePanel = new JPanel();
        imageList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        imageList.setLayoutOrientation(JList.VERTICAL);
        imageList.setVisibleRowCount(-1);
        imageList.setPreferredSize(new Dimension(200, 80));
        imageList.setBorder(new LineBorder(Color.black));
        imageList.setSelectedIndex(0);
        imageList.addListSelectionListener(new ListSelectionListener() {
            @Override
			public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    selectedImage = imageList.getSelectedIndex();
                }
            }
        });
        imagePanel.add(imageList);
        controlPanel.add(imagePanel);

        // Sliders
        controlPanel.add(minHSBPanel);
        controlPanel.add(maxHSBPanel);       
        
        // Add Parent listeners to sliders so they notify gui
        ChangeListener slideListener = new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
                int index = entityList.getSelectedIndex();
                System.out.println("Update button pressed.");
                switch (index) {
                	case 0:                 		
                		Thresholds.activeThresholds.ballMins = minHSBPanel.copyValue();
                		Thresholds.activeThresholds.ballMaxs = maxHSBPanel.copyValue();
                		break;
                	case 1:
                		Thresholds.activeThresholds.dotMins = minHSBPanel.copyValue();
                		Thresholds.activeThresholds.dotMaxs = maxHSBPanel.copyValue();
                		break;
                	case 2:
                		Thresholds.activeThresholds.basePlateMins = minHSBPanel.copyValue();
                		Thresholds.activeThresholds.basePlateMaxs = maxHSBPanel.copyValue();
                		break;
                	case 3:
                		Thresholds.activeThresholds.yellowMins = minHSBPanel.copyValue();
                		Thresholds.activeThresholds.yellowMaxs = maxHSBPanel.copyValue();
                		break;
                	default:
                		break;
                }
                updateEntities(entities);
			}
		};

		minHSBPanel.addParentChangeListener(slideListener);
		maxHSBPanel.addParentChangeListener(slideListener);
		
        // Update button
        controlPanel.add(colorChecker);// Update button
        JButton button = new JButton("Save");
        button.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
        		JSONObject jsonThresh = Thresholds.activeThresholds.serialize();
    			FileWriter file = null;
				try {
					file = new FileWriter("assets/thresholds/" + Thresholds.pitchName + ".json");
	    			file.write(jsonThresh.toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					if (file != null) {
						try {
							file.flush();
							file.close();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
				}
            }
        });
        controlPanel.add(button);

        windowFrame.setContentPane(contentPanel);
    }
	
	public static void start() {
		singleton.initialise();
	}
	
	public static void updateImage(IplImage image) {
		singleton.setImage(image.getBufferedImage());
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
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	public void updateEntities(EntityThresh[] entities) {
		for (int i =0; i<entities.length; i++) {
			if (entities[i].name.equals("Ball")) {
				entities[i].mins = Thresholds.activeThresholds.ballMins;
				entities[i].maxs = Thresholds.activeThresholds.ballMaxs;
			}
			if (entities[i].name.equals("Dot")) {
				entities[i].mins = Thresholds.activeThresholds.dotMins;
				entities[i].maxs = Thresholds.activeThresholds.dotMaxs;
			}
			if (entities[i].name.equals("BasePlate")) {
				entities[i].mins = Thresholds.activeThresholds.basePlateMins;
				entities[i].maxs = Thresholds.activeThresholds.basePlateMaxs;
			}
			if (entities[i].name.equals("Yellow")) {
				entities[i].mins = Thresholds.activeThresholds.yellowMins;
				entities[i].maxs = Thresholds.activeThresholds.yellowMaxs;
			}
		}
	}
	
}
