package org.k9m;
public class Node extends Thread
{	
	int number;

	Node[] network;

	Packet store = null;

	Gui gui;

	public Node(int n)
	{	
		super("T[" + n + "]");
		number = n;		 
	}

	public Node(int n, Node[] nw, Gui gui )
	{	
		this(n);		
		network = nw;
		this.gui = gui;




		//gui.clearBoxContent(0);
	}

	public void run()
	{	

		System.err.println( this.getName() +  " has been started" );
		try
		{	
			while (true){

				synchronized(this){
					this.wait();
				}

				int next = (int) (Math.random() * (network.length));
				Node sendto = network[next];					

				if (sendto.empty() && store != null){

					sendto.receive(store); 	// send to non-empty node
					store = null;
				}
				
			}
		}
		catch (InterruptedException e){			
			System.err.println( this.getName() +  " has been stopped packet: ");	
			return;
		}		

	}

	public synchronized void receive( Packet messagePart )
	{	
		System.err.println( this.getName() +  " has received packet " + messagePart.getPartNumber());
		store = messagePart;		

		//gui.setBoxContent(0, store, "OO");
	}	

	public synchronized boolean empty() { return (store == null); }

	public synchronized Packet getStore() { return store; }

	public synchronized int getNumber()	{ return number;	}

	public synchronized Node[] getNetwork()	{ return network;	}

	public synchronized String toString()	{ return "Node " + number;	}

}

