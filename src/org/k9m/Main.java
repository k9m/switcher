package org.k9m;

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
