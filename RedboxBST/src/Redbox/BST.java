/*
 * Milan Gulati
 */

package Redbox;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class BST
{
	private Node root;				//member variable - node pointer
	
	//constructor
	BST() {}
	
	//mutator
	public void setRoot(Node rt) {root = rt;}
	
	//accessor
	public Node getRoot() {return root;}
	
	/*
	 * insert function
	 * the insert function calls a recursive helper function
	 * the helper function will seed the binary tree
	 */
	public void insert(String title, int available, int rented)
	{
		root = helpRec(root, title, available, rented);
	}
	
	public Node helpRec(Node root, String title, int available, int rented)
	{
		if(root == null)
		{
			root = new Node (title, available, rented);
			return root;
		}
		
		int compare = title.compareTo(root.getTitle());
		
		if(compare > 0)			//title is larger than roots title - set right
		{
			root.setRight(helpRec(root.getRight(), title, available, rented));
		}
		else if(compare < 0)	//title is smaller than roots title - set left
		{
			root.setLeft(helpRec(root.getLeft(), title, available, rented));
		}
		
		return root;
	}
	
	/*
	 * search function
	 * takes root node and title looking for as parameters
	 * will compare the title to the root title
	 * if same as root title then it returns the root
	 * otherwise it will recur down the tree until the proper node is found
	 */
	public Node search(Node root, String title)
	{	
		//check if root is null before comparing titles in case the node does not exist
		//would cause null pointer exception if so
		if(root == null)
		{
			return root;
		}
		
		//if the current node's title is equal to the title searching for
		if(title.compareTo(root.getTitle()) == 0)
		{
			return root;
		}
		
		//if the title is alphabetically larger than the current node's title
		//then recur down the right subtree and look for it
		if(title.compareTo(root.getTitle()) > 0)
		{
			return search(root.getRight(), title);
		}
		
		//otherwise, recur down the left subtree
		return search(root.getLeft(), title);
	}
	
	
	/*
	 * delete function
	 * takes title as a parameter
	 * passes root and title to a recursive helper function
	 */
	public void delete(String title)
	{
		root = helpDelete(root, title);
	}
	
	public Node helpDelete(Node root, String title)
	{
		//base case when the tree is empty
		if(root == null)
			return root;
		
		//if tree is not empty, recur down tree until matching titles
		int compare = title.compareTo(root.getTitle());
		
		if(compare < 0)			//title is smaller than root title
			root.setLeft(helpDelete(root.getLeft(), title));
		
		else if(compare > 0)	//title is larger than root title
			root.setRight(helpDelete(root.getRight(), title));
		
		else					//title is the same as the root title - delete this node
		{
			//if node has 0 or 1 child
			if(root.getLeft() == null)
				return root.getRight();
			
			else if(root.getRight() == null)
				return root.getLeft();
			
			//if node has two leafs
			//get the smaller value node form the right
			root.setTitle(smallTitle(root.getRight()));
			
			//delete recursively back up tree
			root.setRight(helpDelete(root.getRight(), root.getTitle()));
		}
		
		return root;
	}
	
	//returns the smallest title (alphabetically) in a tree
	//starts at the passed parameter Node n
	public String smallTitle(Node n)
	{
		String title = n.getTitle();
		while(n.getLeft() != null)
		{
			title = n.getLeft().getTitle();
			n = n.getLeft();
		}
		
		return title;
	}
	
	/*
	 * function seeds the binary search tree using the inventory.dat file
	 * goes through each line of file
	 * calls createNode function to create node at that index
	 * adds node to Binary Search Tree
	 */
	public static BST createTree() throws IOException
	{
		BST tree = new BST();
		Scanner input = new Scanner(new File("inventory.dat"));
		
		while(input.hasNextLine())			//while loop - traverse each line of inventory file
		{
			String temp = input.nextLine();	//take in line as a temporary string
			
			Node tNode = createNode(temp);	//pass temp string to create node function
			
			//add new node to BST via insert function
			String tTitle = tNode.getTitle();
			int tAvail = tNode.getAvail();
			int tRent = tNode.getRented();
			
			tree.insert(tTitle, tAvail, tRent);
		}
		
		input.close();
		return tree;
	}
	
	
	/*
	 * inOrder traversal function - USED FOR TESTING
	 * traverses and prints each element in the BST in order
	 * (left, root, right)
	 */
	public void inOrder(Node n)
	{
		if(n == null)
			return;
		
		inOrder(n.getLeft());
		
		System.out.print(n.getTitle() + " ");
		
		inOrder(n.getRight());
	}
	
	/*
	 * function writes the output file for the program redbox_kiosk.txt
	 * traverse the function in alphabetical order - using an inOrder traversal
	 * the traversal is iterative not recursive - can't be iterative because of the printwriter
	 * print the current information at the node in a formatted line
	 * close the file
	 */
	public void writeReport(Node root) throws IOException
	{
		File output = new File("redbox_kiosk.txt");
		PrintWriter print = new PrintWriter(output);
		
		if (root == null)
		{
        	print.close();						//close the file
            return;
        }
  
        Node c = root;							//node pointer keeps track of current node
        Stack<Node> stack = new Stack<Node>(); 	//stack used to traverse the tree iteravtively
  
        while (stack.size() > 0 || c != null)	//while stack size is not zero and the current node is not null
        { 
            while (c !=  null) 
            { 
            	stack.push(c); 					//push current node onto the stack
            	c = c.getLeft();				//set current to the left node
            } 
  
            c = stack.pop();					//pop the current node
            
            //if the available and rented are not empty
            //print the given nodes information to the file
            if(c.getAvail() != 0 || c.getRented() != 0)
            {
            	print.printf("%-30s%10d%10d", c.getTitle(), c.getAvail(), c.getRented());
            	print.println();
            }
            
            c = c.getRight(); 					//now go to right subtree
        }
        
        print.close();
	}
	
	
	/*
	 * function takes line of inventory file as parameter
	 * parses line into title, quantity available, and quantity rented
	 * calls node constructor and returns node
	 */
	public static Node createNode(String line)
	{
		String title;
		int available, rented;
		
		String[] split = line.split(",");
		
		title = split[0].substring(1, split[0].length() - 1);	//remove "" from title and assign to title
		available = Integer.parseInt(split[1]);					//parse into int and assign to available
		rented = Integer.parseInt(split[2]);					//parse into int and assign to rented
		
		return new Node(title, available, rented);				//return newly created node for use elsewhere
	}
}
