package sdp.group2.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sdp.group2.communication.CommandQueue;
import sdp.group2.communication.Commands;
import sdp.group2.pc.MasterController;
import sdp.group2.util.Constants;
import sdp.group2.vision.EntityThresh;
import sdp.group2.vision.Thresholds;
import sdp.group2.geometry.Point;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class VisionGUI extends WindowAdapter {
	
    private static final int WINDOW_WIDTH = 1100;
    private static final int WINDOW_HEIGHT = 550;

	public static final int MAIN_INDEX = 0;
	public static final int BALL_INDEX = 1;
	public static final int DOT_INDEX = 2;
	public static final int ROBOT_INDEX = 3;

    private static JFrame windowFrame;
    private static JLabel imageLabel = new JLabel();
    private static JLabel ballLabel = new JLabel();
    private static EntityThresh[] entities;
    private static String[] imageNames = new String[] {"Main", "Ball", "Dots", "Bases"};
    private static String[] entityNames;
    public static int selectedImage;
    public static boolean drawObjects = true;
    
    private static VisionGUI singleton;
    
    static {
    	singleton = new VisionGUI();
    }

	public void initialise() {
	    JPanel contentPanel;
	    entities = Thresholds.activeThresholds.entities;
	    entityNames = new String[]{entities[0].name,entities[1].name, entities[2].name};

        windowFrame = new JFrame("Vision: " + Thresholds.pitchName);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.addWindowListener(this); 
        windowFrame.setVisible(true);
        windowFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Main container
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));               
        
        // Entity Panel
        final JTabbedPane entityPane = new JTabbedPane();
        
        for (int i = 0; i < entityNames.length; i++) {
            JComponent panel = makeSliderPanel(entityNames[i], i);
            entityPane.addTab(entityNames[i], null, panel, null);
		}
        
        // Image List
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createTitledBorder("Select Image:"));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        contentPanel.add(mainPanel);

	    final JList<String> imageList = new JList<String>(imageNames);
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
                	int index = imageList.getSelectedIndex();
                    selectedImage = index;
                    if (index != 0) {
                    	entityPane.setSelectedIndex(index-1);
                    }
                }
            }
        });
        mainPanel.add(imageList);
        mainPanel.add(imageLabel);

        // Sidebar
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createTitledBorder("Edit Thresholds:"));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        contentPanel.add(controlPanel);
        
        // Draw Shit
        JCheckBox drawBox = new JCheckBox();
        drawBox.setSelected(drawObjects);
        drawBox.setText("Toggle Objects");
        drawBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				drawObjects = !drawObjects;
			}
		});
        controlPanel.add(drawBox);
        
     // Start robots
        JCheckBox aiBox = new JCheckBox();
        aiBox.setSelected(MasterController.matchStarted);
        aiBox.setText("AI on");
        aiBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				MasterController.matchStarted = !MasterController.matchStarted;
			}
		});
        controlPanel.add(aiBox);
        
        
        // Adding entity panel
        controlPanel.add(entityPane);

        // Save button
        JButton button = new JButton("Save");
        button.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
            	Thresholds.writeToFile(Thresholds.activeThresholds);
            }
        });
        controlPanel.add(button);
        
        // Bluetooth button
        JButton button2 = new JButton("Test Bluetooth Delay");
        button2.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		System.out.println("Boom.");
        		do360();
        	}
        });
        controlPanel.add(button2);
        
        // Ball position label
        ballLabel.setFont(Font.getFont("Monospaced"));
        controlPanel.add(ballLabel);

        windowFrame.setContentPane(contentPanel);
        windowFrame.pack();
        windowFrame.setVisible(true);
    }
	
	public static void start() {
		singleton.initialise();
	}
	
	public static void updateImage(IplImage image) {
		singleton.setImage(image.getBufferedImage());
	}
	
	public static void setBallPos(Point mmPoint) {
		ballLabel.setText("Ball pos: " + mmPoint.toString());
	}
	
	public void setImage(BufferedImage image) {
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	public void do360() {
    	Random r = new Random();
    	int rand = r.nextInt(10);
		CommandQueue.add(Commands.rotate(400 + rand, 400), Constants.ROBOT_2D_NAME);
    }
	
    protected JComponent makeSliderPanel(String text, int index) {
        JPanel panel = new JPanel(false);
        // below 0 for min 1 for max
        System.out.println(entities[index].name);
        HSBPanel minHSBPanel = new HSBPanel("Min color", 0, entities[index]);
        HSBPanel maxHSBPanel = new HSBPanel("Max color", 1, entities[index]);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(minHSBPanel);
        panel.add(maxHSBPanel);
        return panel;
    }
    
    @Override
    public void windowClosing(WindowEvent e) {
    	MasterController.disconnect();
    	try {
    		Thread.sleep(500);
    	} catch (InterruptedException ex) {
    		ex.printStackTrace();
    	}
    }
	
}
