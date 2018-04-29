package org.k9m.switcher;

import org.k9m.switcher.gui.Gui;

import javax.swing.JFrame;

public class Main {
	
	public static void main(String[] args) {
		
		Gui gui = new Gui();
		gui.pack();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);	
		gui.setResizable(false);

	}

}
