package org.k9m.switcher.model;
public class Packet 
{	
	private int partNumber;
	private String part;

	public Packet( int PN, String P)
	{	partNumber = PN; 
		part = P;
	}

	public void setPartNumber( int PN )	{ partNumber = PN; }

	public int getPartNumber() { return partNumber; }

	public void setPart( String P )	{ part = P; }

	public String getPart() { return part; }

	public String toString(){ return "Packet " +partNumber +" containing " + part; }
}
