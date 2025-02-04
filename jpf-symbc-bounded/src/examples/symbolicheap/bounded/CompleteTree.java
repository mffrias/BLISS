package symbolicheap.bounded;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;
import symbolicheap.bounded.HeapNode;


public class CompleteTree {

	// Stubs to be intercepted at the bytecode level
	public void countStructure(CompleteTree root) {}
	public void dumpStructure(CompleteTree root) {}

	// Extra fields to support execution of hybridRepOK
	public static final CompleteTree SYMBOLICCOMPLETETREE = new CompleteTree();
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE;


	public HeapNode root;
	
	public HeapNode nodeWithEmptySlot;
	
	public int size;
		
	
	
	// Hybrid invariant that tolerates partially symbolic structures

	public boolean hybridRepOK() {
		
		if (this == SYMBOLICCOMPLETETREE)
			return true;

		Set<HeapNode> visited = new HashSet<HeapNode>();
		List<HeapNode> worklist = new ArrayList<HeapNode>();

		HeapNode node = this.root;
		
		if (node == null || node == HeapNode.SYMBOLICHEAPNODE )
			return true;
		
		if (node.parent != null){
			return false;
		}

		visited.add(node);
		worklist.add(node);
		
		while (!worklist.isEmpty()) {
			node = worklist.remove(0);

			if (node == null || node == HeapNode.SYMBOLICHEAPNODE)
				break;
			
			
			HeapNode left = node.left;
			if (left != null && left != HeapNode.SYMBOLICHEAPNODE) {
				if (!visited.add(left) || left.parent != node)
					return false;
			}
			worklist.add(left);

			HeapNode right = node.right;
			if (right != null && right != HeapNode.SYMBOLICHEAPNODE) {
				if (!visited.add(right) || right.parent != node)
					return false;
			}
			worklist.add(right);
		}
		
		while (!worklist.isEmpty() && node != HeapNode.SYMBOLICHEAPNODE){
			node = worklist.remove(0);
			if (node != null && node != HeapNode.SYMBOLICHEAPNODE)
				return false;
		}

		return visited.size() == this.size && this.size <= LIMIT;
	}

	
	
	public boolean repOK_Concrete(){
		Set<HeapNode> visited = new HashSet<HeapNode>();
		List<HeapNode> worklist = new ArrayList<HeapNode>();

		HeapNode node = this.root;
		
		if (node == null)
			return true;

		visited.add(node);
		worklist.add(node);
		
		while (!worklist.isEmpty()) {
			node = worklist.remove(0);

			if (node == null)
				break;
			
			HeapNode left = node.left;
			if (left != null && (!visited.add(left) || node.key < left.key || left.parent != node)) {
				return false;
			}
			worklist.add(left);

			HeapNode right = node.right;
			if (right != null && (!visited.add(right) || node.key < right.key || right.parent != node)) {
				return false;
			}
			worklist.add(right);
		}
		
		while (!worklist.isEmpty()){
			node = worklist.remove(0);
			if (node != null)
				return false;
		}

		return visited.size() == this.size && this.size <= LIMIT;
	}
	
	
	
	
	
	
	private static int LIMIT = 5;
	
	public static void main(String[] args) {
		CompleteTree X = new CompleteTree();
		X = (CompleteTree) Debug.makeSymbolicRef("X", X);
//		X = (AvlTree) Debug.makeSymbolicRefBounded("X", X);

//		HeapNode X1 = new HeapNode();
//		HeapNode X2 = new HeapNode();
//		HeapNode X3 = new HeapNode();
//		HeapNode X4 = new HeapNode();
//		HeapNode X5 = new HeapNode();
//
//		X.root = X1;
//		X.size = 5;
//		X1.parent = null;
//		X1.left = X2;
//		X1.right = X3;
//		X2.parent = X1;
//		X3.parent = X1;
//		X2.left = X4;
//		X2.right = X5;
//		X4.parent = X2;
//		X5.parent = X2;
//		X3.left = null;
//		X3.right = null;
//		X4.left = null;
//		X4.right = null;
//		X5.left = null;
//		X5.right = null;
//		X1.key = 50;
//		X2.key = 40;
//		X3.key = 30;
//		X4.key = 20;
//		X5.key = 10;
		
		
		if (X != null && X.repOK_Concrete()) {
			X.countStructure(X);
		}

	}	
	
} // ~~~~~~~~~~~~~~~~~ End of class ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



