package org.k9m.switcher.gui;

import org.k9m.switcher.PacketSwitcher;
import org.k9m.switcher.model.Packet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame implements ActionListener
{

	private static final int NR_NODES = 20;
	
	private static final int NODEBOX_ROWS = 3;
	private static final int NODEBOX_COLS = 8;
	
	private static final int TARGETBOX_ROWS = 3;
	private static final int TARGETBOX_COLS = 30;
	
	private static final String DEFAULT_TEXT = "The Dog Days Are Over";

	private static final Dimension DIM_BUTTON = new Dimension(100,30);
	
	private JButton stop;
	private JButton start;
	private JButton test;
	private JTextArea[] nodeBoxes;
	private JTextArea targetBox;
	private JTextField messageBox;
	private PacketSwitcher switcher;

	public Gui()
	{			
		int gridSize = (int)Math.round(Math.sqrt(NR_NODES -1));				

		JPanel boxPanel = new JPanel(new GridLayout(gridSize , gridSize));		

		JPanel[] nodePanels = new JPanel[NR_NODES - 1];
		nodeBoxes = new JTextArea[NR_NODES - 1];

		for (int i = 0; i < NR_NODES - 1; i++){

			nodeBoxes[i] = new JTextArea(NODEBOX_ROWS,NODEBOX_COLS);
			nodeBoxes[i].setEditable(false);

			nodePanels[i] = new JPanel();
			nodePanels[i].setBorder(BorderFactory.createTitledBorder("Node [" + i + "]"));
			nodePanels[i].add(nodeBoxes[i]);

			boxPanel.add(nodePanels[i]);
		}
		
		JPanel targetPanel = new JPanel();
		targetPanel.setBorder(BorderFactory.createTitledBorder("Target Node [" + (NR_NODES - 1) + "]"));
		targetBox = new JTextArea(TARGETBOX_ROWS,TARGETBOX_COLS);
		targetBox.setEditable(false);
		targetPanel.add(targetBox);
		
		JPanel buttonPanelCompound = new JPanel();
		buttonPanelCompound.setLayout(new BoxLayout(buttonPanelCompound, BoxLayout.Y_AXIS));
		
		JPanel buttonPanel = new JPanel();	
		
		start = new JButton("Start");
		start.addActionListener(this);
		start.setPreferredSize(DIM_BUTTON);
		buttonPanel.add(start);

		stop = new JButton("Stop");		
		stop.addActionListener(this);
		stop.setPreferredSize(DIM_BUTTON);
		buttonPanel.add(stop);

		test = new JButton("Test");
		test.addActionListener(this);
		test.setPreferredSize(DIM_BUTTON);
		buttonPanel.add(test);
		
		
		
		JPanel messagePanel = new JPanel();
		messagePanel.setBorder(BorderFactory.createTitledBorder("Enter your message here"));
		messageBox = new JTextField(25);
		messageBox.setText(DEFAULT_TEXT);
		messagePanel.add(messageBox);

		
		buttonPanelCompound.add(buttonPanel);
		buttonPanelCompound.add(messagePanel);				

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createTitledBorder("Packet Switcher"));
		
		mainPanel.add(buttonPanelCompound, BorderLayout.NORTH);
		mainPanel.add(boxPanel, BorderLayout.CENTER);
		mainPanel.add(targetPanel, BorderLayout.SOUTH);

		this.add(mainPanel);
	}
	public void actionPerformed(ActionEvent e)
	{	
		if (e.getSource()==start){ this.startSwitching(); }		

		if (e.getSource()==stop){ switcher.setFinish(); }

		if (e.getSource()==test){ this.test(); }

	}

	private void startSwitching()
	{		
		String message = messageBox.getText().trim();
		
		if (message.length() == 0){
			JOptionPane.showMessageDialog( this, "Please Enter at least one character!" );
			return;
		}
		
		switcher = new PacketSwitcher(NR_NODES, message, this);
		switcher.setDaemon(true);
		switcher.start();
	}

	public synchronized void setBoxContent(int i, Packet packet) { nodeBoxes[i].setText("\n   " + packet.getPart()); }
	
	public synchronized void appendTargetContent(String part) { targetBox.setText("\n   " + part + " "); }
	
	public synchronized void clearBoxContent(int i) { nodeBoxes[i].setText(""); }

	public void test()
	{
		System.err.println("SW" + switcher.getState());		
		switcher.testKill();
	}
}
