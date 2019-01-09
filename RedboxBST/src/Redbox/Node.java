/*
 * Milan Gulati
 */

package Redbox;

public class Node extends Payload
{
	Node left;			//left node pointer
	Node right;			//right node pointer
	
	//overloaded constructor
	Node(String title, int avail, int rented)
	{
		super(title, avail, rented);
		left = null;
		right = null;
	}
	
	//mutator functions

	public void setLeft(Node l) {left = l;}
	
	public void setRight(Node r) {right = r;}
	
	//accessor functions
	
	public Node getLeft() {return left;}
	
	public Node getRight() {return right;}
}
