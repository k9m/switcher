package org.k9m.switcher.model;

public class TargetNode extends Node {

	Packet[] message;

	Packet dummy = new Packet( -1, "???" );

	int got;

	boolean complete;


	public TargetNode(int number, int nrPackets)
	{	
		super(number);	// must force the Node initialising to occur		

		this.number = number;	

		got = 0;		

		complete = false;

		message = new Packet[nrPackets];

		for (int p=0; p<message.length; p++)
			message[p] = dummy;
	}





	@Override
	public void run()
	{	
		try
		{
			while (got < message.length)	// message incomplete
			{	
				synchronized(this){
					this.wait();
				}

				if (store != null){
					message[store.getPartNumber()] = store;
					store = null;
					got++;				

					System.err.println("Target Node recieved message: " + got);
				}
			}
		}
		catch (InterruptedException e){
			System.err.println( "Target Node" +  " has been stopped" );
			return;
		}

		System.err.println( "\nThe target has received the whole message\n");
		complete = true;
	}	

	public boolean getComplete() { return complete; }
	
	public Packet[] getMessage() { return message; }




}




