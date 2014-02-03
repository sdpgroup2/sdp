package group2.sdp.pc.comms;

//UI imports
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;


@SuppressWarnings("serial")
/**
 * class to control robot by using a GUI to test time to send messages to it
 * @author Gordon Edwards
 * @author Michael Mair
 * Code based on SDP group 4 2013
 */
public class ControlGUI extends JFrame {
	// GUI elements

	private final JPanel startStopQuitPanel = new JPanel();
	private final JPanel optionsPanel = new JPanel();
	private final JPanel simpleMovePanel = new JPanel();
	private final JPanel complexMovePanel = new JPanel();
	private final JPanel moveTargetPanel = new JPanel();
	private final JPanel moveTargetOptionsPanel = new JPanel();
	// General control buttons
	private final JButton startButton = new JButton("Start");
	private final JButton resetButton = new JButton("Reset");
	private final JButton quitButton = new JButton("Quit");
	private final JButton forceQuitButton = new JButton("Force quit");
	private final JButton stopButton = new JButton("Stop");
	private final JButton stratStartButton = new JButton("Strat Start");
	private final JButton penaltyAtkButton = new JButton("Penalty Attack");
	private final JButton penaltyDefButton = new JButton("Penalty Defend");
	private final JButton moveNoCollTarget = new JButton("Move while avoiding just opponent");
	private final JButton moveNoCollOppTarget = new JButton("Move while avoiding all obstacles");
	// Basic movement
	private final JButton forwardButton = new JButton("Forward");
	private final JButton backwardButton = new JButton("Backward");
	private final JButton leftButton = new JButton("Left");
	private final JButton rightButton = new JButton("Right");
	// Kick
	private final JButton kickButton = new JButton("Kick");
	private final JButton dribblerStart = new JButton("DribblerStart");
	private final JButton dribblerStop = new JButton("DribblerStop");
	// Other movement
	private final JButton rotateButton = new JButton("Rotate");
	private final JButton moveButton = new JButton("Move");
	private final JButton moveToButton = new JButton("Move To");
	private final JButton rotateAndMoveButton = new JButton("Rotate & Move");
	private final JButton dribbleButton = new JButton("Dribble");

	// OPcode fields
	private final JLabel op1label = new JLabel("Option 1: ");
	private final JLabel op2label = new JLabel("Option 2: ");
	private final JLabel op3label = new JLabel("Option 3: ");
	private final JLabel op4label = new JLabel("Move to (x label): ");
	private final JLabel op5label = new JLabel("Move to (y label): ");
	private final JTextField op1field = new JTextField();
	private final JTextField op2field = new JTextField();
	private final JTextField op3field = new JTextField();
	public static JTextField op4field = new JTextField();
	public static JTextField op5field = new JTextField();
	
	private static BTSend btSendR1;
	private static BTSend btSendR2;
	
	public static void main(String[] args) throws IOException {
		// Make the GUI pretty
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Sets up the GUI
		ControlGUI gui = new ControlGUI();
		gui.setVisible(true);
		
		btSendR1 = null;
		btSendR2 = null;
		try {
			//note of name and MAC
//			btSendR1 = new BTSend("SDP 2D","0016530BBBEA");
			btSendR2 = new BTSend("SDP 2A", "00165307D55F");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("problem connecting" + e1.getMessage());
		}

	}

	private void cleanQuit() {
		robot.clearBuff();
		if (robot.isConnected())
			robot.disconnect();
		System.exit(0);
	}

	public ControlGUI() {

		this.setTitle("Group 2 control GUI");

		op1field.setColumns(6);
		op2field.setColumns(6);
		op3field.setColumns(6);
		op1field.setText("0");
		op2field.setText("0");
		op3field.setText("0");
		// Auto-generated GUI code (made more readable)
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.getContentPane().setLayout(gridBagLayout);

		GridBagConstraints gbc_startStopQuitPanel = new GridBagConstraints();
		gbc_startStopQuitPanel.anchor = GridBagConstraints.NORTH;
		gbc_startStopQuitPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_startStopQuitPanel.insets = new Insets(0, 0, 5, 0);
		gbc_startStopQuitPanel.gridx = 0;
		gbc_startStopQuitPanel.gridy = 0;
		this.getContentPane().add(startStopQuitPanel, gbc_startStopQuitPanel);
		startStopQuitPanel.add(startButton);
		startStopQuitPanel.add(stopButton);
		startStopQuitPanel.add(resetButton);
		startStopQuitPanel.add(quitButton);
		startStopQuitPanel.add(forceQuitButton);
		startStopQuitPanel.add(stratStartButton);
		startStopQuitPanel.add(penaltyAtkButton);
		startStopQuitPanel.add(penaltyDefButton);

		GridBagConstraints gbc_simpleMoveTestPanel = new GridBagConstraints();
		gbc_simpleMoveTestPanel.anchor = GridBagConstraints.NORTH;
		gbc_simpleMoveTestPanel.fill = GridBagConstraints.VERTICAL;
		gbc_simpleMoveTestPanel.insets = new Insets(0, 0, 5, 0);
		gbc_simpleMoveTestPanel.gridx = 0;
		gbc_simpleMoveTestPanel.gridy = 1;
		// gbc_simpleMoveTestPanel.gridwidth = 2;
		this.getContentPane().add(optionsPanel, gbc_simpleMoveTestPanel);
		optionsPanel.add(op1label);
		optionsPanel.add(op1field);
		optionsPanel.add(op2label);
		optionsPanel.add(op2field);
		optionsPanel.add(op3label);
		optionsPanel.add(op3field);

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		this.getContentPane().add(simpleMovePanel, gbc_panel);
		simpleMovePanel.add(forwardButton);
		simpleMovePanel.add(backwardButton);
		simpleMovePanel.add(leftButton);
		simpleMovePanel.add(rightButton);
		simpleMovePanel.add(kickButton);
		simpleMovePanel.add(dribblerStart);
		simpleMovePanel.add(dribblerStop);

		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 3;
		this.getContentPane().add(complexMovePanel, gbc_panel_1);
		complexMovePanel.add(rotateButton);
		complexMovePanel.add(moveButton);
		complexMovePanel.add(moveToButton);
		complexMovePanel.add(rotateAndMoveButton);

		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 4;
		this.getContentPane().add(moveTargetPanel, gbc_panel_2);
		moveTargetPanel.add(moveNoCollTarget);
		moveTargetPanel.add(moveNoCollOppTarget);

		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 5;
		this.getContentPane().add(moveTargetOptionsPanel, gbc_panel_3);
		op4field.setColumns(6);
		op5field.setColumns(6);
		op4field.setText("" + 100);
		op5field.setText("" + 100);
		moveTargetOptionsPanel.add(op4label);
		moveTargetOptionsPanel.add(op4field);
		moveTargetOptionsPanel.add(op5label);
		moveTargetOptionsPanel.add(op5field);

		complexMovePanel.add(dribbleButton);

		this.addWindowListener(new ListenCloseWdw());

		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});


		kickButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mover.kick();
			}
		});

		
		forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int op1 = Integer.parseInt(op1field.getText());
				mover.move(0, op1);
			}
		});

		backwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int op1 = Integer.parseInt(op1field.getText());

				mover.move(0, -op1);
			}
		});

		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int op1 = Integer.parseInt(op1field.getText());

				mover.move(-op1, 0);
			}
		});

		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int op1 = Integer.parseInt(op1field.getText());

				mover.move(op1, 0);
			}
		});

		dribbleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dribbleThread == null || !dribbleThread.isAlive()) {
					dribbleThread = new DribbleBallThread();
					dribbleThread.start();
				} else {
					System.out.println("Dribble is already active!");
				}
			}
		});

		rotateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int angle = Integer.parseInt(op1field.getText());

				mover.rotate(Math.toRadians(angle));
			}
		});

		moveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int op1 = Integer.parseInt(op1field.getText());
				int op2 = Integer.parseInt(op2field.getText());

				mover.move(op1, op2);
			}
		});

		moveToButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int op1 = Integer.parseInt(op1field.getText());
				int op2 = Integer.parseInt(op2field.getText());

				mover.moveToAndStop(op1, op2);
			}
		});

		rotateAndMoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int op1 = Integer.parseInt(op1field.getText());
				int op2 = Integer.parseInt(op2field.getText());
				int op3 = Integer.parseInt(op3field.getText());

				robot.rotateMove(op1, op2, op3);
			}
		});

		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Disconnecting...");
				// Kill the mover and wait for it to stop completely
				robot.disconnect();
				System.out.println("Disconnected succesfully");
				System.out.println("Reconnecting...");
				try {
					Thread.sleep(400);
					robot.connect();
					System.out.println("Reconnected successfully!");
				} catch (Exception e1) {
					System.out.println("Failed to reconnect! Shutting down GUI...");
					cleanQuit();
				}
			}
		});

		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Quitting the GUI");
				cleanQuit();
			}
		});

		forceQuitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Quitting the GUI");
				robot.clearBuff();
				robot.forcequit();
				System.exit(0);
			}
		});

		// Centre the window on startup
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getPreferredSize();
		this.setLocation((dim.width - frameSize.width) / 2, (dim.height - frameSize.height) / 2);
		this.setResizable(false);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}

	public class ListenCloseWdw extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.out.println("Quitting the GUI");
			cleanQuit();
		}
	}
}
