package symbolicheap.bounded.fieldBasedTesting.sortedlist;

import java.io.Serializable;


/**
 *StrictlySortedSinglyLinkedList's nodes
 *@author
 */

public  class Node implements Serializable{

	public static final Node SYMBOLICNODE = new Node(); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.

	public  static final long serialVersionUID = 2; 
	public int value;

	public transient int _index;
	public Node next;

	public String toString() {
		return "[" + value + "]";
	}


		
	
}
