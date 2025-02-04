package symbolicheap.bounded;


public class LinkedListNode {


	// Extra fields to support execution of hybridRepOK
	public static final LinkedListNode SYMBOLICLINKEDLISTNODE = new LinkedListNode();
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE;
	public static final Integer SYMBOLICINTEGER = Integer.MIN_VALUE;

	// Hybrid invariant that tolerates partially symbolic structures

	public LinkedListNode(){}
	
	
	
	public LinkedListNode(int e, LinkedListNode next, LinkedListNode prev) {
		this.next = next;
		this.prev = prev;
		this.value = e;
	}



	public LinkedListNode next, prev;
	
	public int value;
}


 // ~~~~~~~~~~~~~~~~~ End of class ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

