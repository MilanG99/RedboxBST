# RedboxBST

### Project Description
This java project displays a inventory of a redbox kiosk.  The program loads an inventory data file containing the name of all DVD titles
and the amount of those titles available and those rented and sorts each title into a binary search tree.  After processing the inventory 
file, a transaction log is processed and the BST is updated based on those transactions (transactions include the command rent, add, return
or remove, the title, and the number of DVD's).  If an invalid line is read from the transaction log it is skipped over and placed into an
error file.  After all transactions have been processed, a report redbox_kiosk.txt is generated and the program closes.
