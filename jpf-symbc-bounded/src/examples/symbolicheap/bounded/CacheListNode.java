package symbolicheap.bounded;

public class CacheListNode extends Object {

	// Extra fields to support execution of hybridRepOK
	public static final CacheListNode SYMBOLICCACHELISTNODE = new CacheListNode();
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE;
	
	public CacheListNode listNext;

	public CacheListNode listPrevious;

	public int nodeValue;
}