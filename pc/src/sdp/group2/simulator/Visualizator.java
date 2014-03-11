package sdp.group2.simulator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sdp.group2.geometry.Millimeter;
import sdp.group2.geometry.PointSet;
import sdp.group2.world.Ball;
import sdp.group2.world.Pitch;
import sdp.group2.world.Zone;
import sdp.group2.geometry.Point;

/** TODO: draw pitch and zones
 * 	TODO: highlight active zone
 *  TODO: draw robot and its direction
 *  TODO: draw ball and its direction
 *  TODO: account for goals, add score table, and restart procedure
 *  TODO: add pause/resume
 */

public class Visualizator extends JFrame {
	
	private static final long serialVersionUID = -666L;
	private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    
    private static final Color COLOR_BACKGROUND = new Color(0, 0, 0);
    private static final Color COLOR_BOUNDARY = new Color(255, 255, 255);
    private static final Color COLOR_PITCH = new Color(0, 100, 0);
    
    private Pitch pitch;
    
    private JPanel frame;
	private JPanel panel;
    
    public Visualizator(Pitch pitch) {
    	super();
    	this.pitch = pitch;
    	initializeComponent();
    	this.setVisible(true);
    }
    
    public void initializeComponent() {
		frame = (JPanel)this.getContentPane();
		panel = new JPanel();

		frame.setLayout(null);
		frame.setBackground(COLOR_BACKGROUND);
		frame.setForeground(COLOR_BOUNDARY);
		
		addComponent(frame, panel, 10, 10, 640, 480);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.setBackground(COLOR_PITCH);
		panel.add(new DrawPitch());
		
		this.setTitle("Match Simulator");
		this.setLocation(new java.awt.Point(10, 10));
		this.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pitch.updateBallPosition(new Point(500, 400));
		
    }
    
    private void drawOutline() {
    	
    }
    
    private void addComponent(Container container, Component c, int x, int y, int width, int height) {
		c.setBounds(x,y,width,height);
		container.add(c);
	}
    
    private class DrawPitch extends JPanel {
    	
    	public Dimension getPreferredSize() {
    		return new Dimension(640, 480);
    	}
    	
    	protected void paintComponent(Graphics g) {
     
    		g.setColor(COLOR_BOUNDARY);
    		PointSet pitchOutline = pitch.getOutline();
    		int N = pitchOutline.size();
    		for (int i = 1; i < N + 1; i++) {
    			Point p0 = pitchOutline.get((i - 1) % N);
    			Point p1 = pitchOutline.get(i % N);
    			g.drawLine((int) Millimeter.mm2pix(p0.x),
                        (int) Millimeter.mm2pix(p0.y),
                        (int) Millimeter.mm2pix(p1.x),
                        (int) Millimeter.mm2pix(p1.y));
    		}
    		
    		for(Zone zone : pitch.getAllZoneOutline()) {
    			g.setColor(new Color(255, 255, 0));
    			PointSet outline = zone.getOutline();
    			N = outline.size();
    			for (int i = 1; i < N + 1; i++) {
        			Point p0 = outline.get((i - 1) % N);
        			Point p1 = outline.get(i % N);
        			g.drawLine((int) Millimeter.mm2pix(p0.x),
                            (int) Millimeter.mm2pix(p0.y),
                            (int) Millimeter.mm2pix(p1.x),
                            (int) Millimeter.mm2pix(p1.y));
        		}
    			
    			g.setColor(new Color(0, 0, 100));
// Robot position is null    			
//    			int x = Milimeter.mm2pix(zone.getRobotPosition().x);
//    			int y = Milimeter.mm2pix(zone.getRobotPosition().y);
//    			g.fillRect(x, y, 50, 50);
    		}
    		
    		g.setColor(new Color(255, 0, 0));
    		
    		Ball ball = pitch.getBall();
    		int r = (int) Millimeter.mm2pix(ball.getRadius());
    		int x = (int) Millimeter.mm2pix(ball.getPosition().x - r);
    		int y = (int) Millimeter.mm2pix(ball.getPosition().y - r);
    		g.fillOval(x, y, 2 * r, 2 * r);
     
    	}
    }

}
