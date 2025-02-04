package symbolicheap.bounded;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.nasa.jpf.symbc.Debug;
import gov.nasa.jpf.vm.Verify;

public class CopyOfTreeSet {

	private class Pair<T, U> {
		private T a;
		private U b;

		public Pair(T a, U b) {
			this.a = a;
			this.b = b;
		}

		public T first() {
			return a;
		}

		public U second() {
			return b;
		}
	}


	
	public int color() {
		if (color == RED) {
			color = RED;
		} else {
			color = BLACK;
		}
		return color;
	}



	//fields must be public for the concretization of the hybrid heap to work

	public static final CopyOfTreeSet SYMBOLICOBJ = new CopyOfTreeSet(); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.

	public static final int RED = 0;

	public static final int BLACK = 1;

	public int key;

	public int color;

	public CopyOfTreeSet left;

	public CopyOfTreeSet right;

	public CopyOfTreeSet parent;


	public CopyOfTreeSet(){} //Every class must provide the 0-ary constructor for LI to work properly.

	public CopyOfTreeSet(int key, int color) {
		this.key = key;
		this.color = color;
//		this.color = Verify.random(1);
//		System.out.println("I AM HERE " + this.color);
		left = right = parent = null;
	}

	
	public CopyOfTreeSet(int key, int color, CopyOfTreeSet left, CopyOfTreeSet right, CopyOfTreeSet parent) {
		this.key = key;
		this.color = color;
		//		this.color = Verify.random(1);
		//		System.out.println("I AM HERE " + this.color);
		this.left = left;
		this.right = right;
		this.parent = parent;
	}
	


	public boolean contains(CopyOfTreeSet root, int key) {
		CopyOfTreeSet p = root;
		while (p != null) {
//			Verify.ignoreIf(!repOK(root, LIMIT));
			if (key == p.key) {
				return true;
			} else if (key < p.key) {
				p = p.left;
			} else {
				p = p.right;
			}
		}
		return false;
	}

	public int count(CopyOfTreeSet root) {
		Verify.ignoreIf(!repOK(root, LIMIT));
		root.color = BLACK;
		int n = 1;
		CopyOfTreeSet l = left;
		CopyOfTreeSet r = right;
		if (l != null) {
			n += l.count(root);
		}
		if (r != null) {
			n += r.count(root);
		}
		return n;
	}

	
	
	public void bfsTraverse(CopyOfTreeSet root){
		Set<CopyOfTreeSet> visited = new HashSet<CopyOfTreeSet>();
		List<CopyOfTreeSet> worklist = new ArrayList<CopyOfTreeSet>();
		visited.add(root);
		worklist.add(root);
		while (!worklist.isEmpty() && visited.size() <= LIMIT) {
			CopyOfTreeSet node = worklist.remove(0);
			CopyOfTreeSet left = node.left;
			if (left != null && visited.add(left)) {
				
				worklist.add(left);
			}
			CopyOfTreeSet right = node.right;
			if (right != null && visited.add(right)) {
				worklist.add(right);
			}
		} 
	}

	
	
	public void dfsTraverse(CopyOfTreeSet root){
		HashSet<CopyOfTreeSet> visited = new HashSet<CopyOfTreeSet>();
		dfsTraverseAux(root, visited);
	}
	
	
	private void dfsTraverseAux(CopyOfTreeSet root, HashSet<CopyOfTreeSet> visited){
		if (root != null && visited.add(root)){
			
			if (root.left != null){
				dfsTraverseAux(root.left, visited);
			}
			if (root.right != null){
				dfsTraverseAux(root.right, visited);
			}
			
		}
	}
	
	
	
	
	
    public CopyOfTreeSet insert(int elem) {
    	CopyOfTreeSet root = this;
        CopyOfTreeSet inserted = this.binarySearchTreeInsert(elem, root);
        if (inserted != null) {
            root = fixAfterInsertion(inserted, root);
        }
        return root;
    }

    private CopyOfTreeSet fixAfterInsertion(CopyOfTreeSet x, CopyOfTreeSet root) {

        while (x != null && x != root && x.parent.color == RED) {
            CopyOfTreeSet px = x.parent;
            CopyOfTreeSet ppx;
            CopyOfTreeSet lppx;

            if (px != null) {
                ppx = px.parent;
                if (ppx != null)
                    lppx = ppx.left;
                else
                    lppx = null;
            } else {
                ppx = null;
                lppx = null;
            }

            if (px == lppx) {
                
                CopyOfTreeSet rppx;
                if (ppx!=null)
                    rppx = ppx.right;
                else 
                    rppx = null;
                
                if ((rppx != null) && (rppx.color == RED)) {

                    if (px != null)
                        px.color = BLACK;

                    rppx.color = BLACK;

                    if (ppx != null)
                        ppx.color = RED;

                    x = ppx;
                } else {
                    CopyOfTreeSet rpx;

                    if (px != null)
                        rpx = px.right;
                    else
                        rpx = null;

                    if (x == rpx) {
                        x = px;
                        root = rotateLeft(x, root);
                    }

                    CopyOfTreeSet px2;
                    if (x != null)
                        px2 = x.parent;
                    else
                        px2 = null;

                    if (px2 != null) {
                        px2.color = BLACK;
                        CopyOfTreeSet ppx2;
                        if (px2 != null)
                            ppx2 = px2.parent;
                        else
                            ppx2 = null;

                        if (ppx2 != null)
                            ppx2.color = RED;
                        root = rotateRight(ppx2, root);
                    }
                }
            } else {
                CopyOfTreeSet y = lppx;
                if ((y != null) && (y.color == RED)) {

                    if (px != null)
                        px.color = BLACK;

                    y.color = BLACK;

                    if (ppx != null)
                        ppx.color = RED;

                    x = ppx;
                } else {

                    CopyOfTreeSet lpx;
                    if (px != null)
                        lpx = px.left;
                    else
                        lpx = null;

                    if (x == lpx) {
                        x = px;
                        root = rotateRight(x, root);
                    }

                    CopyOfTreeSet px2;
                    if (x != null)
                        px2 = x.parent;
                    else
                        px2 = null;

                    if (px2 != null) {
                        px2.color = BLACK;

                        CopyOfTreeSet ppx2;
                        if (px2 != null)
                            ppx2 = px2.parent;
                        else
                            ppx2 = null;

                        if (ppx2 != null)
                            ppx2.color = RED;
                        
                        root = rotateLeft(ppx2, root);
                    }
                }
            }
        }
        root.color = BLACK;
        
        return root;
    }

    /**
     * @param parent
     */
    private CopyOfTreeSet rotateRight(CopyOfTreeSet z, CopyOfTreeSet root) {
        CopyOfTreeSet y = z.left;
        z.left = y.right;
        if (y.right != null)
            y.right.parent = z;
        y.parent = z.parent;
        if (z.parent == null)
            root = y;
        else if (z == z.parent.right)
            z.parent.right = y;
        else
            z.parent.left = y;
        y.right = z;
        z.parent = y;
        
        return root;
    }

    private CopyOfTreeSet rotateLeft(CopyOfTreeSet z, CopyOfTreeSet root) {
        CopyOfTreeSet y = z.right;
        z.right = y.left;
        if (y.left != null)
            y.left.parent = z;
        y.parent = z.parent;
        if (z.parent == null)
            root = y;
        else if (z == z.parent.left)
            z.parent.left = y;
        else
            z.parent.right = y;
        y.left = z;
        z.parent = y;
        
        return root;
    }

    private CopyOfTreeSet binarySearchTreeInsert(int elem, CopyOfTreeSet root) {
    	CopyOfTreeSet prev = null;
    	CopyOfTreeSet x = this;

    	while ((x != null) && (x.key != elem)) {
    		prev = x;
    		if (elem < x.key)
    			x = x.left;
    		else
    			x = x.right;
    	}

    	CopyOfTreeSet result;
    	if (x == null) {
    		CopyOfTreeSet newTreeNode = new CopyOfTreeSet(elem, RED, null, null, prev);
    		newTreeNode.parent = prev;

    		if (prev == null) {
    			root = newTreeNode;
    		} else {
    			if (newTreeNode.key < prev.key)
    				prev.left = newTreeNode;
    			else
    				prev.right = newTreeNode;
    		}

    		result = newTreeNode;

    	} else
    		result = null;

    	return result;
    }



    private CopyOfTreeSet successor(CopyOfTreeSet t) {
    	if (t == null) {

    		return null;
    	} else if (t.right != null) {
    		CopyOfTreeSet p = t.right;
    		while (p.left != null) {
    			p = p.left;
    		}

    		{/*$goal 9 reachable*/}
    		return p;
    	} else {
    		CopyOfTreeSet p = t.parent;
    		CopyOfTreeSet ch = t;
    		while (p != null && ch == p.right) {
    			{/*$goal 10 unreachable*/} //always t.right != null
    					ch = p;
    			p = p.parent;
    		}
    		{/*$goal 11 unreachable*/} //always t.right != null
    		return p;
    	}
    }


    
    private CopyOfTreeSet getEntry_remove(int key, CopyOfTreeSet root) {
    	CopyOfTreeSet p = root;
    	while (p != null) {

    		if (key == p.key) {

    			return p;
    		} else if (key < p.key) {

    			p = p.left;
    		} else {

    			p = p.right;
    		}
    	}
    	return null;
    }

    
    public boolean remove(int aKey, CopyOfTreeSet root) {
    	CopyOfTreeSet p = root.getEntry_remove(aKey, root);
    	if (p == null) {
    		return false;
    	}
        
        root = deleteEntry(p, root);
        
        return true;
}

    
    
    private CopyOfTreeSet deleteEntry(CopyOfTreeSet p, CopyOfTreeSet root) {
    	//        decrementSize();

    	// If strictly internal, copy successor's element to p and then make p
    	// point to successor.
    	if (p.left != null && p.right != null) {

    		CopyOfTreeSet s = root.successor(p);
    		p.key = s.key;

    		p = s;
    	} // p has 2 children

    	// Start fixup at replacement node, if it exists.
    	CopyOfTreeSet replacement;
    	if (p.left != null )
    		replacement = p.left ;
    	else
    		replacement = p.right;

    	if (replacement != null) {

    		// Link replacement to parent
    		replacement.parent = p.parent;
    		if (p.parent == null) {
    			root = replacement;
    		} else if (p == p.parent.left){

    			p.parent.left = replacement;
    		} else {

    			p.parent.right = replacement;
    		}

    		// Null out links so they are OK to use by fixAfterDeletion.
    		p.left = p.right = p.parent = null;

    		// Fix replacement
    		if (p.color == BLACK) {

    			fixAfterDeletion(replacement, root);
    		}
    	} else if (p.parent == null) { // return if we are the only node.

    		root = null;
    	} else { //  No children. Use self as phantom replacement and unlink.
    		if (p.color == BLACK) {

    			fixAfterDeletion(p, root);
    		}

    		if (p.parent != null) {

    			if (p == p.parent.left) {

    				p.parent.left = null;
    			} else if (p == p.parent.right) {

    				p.parent.right = null;
    			}

    			p.parent = null;
    		}
    	}
    	return root;
    }

	
    private void fixAfterDeletion(CopyOfTreeSet entry, CopyOfTreeSet root) {
    	CopyOfTreeSet x = entry;

    	while (x != root && colorOf(x) == BLACK) {


    		if (x == leftOf(parentOf(x))) {

    			CopyOfTreeSet sib = rightOf(parentOf(x));

    			if (colorOf(sib) == RED) {

    				setColor(sib, BLACK);
    				setColor(parentOf(x), RED);
    				root = rotateLeft(parentOf(x), root);
    				sib = rightOf(parentOf(x));
    			}

    			if (colorOf(leftOf(sib)) == BLACK
    					&& colorOf(rightOf(sib)) == BLACK) {


    				setColor(sib, RED);
    				x = parentOf(x);
    			} else {
    				if (colorOf(rightOf(sib)) == BLACK) {

    					setColor(leftOf(sib), BLACK);
    					setColor(sib, RED);
    					root.rotateRight(sib, root);
    					sib = rightOf(parentOf(x));
    				}
    				setColor(sib, colorOf(parentOf(x)));
    				setColor(parentOf(x), BLACK);
    				setColor(rightOf(sib), BLACK);
    				root = rotateLeft(parentOf(x), root);
    				x = root;
    			}
    		} else { // symmetric
    			CopyOfTreeSet sib = leftOf(parentOf(x));

    			if (colorOf(sib) == RED) {

    				setColor(sib, BLACK);
    				setColor(parentOf(x), RED);
    				root = rotateRight(parentOf(x), root);
    				sib = leftOf(parentOf(x));
    			}

    			if (colorOf(rightOf(sib)) == BLACK
    					&& colorOf(leftOf(sib)) == BLACK) {

    				setColor(sib, RED);
    				x = parentOf(x);
    			} else {
    				if (colorOf(leftOf(sib)) == BLACK) {

    					setColor(rightOf(sib), BLACK);
    					setColor(sib, RED);
    					root = rotateLeft(sib, root);
    					sib = leftOf(parentOf(x));
    				}
    				setColor(sib, colorOf(parentOf(x)));
    				setColor(parentOf(x), BLACK);
    				setColor(leftOf(sib), BLACK);
    				root = rotateRight(parentOf(x), root);
    				x = root;
    			}
    		}
    	}

    	setColor(x, BLACK);
    	
    }
	
	
	public boolean repOK_Concrete(CopyOfTreeSet root) {
		return repOK_Structure(root) && repOK_Colors(root) && repOK_KeysAndValues(root);
	}

	public boolean repOK_Structure(CopyOfTreeSet root) {
		  Set<CopyOfTreeSet> visited = new HashSet<CopyOfTreeSet>();
          List<CopyOfTreeSet> worklist = new ArrayList<CopyOfTreeSet>();
          visited.add(root);
          worklist.add(root);
          while (!worklist.isEmpty()) {
                  CopyOfTreeSet node = worklist.remove(0);
                  CopyOfTreeSet left = node.left;
                  if (left != null) {
                          if (!visited.add(left)) {
                                  return false;
                          }
                          if (left.parent != node) {
                                  return false;
                          }
                          worklist.add(left);
                  }
                  CopyOfTreeSet right = node.right;
                  if (right != null) {
                          if (!visited.add(right)) {
                                  return false;
                          }
                          if (right.parent != node) {
                                  return false;
                          }
                          worklist.add(right);
                  }
          }
          return visited.size() <= LIMIT;
	}

	public boolean repOK_Colors(CopyOfTreeSet root) {
		if (root.color != BLACK)
			return false;
		
		List<CopyOfTreeSet> worklist = new ArrayList<CopyOfTreeSet>();
		worklist.add(root);
		while (!worklist.isEmpty()) {
			CopyOfTreeSet current = worklist.remove(0);
			CopyOfTreeSet cl = current.left;
			CopyOfTreeSet cr = current.right;
			if (current.color() == RED) {
				if (cl != null && cl.color() == RED) {
					return false;
				}
				if (cr != null && cr.color() == RED) {
					return false;
				}
			}
			if (cl != null) {
				worklist.add(cl);
			}
			if (cr != null) {
				worklist.add(cr);
			}
		}
		int numberOfBlack = -1;
		List<Pair<CopyOfTreeSet, Integer>> worklist2 = new ArrayList<CopyOfTreeSet.Pair<CopyOfTreeSet, Integer>>();
		worklist2.add(new Pair<CopyOfTreeSet, Integer>(root, 0));
		//		root.dumpStructure(root);
		while (!worklist2.isEmpty()) {
			//			System.out.println("->0");
			Pair<CopyOfTreeSet, Integer> p = worklist2.remove(0);
			CopyOfTreeSet e = p.first();
			int n = p.second();
			if (e != null && e.color() == BLACK) {
				n++;
				//				System.out.println("->1 n=" + n);
			}
			if (e == null) {
				//				System.out.println("->2");
				if (numberOfBlack == -1) {
					numberOfBlack = n;
					//					System.out.println("->3 n=" + n);
				} else if (numberOfBlack != n) {
					//					System.out.println("REPOK FAIL n = " + n + " noBlk = " + numberOfBlack);
					return false;
				}
			} else {
				//				System.out.println("->4");
				worklist2.add(new Pair<CopyOfTreeSet, Integer>(e.left, n));
				worklist2.add(new Pair<CopyOfTreeSet, Integer>(e.right, n));
			}
		}
		return true;
	}
	
	
	

	public boolean repOK_KeysAndValues(CopyOfTreeSet root) {
		int min = repOK_findMin(root);
		int max = repOK_findMax(root);
		if (!repOK_orderedKeys(root, min - 1, max + 1)) {
			return false;
		}
		List<CopyOfTreeSet> worklist = new ArrayList<CopyOfTreeSet>();
		worklist.add(root);
		while (!worklist.isEmpty()) {
			CopyOfTreeSet current = worklist.remove(0);
			if (current.left != null) {
				worklist.add(current.left);
			}
			if (current.right != null) {
				worklist.add(current.right);
			}
		}
		return true;
	}

	private int repOK_findMin(CopyOfTreeSet root) {
		CopyOfTreeSet curr = root;
		while (curr.left != null) {
			curr = curr.left;
		}
		return curr.key;
	}

	private int repOK_findMax(CopyOfTreeSet root) {
		CopyOfTreeSet curr = root;
		while (curr.right != null) {
			curr = curr.right;
		}
		return curr.key;
	}

	public boolean repOK_orderedKeys(CopyOfTreeSet e, int min, int max) {
		if ((e.key <= min) || (e.key >= max)) {
			return false;
		}
		if (e.left != null) {
			if (!repOK_orderedKeys(e.left, min, e.key)) {
				return false;
			}
		}
		if (e.right != null) {
			if (!repOK_orderedKeys(e.right, e.key, max)) {
				return false;
			}
		}
		return true;
	}


	public boolean hybridRepOK(){
		Set<CopyOfTreeSet> visited = new HashSet<CopyOfTreeSet>();
		List<CopyOfTreeSet> worklist = new ArrayList<CopyOfTreeSet>();
		if (this != SYMBOLICOBJ){
			visited.add(this);

			worklist.add(this);
			while (!worklist.isEmpty()) {
				CopyOfTreeSet node = worklist.remove(0);
				CopyOfTreeSet left = node.left;
				if (left != null && left != SYMBOLICOBJ) {
					if (!visited.add(left)) {
						return false;
					}
					
					if (left.parent != SYMBOLICOBJ && left.parent != node) {
						return false;
					}
					worklist.add(left);

				}
				CopyOfTreeSet right = node.right;
				if (right != null && right != SYMBOLICOBJ) {
					if (!visited.add(right)) {
						return false;
					}
					if (right.parent != SYMBOLICOBJ && right.parent != node) {
						return false;
					}
					worklist.add(right);
				}

			}
			return visited.size() <= LIMIT;	
		} else {
			return true;
		}
	}

	
	
    private static int colorOf(CopyOfTreeSet p) {
    	int result ;
    	if (p==null)
    		result = BLACK;
    	else
    		result = p.color;
        return result;
    }



    private static CopyOfTreeSet parentOf(CopyOfTreeSet p) {
    	CopyOfTreeSet result;
    	if (p == null)
    		result = null;
    	else
    		result = p.parent;

    	return result;
    }


    
    private static void setColor(CopyOfTreeSet p, int c) {
    	if (p != null)
    		p.color = c;
    }

    private static CopyOfTreeSet leftOf(CopyOfTreeSet p) {
    	CopyOfTreeSet result ;
    	if (p == null)
    		result = null;
    	else
    		result = p.left;
    	return result;
    }

    private static CopyOfTreeSet rightOf(CopyOfTreeSet p) {
    	CopyOfTreeSet result;
    	if (p == null) 
    		result = null;
    	else
    		result = p.right;
    	return result;
    }






    

	public void dumpStructure(CopyOfTreeSet root) {
	}

	public void countStructure(CopyOfTreeSet root) {
	}

	public boolean repOK(CopyOfTreeSet root, int size) {
		return true;
	}


	private static final int LIMIT = 6;
	
	public static void main(String[] args) {
		CopyOfTreeSet X = new CopyOfTreeSet(0, 0);
//		X = (CopyOfTreeSet) Debug.makeSymbolicRefBounded("X", X);
		X = (CopyOfTreeSet) Debug.makeSymbolicRef("X", X);
		// int key = Debug.makeSymbolicInteger("key");
		if (X != null) {
			if (X.repOK_Concrete(X)){
				X.countStructure(X);
//				X.dumpStructure(X);
			}
		}
	}

	
//	public static void main(String[] args) {
//		TreeSet X = new TreeSet(0, 0);
////			X = (TreeSet) Debug.makeSymbolicRefBounded("X", X);
//		X = (TreeSet) Debug.makeSymbolicRef("X", X);
//		// int key = Debug.makeSymbolicInteger("key");
//		//	int value = Debug.makeSymbolicInteger("value");
//		int value = 1000;
//		if (X != null && X.repOK_Concrete(X)) {
//			X = X.insert(value);
//			X.countStructure(X);
//		}
//	}
	

	
//	public static void main(String[] args) {
//		TreeSet X = new TreeSet(0,1,null,null,null);
//		X = X.insert(3);
//		X = X.insert(2);
//		X = X.insert(1);
//		X = X.insert(1);
//		X = X.insert(1);
//	}
	

//	public static void main(String[] args) {
//		TreeSet X = new TreeSet(0,1,null,null,null);
//		X = X.insert(30);
//		X = X.insert(20);
//		X = X.insert(10);
//		X = X.insert(12);
//		X = X.insert(14);
//		boolean ret = X.remove(11, X);
//		if (ret)
//			System.out.println("True");
//		else
//			System.out.println("False");
//	}
	
	
	
	
	
//	public static void main(String[] args) {
//		TreeSet X = new TreeSet(0, 0);
////		X = (TreeSet) Debug.makeSymbolicRefBounded("X", X);
//		X = (TreeSet) Debug.makeSymbolicRef("X", X);
//		int value = 1000;
//		if (X != null && X.repOK_Concrete(X)) {
//
//			boolean output = X.remove(value, X);
//
//		}
//	}

	
	
	

	
//	public static void main(String[] args) {
//		TreeSet X = new TreeSet(0, 0);
//		X = (TreeSet) Debug.makeSymbolicRefBounded("X", X);
//		//	X = (TreeSet) Debug.makeSymbolicRef("X", X);
//		if (X != null) {
//
//			X.bfsTraverse(X);
//			X.countStructure(X);
//			//		X.dumpStructure(X);
//		}
//	}
	
	
	

	
	
//	public static void main(String[] args) {
//	TreeSet X = new TreeSet(0, 0);
//	X = (TreeSet) Debug.makeSymbolicRefBounded("X", X);
////		X = (TreeSet) Debug.makeSymbolicRef("X", X);
//	if (X != null) {
//
//		X.dfsTraverse(X);
//		X.countStructure(X);
//		//		X.dumpStructure(X);
//	}
//}

	
	
	
	
}
