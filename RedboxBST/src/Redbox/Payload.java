/*
 * Milan Gulati
 */

package Redbox;

public class Payload 
{
	//member variables
	String title;		//movie title
	int available;		//quantity available
	int rented;			//quantity rented
	
	Payload(String t, int a, int r)
	{
		title = t;
		available = a;
		rented = r;
	}
	
	//mutator functions
	
	public void setTitle(String t) {title = t;}
	
	public void setAvail(int a) {available = a;}
	
	public void setRented(int rent) {rented = rent;}

	//accessor functions
	
	public String getTitle() {return title;}
	
	public int getAvail() {return available;}

	public int getRented() {return rented;}
	
	/*
	 * add, remove, rent, and return functions
	 * edits the available and rented values for the payload of a node
	 */
	public void add(int numAdd) {available = available + numAdd;}
	
	public void remove(int numRemove)
	{
		if(numRemove > available)
		{
			numRemove = numRemove - available;
			available = 0;
			rented = rented - numRemove;
		}
		else
		{
			available = available - numRemove;
		}
	}
	
	public void rent()
	{
		available--;
		rented++;
	}
	
	public void ret()
	{
		available++;
		rented--;
	}
}
