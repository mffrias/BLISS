package symbolicheap.bounded;

public class HeapNode {
	
	public static final HeapNode SYMBOLICHEAPNODE = new HeapNode(); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.

	public int key;

	public HeapNode left = null;

	public HeapNode right = null;
	
	public HeapNode parent = null;


	public HeapNode(){}
	
	public HeapNode(int key, HeapNode left, HeapNode right, HeapNode parent) {
		this.key = key;
		this.left = left;
		this.right = right;
		this.parent = parent;
	}

	public int getKey() {
		return key;
	}

}