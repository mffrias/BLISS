package symbolicheap.bounded;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

public class AvlTreeOldButGood {

	// Stubs to be intercepted at the bytecode level
	public void countStructure(AvlTreeOldButGood root) {}
	public void dumpStructure(AvlTreeOldButGood root) {}

	// Extra fields to support execution of hybridRepOK
	public static final AvlTreeOldButGood SYMBOLICAVLTREE = new AvlTreeOldButGood();
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE;

	// Hybrid invariant that tolerates partially symbolic structures

	public boolean hybridRepOK() {
		if (this == SYMBOLICAVLTREE)
			return true;

		Set<AvlTreeOldButGood> visited = new HashSet<AvlTreeOldButGood>();
		List<AvlTreeOldButGood> worklist = new ArrayList<AvlTreeOldButGood>();

		visited.add(this);
		worklist.add(this);

		while (!worklist.isEmpty()) {
			AvlTreeOldButGood node = worklist.remove(0);

			AvlTreeOldButGood left = node.left;
			if (left != null && left != SYMBOLICAVLTREE) {
				if (!visited.add(left))
					return false;

				worklist.add(left);
			}

			AvlTreeOldButGood right = node.right;
			if (right != null && right != SYMBOLICAVLTREE) {
				if (!visited.add(right))
					return false;

				worklist.add(right);
			}

			if (height != SYMBOLICINT && left != SYMBOLICAVLTREE && right != SYMBOLICAVLTREE
					&& (left == null || left.height != SYMBOLICINT)
					&& (right == null || right.height != SYMBOLICINT)) {
				int lh = (left == null) ? -1 : left.height;
				int rh = (right == null) ? -1 : right.height;
				int diff = lh - rh;
				if (diff < -1 || diff > 1)
					return false;  // unbalanced!
				int maxh = (lh > rh) ? lh : rh;
				if (height != 1 + maxh)
					return false; // wrong value in height field!
			}
		}

		return visited.size() <= LIMIT;
		//		return true;
	}


	// Private members

	public int element;
	public int height;
	public AvlTreeOldButGood left;
	public AvlTreeOldButGood right;


	// Constructors

	public AvlTreeOldButGood() {
		this.element = 0;
		this.height = 0;
		this.left = this.right = null;
	}

	public AvlTreeOldButGood(int element) {
		this.element = element;
		this.height = 1;
		this.left = this.right = null;
	}

	public AvlTreeOldButGood(int element, int height, AvlTreeOldButGood left, AvlTreeOldButGood right) {
		this.element = element;
		this.height = height;
		this.left = left;
		this.right = right;
	}


	// Projectors

	public AvlTreeOldButGood left() {
		return left;
	}

	public AvlTreeOldButGood right() {
		return right;
	}



	// ========= Methods to be verified ===================================


	// ~~~~~~~~~ Begin findMinimum ~~~~~~~~~~

	public int findMinimum(AvlTreeOldButGood root) {
		assert(root != null);
		AvlTreeOldButGood curr = root;
		while (curr.left() != null) {
			curr = curr.left();
		}
		return curr.element;
	}

	// ~~~~~~~~~ End of findMinimum ~~~~~~~~~~



	// ~~~~~~~~~ Begin contains ~~~~~~~~~~

	public boolean contains(AvlTreeOldButGood root, int x) {
		AvlTreeOldButGood p = root;
		while (p != null) {
			//			Verify.ignoreIf(!repOK(root, LIMIT));
			if (x == p.element) {
				return true;
			} else if (x < p.element) {
				p = p.left;
			} else {
				p = p.right;
			}
		}
		return false;
	}

	// ~~~~~~~~~ End of contains ~~~~~~~~~~



	// ~~~~~~~~~ Begin insert ~~~~~~~~~~

	public AvlTreeOldButGood insert(AvlTreeOldButGood root, int x) {
		AvlTreeOldButGood t = root;

		if (t == null) {
			t = new AvlTreeOldButGood(x);
		} else if (x < t.element) {
			t.left = insert(t.left, x);
			if (AvlTreeOldButGood.height(t.left) - AvlTreeOldButGood.height(t.right) == 2) {
				if (x < t.left.element) {
					t = AvlTreeOldButGood.rotateWithLeftChild(t);
				} else {
					t = AvlTreeOldButGood.doubleWithLeftChild(t);
				}
			}
		} else if (x > t.element) {
			t.right = insert(t.right, x);
			if (AvlTreeOldButGood.height(t.right) - AvlTreeOldButGood.height(t.left) == 2) {
				if (x > t.right.element) {
					t = AvlTreeOldButGood.rotateWithRightChild(t);
				} else {
					t = AvlTreeOldButGood.doubleWithRightChild(t);
				}
			}
		} else {
			; // Duplicate; do nothing
		}
		t.height = AvlTreeOldButGood.max(AvlTreeOldButGood.height(t.left), AvlTreeOldButGood.height(t.right)) + 1;
		return t;
	}


	/**
	 * Return the height of t, or -1 if null.
	 */
	private static int height(AvlTreeOldButGood t) {
		return t == null ? -1 : t.height;
	}

	/**
	 * Return maximum of two ints.
	 */
	private static int max(int lhs, int rhs) {
		return lhs > rhs ? lhs : rhs;
	}

	/**
	 * Double rotate binary tree node: first left child with its right child; then
	 * node k3 with new left child. For AVL trees, this is a double rotation for
	 * case 2. Update heights, then return new root.
	 */
	private static AvlTreeOldButGood doubleWithLeftChild(final AvlTreeOldButGood k3) {
		k3.left = AvlTreeOldButGood.rotateWithRightChild(k3.left);
		return AvlTreeOldButGood.rotateWithLeftChild(k3);
	}

	/**
	 * Double rotate binary tree node: first right child with its left child; then
	 * node k1 with new right child. For AVL trees, this is a double rotation for
	 * case 3. Update heights, then return new root.
	 */
	private static AvlTreeOldButGood doubleWithRightChild(final AvlTreeOldButGood k1) {
		k1.right = AvlTreeOldButGood.rotateWithLeftChild(k1.right);
		return AvlTreeOldButGood.rotateWithRightChild(k1);
	}

	/**
	 * Rotate binary tree node with left child. For AVL trees, this is a single
	 * rotation for case 1. Update heights, then return new root.
	 */
	private static AvlTreeOldButGood rotateWithLeftChild(final AvlTreeOldButGood k2) {
		final AvlTreeOldButGood k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.height = AvlTreeOldButGood.max(AvlTreeOldButGood.height(k2.left), AvlTreeOldButGood
				.height(k2.right)) + 1;
		k1.height = AvlTreeOldButGood.max(AvlTreeOldButGood.height(k1.left), k2.height) + 1;
		return k1;
	}

	/**
	 * Rotate binary tree node with right child. For AVL trees, this is a single
	 * rotation for case 4. Update heights, then return new root.
	 */
	private static AvlTreeOldButGood rotateWithRightChild(final AvlTreeOldButGood k1) {
		final AvlTreeOldButGood k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		k1.height = AvlTreeOldButGood.max(AvlTreeOldButGood.height(k1.left), AvlTreeOldButGood
				.height(k1.right)) + 1;
		k2.height = AvlTreeOldButGood.max(AvlTreeOldButGood.height(k2.right), k1.height) + 1;
		return k2;
	}

	// ~~~~~~~~~ End of insert ~~~~~~~~~~



	//--------------------------------------bfsTraverse-begin-------------------------------------//


	public void bfsTraverse(AvlTreeOldButGood root){
		Set<AvlTreeOldButGood> visited = new HashSet<AvlTreeOldButGood>();
		List<AvlTreeOldButGood> worklist = new ArrayList<AvlTreeOldButGood>();
		visited.add(root);
		worklist.add(root);
		while (!worklist.isEmpty() && visited.size() <= LIMIT) {
			AvlTreeOldButGood node = worklist.remove(0);
			AvlTreeOldButGood left = node.left;
			if (left != null && visited.add(left)) {

				worklist.add(left);
			}
			AvlTreeOldButGood right = node.right;
			if (right != null && visited.add(right)) {
				worklist.add(right);
			}
		} 
	}


	//--------------------------------------bfsTraverse-end-------------------------------------//


	//--------------------------------------dfsTraverse-begin-------------------------------------//


	public boolean dfsTraverse(AvlTreeOldButGood root){
		HashSet<AvlTreeOldButGood> visited = new HashSet<AvlTreeOldButGood>();
		dfsTraverseAux(root, visited);
		return visited.size() <= LIMIT;
	}


	private void dfsTraverseAux(AvlTreeOldButGood root, HashSet<AvlTreeOldButGood> visited){
		if (root != null && visited.add(root)){

			if (root.left != null){
				dfsTraverseAux(root.left, visited);
			}
			if (root.right != null){
				dfsTraverseAux(root.right, visited);
			}

		}
	}

	//--------------------------------------dfsTraverse-end-------------------------------------//




	//------------ begin repOK_ConcretePost ---------------//

	public boolean repOK_ConcretePost(AvlTreeOldButGood root) {
		return repOK_StructurePost(root) && repOK_Ordered(root);
	}

	public boolean repOK_StructurePost(AvlTreeOldButGood root) {
		Set<AvlTreeOldButGood> visited = new HashSet<AvlTreeOldButGood>();
		List<AvlTreeOldButGood> worklist = new ArrayList<AvlTreeOldButGood>();

		if(root != null) {
			visited.add(root);
			worklist.add(root);
		}

		while (!worklist.isEmpty()) {

			AvlTreeOldButGood node = worklist.remove(0);

			if (!repOK_Structure_CheckHeight(node)){

				return false; // Unbalanced or wrong height value!
			}

			AvlTreeOldButGood left = node.left();
			if (left != null) {
				if (!visited.add(left)){
					return false; // Not acyclic!
				}

				worklist.add(left);
			}

			AvlTreeOldButGood right = node.right();
			if (right != null) {
				if (!visited.add(right)){
					return false; // Not acyclic!
				}

				worklist.add(right);
			}

		}
		return true;
	}




	// ========= CONCRETE INVARIANT ====================

	// ~~~~~~~~~ Begin repOK_Concrete ~~~~~~~~~~

	public boolean repOK_Concrete(AvlTreeOldButGood root) {
		return repOK_Structure(root) && repOK_Ordered(root);
	}

	public boolean repOK_Structure(AvlTreeOldButGood root) {
		Set<AvlTreeOldButGood> visited = new HashSet<AvlTreeOldButGood>();
		List<AvlTreeOldButGood> worklist = new ArrayList<AvlTreeOldButGood>();

		if(root != null) {
			visited.add(root);
			worklist.add(root);
		}

		while (!worklist.isEmpty()) {

			AvlTreeOldButGood node = worklist.remove(0);

			if (!repOK_Structure_CheckHeight(node))
				return false; // Unbalanced or wrong height value!

			AvlTreeOldButGood left = node.left();
			if (left != null) {
				if (!visited.add(left))
					return false; // Not acyclic!

				worklist.add(left);
			}

			AvlTreeOldButGood right = node.right();
			if (right != null) {
				if (!visited.add(right))
					return false; // Not acyclic!

				worklist.add(right);
			}

		}
		return visited.size() <= LIMIT;
	}


	// Return true if node.height is consistent and within [-1, 1].
	// Assume node != null.
	//
	public boolean repOK_Structure_CheckHeight(AvlTreeOldButGood node) {
		int lh, rh;

		if (node.left == null)
			lh = -1;
		else
			lh = node.left.height;

		if (node.right == null)
			rh = -1;
		else
			rh = node.right.height;

		int difference = lh - rh;
		if (difference < -1 || difference > 1){
			return false; // Not balanced!
		}

		int max = AvlTreeOldButGood.max(lh, rh);
		if (node.height != 1 + max){
			return false; // Wrong value in height field!
		}

		return true;
	}


	public boolean repOK_Ordered(AvlTreeOldButGood root) {
		int min = repOK_findMin(root);
		int max = repOK_findMax(root);
		if (!repOK_ElementsAreOrdered(root, min-1, max+1)) {
			return false;
		}

		// PEND: What does this do? Is it really necessary?
		/*
		List<AvlTree> worklist = new ArrayList<AvlTree>();
		worklist.add(root);
		while (!worklist.isEmpty()) {
			AvlTree current = worklist.remove(0);
			if (current.left() != null) {
				worklist.add(current.left());
			}
			if (current.right() != null) {
				worklist.add(current.right());
			}
		}
		 */

		return true;
	}

	// Return smallest element.
	// Assume root != null.
	//
	private int repOK_findMin(AvlTreeOldButGood root) {
		AvlTreeOldButGood curr = root;
		while (curr.left() != null) {
			curr = curr.left();
		}
		return curr.element;
	}

	// Return largest element.
	// Assume root != null.
	//
	private int repOK_findMax(AvlTreeOldButGood root) {
		AvlTreeOldButGood curr = root;
		while (curr.right() != null) {
			curr = curr.right();
		}
		return curr.element;
	}

	// Return true iff e is a strict (no dups allowed!) search tree.
	// Assume e != null.
	//	
	public boolean repOK_ElementsAreOrdered(AvlTreeOldButGood e, int min, int max) {
		if ((e.element <= min) || (e.element >= max)) {
			return false;
		}
		if (e.left() != null) {
			if (!repOK_ElementsAreOrdered(e.left(), min, e.element)) {
				return false;
			}
		}
		if (e.right() != null) {
			if (!repOK_ElementsAreOrdered(e.right(), e.element, max)) {
				return false;
			}
		}
		return true;
	}

	// ~~~~~~~~~ End of repOK_Concrete ~~~~~~~~~~



	// ~~~~~~~~~ Begin dumpTree (just for manual testing purposes) ~~~~~~~~~~~

	public static void dumpTree(AvlTreeOldButGood root) {
		System.out.println("");
		dumpTree(root, 0);
		System.out.println("");
	}

	private static void dumpTree(AvlTreeOldButGood root, int level) {
		if(root == null)
			return;
		dumpTree(root.right, level+1);
		if(level != 0){
			for(int i = 0; i < level - 1; i++)
				System.out.print("\t");
			//			System.out.println("|-------" + root.element);
			System.out.println("|-------" + root.hashCode());
		}
		else
			//			System.out.println(root.element);
			System.out.println(root.hashCode());
		dumpTree(root.left, level+1);
	}

	// ~~~~~~~~~ End of dumpTree ~~~~~~~~~~





	public AvlTreeOldButGood remove( int x, AvlTreeOldButGood t )
	{
		if( t == null )
			return t;   // Item not found; do nothing


		if ( x<t.element )
			t.left = remove( x, t.left );
		else if( x>t.element )
			t.right = remove( x, t.right );
		else if( t.left != null && t.right != null ) // Two children
		{
			t.element = findMinimum( t.right );
			t.right = remove( t.element, t.right );
		}
		else
			t = ( t.left != null ) ? t.left : t.right;
		return balance( t );
	}


	private AvlTreeOldButGood balance( AvlTreeOldButGood t )
	{
		if( t == null )
			return t;

		if( height( t.left ) - height( t.right ) > 1 )
			if( height( t.left.left ) >= height( t.left.right ) )
				t = rotateWithLeftChild( t );
			else
				t = doubleWithLeftChild( t );
		else
			if( height( t.right ) - height( t.left ) > 1 )
				if( height( t.right.right ) >= height( t.right.left ) )
					t = rotateWithRightChild( t );
				else
					t = doubleWithRightChild( t );

		t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
		return t;
	}















	// marked in tables as bfsTraverse	
		private static int LIMIT = 8;
		public static void main(String[] args) {
	
			AvlTreeOldButGood X = new AvlTreeOldButGood(10);
			X = (AvlTreeOldButGood) Debug.makeSymbolicRef("X", X);
//			X = (AvlTreeOldButGood) Debug.makeSymbolicRefBounded("X", X);
	
			if (X != null && X.repOK_Concrete(X)) {
				try {
					X.bfsTraverse(X);
					X.countStructure(X);
					X.dumpStructure(X);
				} catch (Exception e) {throw new NullPointerException();}
			}
	
		}


	// marked in tables as dfsTraverse	
	//		private static int LIMIT = 12;
	//		public static void main(String[] args) {
	//	
	//			AvlTree X = new AvlTree(10);
	//			X = (AvlTree) Debug.makeSymbolicRef("X", X);
	////			X = (AvlTree) Debug.makeSymbolicRefBounded("X", X);
	//	
	//			if (X != null) {
	//				try {
	//					X.dfsTraverse(X);
	//					X.countStructure(X);
	////					X.dumpStructure(X);
	//				} catch (Exception e) {}
	//			}
	//	
	//		}




	//	 marked in tables as remove	
	//		private static int LIMIT = 5;
	//		public static void main(String[] args) {
	//
	//			AvlTree X = new AvlTree(10);
	////			X = (AvlTree) Debug.makeSymbolicRef("X", X);
	//			X = (AvlTree) Debug.makeSymbolicRefBounded("X", X);
	//
	//			if (X != null) {
	//				try {
	//					int k = 1;
	//					X.remove(k,X);
	//					X.countStructure(X);
	//					X.dumpStructure(X);
	//				} catch (Exception e) {}
	//			}
	//
	//		}



//	//marked in tables as repOK_Concrete
//		private static int LIMIT = 8;
//		public static void main(String[] args) {
//	
//			AvlTree X = new AvlTree(10);
////			X = (AvlTree) Debug.makeSymbolicRef("X", X);
//			X = (AvlTree) Debug.makeSymbolicRefBounded("X", X);
//	
//			if (X != null && X.repOK_Concrete(X)) {
//				X.countStructure(X);
//				X.dumpStructure(X);
//			}
//	
//		}





	public static int numNodes(AvlTreeOldButGood node){
		if (node == null){
			return 0;
		} else {
			int nodesleft = numNodes(node.left);
			int nodesright = numNodes(node.right);
			return 1 + nodesleft + nodesright;
		}
	}







} // ~~~~~~~~~~~~~~~~~ End of class ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



