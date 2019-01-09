/*
 * Milan Gulati
 */

package Redbox;

import java.io.*;
import java.util.*;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		//create a new BST
		//read and process the inventory.dat file
		//read and process the transaction.log file
		//finally, call the print errors function - will create the file and print all errors if any
		
		BST test = new BST();

		processInventory(test);
		processTransactions(test);
		
//		test.inOrder(test.getRoot());
		test.writeReport(test.getRoot());
	}
	
	public static void processInventory(BST tree) throws IOException
	{
		//while loop go through inventory file
		//determine the title
		//determine the number available and the number rented
		//add the title to the BST
		
		Scanner in = new Scanner(new File("inventory.dat"));
		
		while(in.hasNextLine())
		{
			String line = in.nextLine();
			
			String title;
			int avail, rented;
			
			//remove first quotation mark
			title = line.substring(1, line.length());
			
			//split the string into title and after title
			int splitIndex = title.indexOf("\"");										//second quote mark
			String afterTitle = title.substring(splitIndex + 1, title.length());		//find the string that is after title -> ,_,_
			title = title.substring(0, splitIndex);										//finally find the title
			
			//now left with after title -> ,_,_
			//first remove first comma
			afterTitle = afterTitle.substring(1, afterTitle.length());
			
			//now left with after title -> _,_
			//find new split index
			splitIndex = afterTitle.indexOf(",");										//comma between available and rented
			String sAvail = afterTitle.substring(0, splitIndex);						//string of number available
			String sRented = afterTitle.substring(splitIndex + 1, afterTitle.length());	//string of number rented
			
			avail = Integer.parseInt(sAvail);		//find integer from string avail
			rented = Integer.parseInt(sRented);		//find integer from string rented
			
			//now that all info is calculated
			//create and insert the new node
			tree.insert(title, avail, rented);
		}
		
		in.close();
	}
	
	public static void processTransactions(BST tree) throws IOException
	{
		//while loop go through transaction file
		//pass line to find type function
		//	case 1 - add
			//add title if nonexistent
		//	case 2 - remove
			//check that title exists
			//if not then error line
		//	case 3 - rent
			//check that title exists
			//if not then error line
		//	case 4 - return
			//check that title exists
			//if not then error line
		//	default - error line
		
		Scanner ts = new Scanner(new File("transaction.log"));
		
		ArrayList<String> errors = new ArrayList<String>(); 	//holds error lines - pass to print error function after while loop
		
		while(ts.hasNextLine())							//while loop - traverse each line of transaction log
		{
			boolean validFormat = false;
			boolean exists = false;						//variable marks true if the title in the line exists in the BST
			String line = ts.nextLine();				//take in line as a temporary string
			
			int type = transactionType(line);
			
			validFormat = validLine(line);				//pass line to valid format function
			if(!validFormat)							//if line is not a valid format
			{
				errors.add(line);						//add the line to the error list
				continue;								//continue to the next iteration of the while loop
			}
			
			//if title does not exist and we are not adding title
			//then assume that an error exists on the line processing
			exists = titleExists(tree, line);
			if(exists == false && type != 1)
				type = -1;
			
			String title;
			int num;
			
			switch(type)
			{
			case 1:								//add
				exists = titleExists(tree, line);	//still must check if title exists in order to add it to the BST
				
				if(exists)						//if it exists just add to the available node
				{
					title = findTitle(line);
					num = findNumber(line);
					
					Node ad = tree.search(tree.getRoot(), title);
					
					ad.add(num);
				}
				else							//else doesn't exist then create a new node and set the available to the entered amount
				{
					title = findTitle(line);
					num = findNumber(line);
					
					tree.insert(title, num, 0);	//create a new node with title and number added from line
				}
				break;
				
			case 2:								//remove
				
				title = findTitle(line);
				num = findNumber(line);
				
				Node rem = tree.search(tree.getRoot(), title);
				
				//check if remove amount is larger than the available + rented
				//if so then just delete the node
				
				if(num > (rem.getAvail() + rem.getRented()))
				{
					tree.delete(title);
				}
				else
					rem.remove(num);
				
				break;
				
			case 3:								//rent
				
				title = findTitle(line);
				
				Node rent = tree.search(tree.getRoot(), title);
				
				rent.rent();
				
				break;
				
			case 4:								//return
				
				title = findTitle(line);
				
				Node retur = tree.search(tree.getRoot(), title);
				
				retur.ret();
				
				break;
			
			default:							//error
				
				errors.add(line);				//add line to the error array and go to next line
				
				break;
			}
		}
		
		ts.close();
		
		//now pass errors array list to the print errors function
		printErrors(errors);
	}
	
	public static int transactionType(String line)
	{
		//go through line and determine what the transaction type is
		
		String command;
		int splitIndex = 0;
		
		try
		{
			splitIndex = line.indexOf(" ");				//find index of first space
			command = line.substring(0, splitIndex);		//find substring from beginning of line until first space - is the command
		}
		catch(Exception e)
		{
			return -1;
		}
		command = line.substring(0, splitIndex);		//find substring from beginning of line until first space - is the command
		
		if(command.equals("add"))			//return 1 for add
			return 1;
		else if(command.equals("remove"))	//return 2 for remove
			return 2;
		else if(command.equals("rent"))		//return 3 for rent
			return 3;
		else if(command.equals("return"))	//return 4 for return
			return 4;
		else return -1;						//return -1 otherwise = an error line
	}
	
	public static boolean titleExists(BST tree, String line)
	{
		//first find the part of the string after the command
		int splitIndex = line.indexOf(" ");
		String afterCmd = line.substring(splitIndex, line.length());
		
		//now find the string that starts with " and ends with "
		//rent and return are fine without this but needed for remove case
		String title = afterCmd.substring(2, afterCmd.length());
		splitIndex = title.indexOf("\"");
		title = title.substring(0, splitIndex);
		
		//now search for title
			//if found return true
			//else not found return false
		Node test = tree.search(tree.getRoot(), title);
		
		if(test != null)
			return true;
		return false;
	}
	
	public static String findTitle(String line)
	{
		//first find the part of the string after the command
		int splitIndex = line.indexOf(" ");
		String afterCmd = line.substring(splitIndex, line.length());
		
		//now find the string that starts with " and ends with "
		//start title after index 2, being the space and the "
		String title = afterCmd.substring(2, afterCmd.length());
		splitIndex = title.indexOf("\"");
		title = title.substring(0, splitIndex);
		
		return title;
	}
	
	public static int findNumber(String line)
	{
		//first find the part of the string after the command
		int splitIndex = line.indexOf(" ");
		String afterCmd = line.substring(splitIndex, line.length());
		
		//now seperate the title from the number
		String number = afterCmd.substring(2, afterCmd.length());	//trim space and remaining "
		splitIndex = number.indexOf(",");							//the split index is at the ,
		number = number.substring(splitIndex + 1, number.length());	//go from , to end of remaining string
		
		//now convert string into a returnable integer
		int num = Integer.parseInt(number);
		
		return num;
	}
	
	public static boolean validLine(String line)
	{
		int currIndex = 0;
		
		//check if add_ is in the command line
			//return true
		//else if remove_
		//else if rent_
		//esle if return_
		
		if(line.contains("add \""))
		{
			currIndex = 5;
		}
		else if(line.contains("remove \""))
		{
			currIndex = 8;
		}
		else if(line.contains("rent \""))
		{
			currIndex = 6;
		}
		else if(line.contains("return \""))
		{
			currIndex = 8;
		}
		else
			return false;
		
		//if the line is not rent or return must check for the number format at the end of the line
		if(!line.contains("rent") && !line.contains("return"))
		{
			//the command and first quote must exist if made it this far
			//now reduce the line so it goes up to the first letter of the title
			//then check if the line contains a quotation mark and a comma
			line = line.substring(currIndex, line.length());
			if(!line.contains("\","))
				return false;
			
			//still valid so far, now confirm that just a number resides after the comma
			//get the index of the comma and reduce the string
			currIndex = line.indexOf(",");
			String num = line.substring(currIndex + 1, line.length());
			
			//now check if the string num can be converted to a num
			//if so then return true
			try
			{
				int number = Integer.parseInt(num);
				return true;
			}
			catch(NumberFormatException e)
			{
				return false;
			}
		}
		
		//else it must be an add or remove command, so just check for the second quote
		else
		{
			line = line.substring(currIndex, line.length());
			if(!line.contains("\""))
				return false;
			
			//line looks like this now -> title"
			//now check for whitespace after the last "
			currIndex = line.indexOf("\"");
			if(currIndex + 1 != line.length())
				return false;
			
			return true;
		}
	}
	
	
	public static void printErrors(ArrayList<String> errors) throws IOException
	{
		int size = errors.size();											//find the size of the array list
		
		PrintWriter print = new PrintWriter(new File("error.log"));		//print writer to customer file
		
		/*
		 * use a for loop to traverse the error arrayList
		 * print the current string to the file
		 */
		for(int i = 0; i < size; i++)
			print.println(errors.get(i));
		
		print.close();
	}
}
