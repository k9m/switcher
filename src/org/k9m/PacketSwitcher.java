package org.k9m;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class PacketSwitcher extends Thread
{	
	private static final int SLEEP_INTERVAL_MILLISEC = 50;

	Packet[] message;

	Node[] network;

	Gui gui;



	TargetNode target;

	boolean keepGoing = true;	

	public PacketSwitcher(int nrNodes, String messageString, Gui gui)
	{	
		this.network = new Node[nrNodes];
		this.gui = gui;
		
		String[] messageChunks = messageString.split(" ");
		message = new Packet[messageChunks.length];
		
		for (int i = 0; i < messageChunks.length; i++){
			
			message[i] = new Packet(i, messageChunks[i]);			
		}
	}



	public void setFinish() { keepGoing = false;	}	

	@Override
	public void run()
	{
		this.switching();

		final int networkSize = network.length;

		while(!target.getComplete()){			

			try {
				Thread.sleep(SLEEP_INTERVAL_MILLISEC);
				
				for (int n=0; n < network.length; n++){
					synchronized(network[n]){
						network[n].notify();
					}
				}
				
				Thread.sleep(SLEEP_INTERVAL_MILLISEC);

				for (int n=0; n < network.length; n++){

					if ( n < networkSize -1){
						if (!network[n].empty())
							gui.setBoxContent(network[n].getNumber(), network[n].getStore());
						else
							gui.clearBoxContent(network[n].getNumber());
					}
					
					Packet[] message = target.getMessage();
					String msg = "";
					for (int i = 0; i < message.length; i++)
						if (message[i] != null)
							msg += message[i].getPart() + " ";
					
					gui.appendTargetContent(msg);
				}


			} catch (InterruptedException e) {				
				e.printStackTrace();
			}

			if (!keepGoing){
				killThreads();

				System.err.println("Network shut down, User Interrupt");
				return;
			}			
		}

		killThreads();				
	}	


	public void killThreads()
	{		
		boolean noneAlive = false;
		int counter = 0;

		while(!noneAlive){

			noneAlive = true;

			for (int n=0; n < network.length; n++){
				if ( !(network[n].getState() == Thread.State.TERMINATED) ){			
					network[n].interrupt();
					noneAlive = false;
				}
			}

			System.err.println(counter++);
		}

		testKill();
		System.err.println("Network shut down, Message Recieved ");
		return;		
	}

	public void testKill()
	{		
		for (int n=0; n < network.length; n++)
			System.err.println(network[n].getState());				
	}

	private void switching()
	{
		for (int p=0; p < message.length; p++)
			System.err.print( " " + message[p].getPart() );
		System.err.println("\n");

		// set up the non-target nodes
		for (int n=0; n < network.length-1; n++)
			network[n] = new Node(n, network, gui);

		// set up the target node at the end of the array
		target = new TargetNode(network.length-1,message.length);
		System.err.println(target.getName());
		network[network.length - 1] = target;

		for (int n=0; n < network.length; n++)
			network[n].start();

		for (int n=0; n < message.length; n++)
			network[n].receive(message[n]);
	}	
}



