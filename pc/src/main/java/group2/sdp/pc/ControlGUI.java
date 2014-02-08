package group2.sdp.pc;

//UI imports
import group2.sdp.pc.comms.Sender;

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


//@SuppressWarnings("serial")
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
	private final JButton disconnectButton = new JButton("Quit");
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

	private static Sender connection2D;
	private static Sender connection2A;

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

		connection2D = null;
		connection2A = null;


		boolean connectedR1 = false;
		Thread connectTo2A = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i< 5; i++){
					try {
						//note of name and MAC
						connection2A = new Sender("SDP 2A","00165307D55F");
						break;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("problem connecting" + e1.getMessage());
						if (i == 5){
							System.exit(0);
						}
					}
				}
			}

		});
		Thread connectTo2D = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i< 5; i++){
					try {
						//note of name and MAC
						connection2D = new Sender("SDP 2D","0016530BBBEA");
						break;

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("problem connecting" + e1.getMessage());
						if (i == 5){
							System.exit(0);
						}
					}
				}
			}

		});
		connectTo2A.start();
		connectTo2D.start();

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
		startStopQuitPanel.add(disconnectButton);
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
			@Override
			public void actionPerformed(ActionEvent e) {
				connection2A.clearBuff();
				connection2D.clearBuff();
				connection2A.stop();
				connection2D.stop();
			}
		});

		moveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connection2A.clearBuff();
				connection2D.clearBuff();
				final int direction = Integer.parseInt(op1field.getText());
				final int angle = Integer.parseInt(op2field.getText());
				final int speed = Integer.parseInt(op3field.getText());
				Thread moveBot1 = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							connection2A.move(direction, angle, speed);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				Thread moveBot2 = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							connection2D.move(direction, angle, speed);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				moveBot1.start();
				moveBot2.start();
				try {
					moveBot1.join();
					moveBot2.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

		kickButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connection2A.clearBuff();
				connection2D.clearBuff();
				final int angle = Integer.parseInt(op1field.getText());
				final int speed = Integer.parseInt(op2field.getText());
				Thread moveBot1 = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							connection2A.kick(angle, speed);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				Thread moveBot2 = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							connection2D.kick(angle, speed);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				moveBot1.start();
				moveBot2.start();
				try {
					moveBot1.join();
					moveBot2.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		rotateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connection2D.clearBuff();
//				btSendR2.clearBuff();
				final int direction = Integer.parseInt(op1field.getText());
				final int angle = Integer.parseInt(op2field.getText());
				final int speed = Integer.parseInt(op3field.getText());
				Thread rotateBot1 = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							connection2A.rotate(direction, angle, speed);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				});
				Thread rotateBot2 = new Thread(new Runnable() {

					@Override
					public void run() {
					try {
						connection2A.rotate(direction, angle, speed);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}

				});
				rotateBot1.start();
				rotateBot2.start();
				try {
					rotateBot1.join();
					rotateBot2.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		disconnectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Quitting the GUI");
//				cleanQuit();
			}
		});

		forceQuitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Quitting the GUI");
				System.exit(0);
			}
		});

		forwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		backwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		leftButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		rightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		dribbleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		moveToButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		rotateAndMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Disconnecting...");
				// Kill the mover and wait for it to stop completely
				System.out.println("Disconnected succesfully");
				System.out.println("Reconnecting...");
				try {
					Thread.sleep(400);
					System.out.println("Reconnected successfully!");
				} catch (Exception e1) {
					System.out.println("Failed to reconnect! Shutting down GUI...");
//					cleanQuit();
				}
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
		@Override
		public void windowClosing(WindowEvent e) {
			System.out.println("Quitting the GUI");
//			cleanQuit();
		}
	}
}

