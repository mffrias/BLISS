package symbolicheap.bounded.fieldBasedTesting.sortedlist;

import java.io.Serializable;

public class SinglyLinkedListNode implements Serializable {

	private static final long serialVersionUID=6495900899527469882L;

	public /*@ nullable @*/ SinglyLinkedListNode next;
	
	public /*@ nullable @*/ int value;

	public transient int _index;
	
}
