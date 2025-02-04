//
// Copyright (C) 2006 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package symbolicheap.bounded.fieldBasedTesting.binheap;


//import rfm.testingtool.structures.binheap.BinomialHeapNode.NodeWrapper;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.TreeBag;

import gov.nasa.jpf.symbc.Debug;
import symbolicheap.bounded.fieldBasedTesting.binheap.BinomialHeapNode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

//import rfm.testingtool.structures.binheap.BinomialHeapNode;


/**
 * @SpecField nodes: set BinomialHeapNode from this.Nodes, this.nodes.child, this.nodes.sibling, this.nodes.parent  
 *                   | this.nodes = this.Nodes.*(child @+ sibling) @- null ;
 */
/**
 * @Invariant ( all n: BinomialHeapNode | ( n in this.Nodes.*(sibling @+ child) @- null => (
 *		            ( n.parent!=null  => n.key >=  n.parent.key )  &&   
 *		            ( n.child!=null   => n !in n.child.*(sibling @+ child) @- null ) && 
 *		            ( n.sibling!=null => n !in n.sibling.*(sibling @+ child) @- null ) && 
 *		            ( ( n.child !=null && n.sibling!=null ) => (no m: BinomialHeapNode | ( m in n.child.*(child @+ sibling) @- null && m in n.sibling.*(child @+ sibling) @- null )) ) && 
 *		            ( n.degree >= 0 ) && 
 *		            ( n.child=null => n.degree = 0 ) && 
 *		            ( n.child!=null =>n.degree=#(n.child.*sibling @- null) )  && 
 *		            ( #( ( n.child @+ n.child.child.*(child @+ sibling) ) @- null ) = #( ( n.child @+ n.child.sibling.*(child @+ sibling)) @- null )  ) && 
 *		            ( n.child!=null => ( all m: BinomialHeapNode | ( m in n.child.*sibling@-null =>  m.parent = n  ) ) ) && 
 *		            ( ( n.sibling!=null && n.parent!=null ) => ( n.degree > n.sibling.degree ) )
 * ))) && 
 * ( this.size = #(this.Nodes.*(sibling @+ child) @- null) ) &&
 * ( all n: BinomialHeapNode | n in this.Nodes.*sibling @- null => ( 
 *  ( n.sibling!=null => n.degree < n.sibling.degree ) && 
 *  ( n.parent=null ) 
 *  )) ;
 */
public class BinomialHeap implements Serializable {

	public static final BinomialHeap SYMBOLICBINOMIALHEAP = new BinomialHeap(); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.


	
	public boolean hybridRepOK() {
		if (this.Nodes == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
			return true;
		BinomialHeapNode root = this.Nodes;
		final LinkedList<BinomialHeapNode> seen = new LinkedList<BinomialHeapNode>();
		if (root!=null) {

			// son aciclicos los hijos de root?
			if (!isAcyclicHybrid(root, seen)){
				return false;
			}	
			// esta ordenado el arbol que cuelga de root?
			if (!orderedHybrid(root)){
				return false;
			}

			// el parent de root es null?
			if (root == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || root.parent == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
				return true;
			if (root.parent!=null){
				return false;
			}
			// si hay un root.sibling, el degree de root es menor
			if (root == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || root.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
				return true;
			if (root.sibling!=null) {
				if (root == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || root.degree == BinomialHeapNode.SYMBOLICINT || root.sibling.degree == BinomialHeapNode.SYMBOLICINT)
					return true;
				if (root.degree >= root.sibling.degree){
					return false;
				}	
			}

			// los ns son los siblings de root
			if (root == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || root.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
				return true;
			BinomialHeapNode ns = root.sibling;

			while (ns != null) {

				// son aciclicos los hijos del sibling de root
				if (!isAcyclicHybrid(ns, seen)){
					return false;
				}
				// el parent del sibling es root?
				if (ns == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || ns.parent == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
					return true;
				if (ns.parent!=null){
					return false;
				}
				// el degree de este sibling es menor al del siguiente?
				if (ns == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || ns.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
					return true;
				if (ns.sibling!=null) {
					if (ns == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || ns.degree == BinomialHeapNode.SYMBOLICINT || ns.sibling.degree == BinomialHeapNode.SYMBOLICINT)
						return true;
					if (ns.degree>=ns.sibling.degree){
						return false;
					}	
				}

				// estan ordenados el subarbol que cuelga de este sibling?
				if (!orderedHybrid(ns)){
					return false;
				}

				// proximo sibling de root
				if (ns == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || ns.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
					return true;
				ns = ns.sibling;
			}

		}
		if (size == BinomialHeap.SYMBOLICINT)
			return true;
		return size == seen.size();
	}


	public static boolean isAcyclicHybrid(final BinomialHeapNode start, final LinkedList<BinomialHeapNode> seen) {
		if (start == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
			return true;
		if (seen.contains(start)){
			return false;
		}
		if (start == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || start.degree == BinomialHeapNode.SYMBOLICINT)
			return true;
		if (start.degree<0){
			return false;
		}

		// enable these lines if key is instance of Object
		//if (start.key==null)
		//	 return false;

		if (start == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
			return true;
		seen.add(start);

		if (start == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || start.child == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
			return true;
		BinomialHeapNode currchild = start.child;

		int child_count = 0;

		while (currchild!=null) {
			child_count++;

			if (currchild == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || currchild.parent == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
				return true;
			if (currchild.parent != start){
				return false;
			}
			if (!isAcyclicHybrid(currchild, seen)){
				return false;
			}
			if (currchild == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || currchild.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
				return true;
			if (currchild.sibling!=null) {
				if (currchild == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || currchild.degree == BinomialHeapNode.SYMBOLICINT || currchild.sibling.degree == BinomialHeapNode.SYMBOLICINT)
					return true;
				if (currchild.degree<=currchild.sibling.degree){
					return false;
				}
			}
			if (currchild == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || currchild.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
				return true;
			currchild = currchild.sibling;
		}

		if (start == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || start.degree == BinomialHeapNode.SYMBOLICINT)
			return true;
		if (start.degree!=child_count){
			return false;
		}

		if (start == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || start.child == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
			return true;
		if (start.child!=null) {
			int tam_child=1;
			if (start == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || start.child == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || start.child.child == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
				return true;
			if (start.child.child!=null) {
				BinomialHeapNode curr = start.child.child;
				while (curr!=null) {
					tam_child+= count_nodesHybrid(curr);
					if (tam_child == BinomialHeapNode.SYMBOLICINT)
						return true;
					curr = curr.sibling;
					if (curr == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || curr.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
						return true;
				}
			}

			int tam_sibling=1;
			if (start == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || start.child == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || start.child.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
				return true;
			if (start.child.sibling!=null) {
				BinomialHeapNode curr = start.child.sibling;
				while (curr!=null) {
					int i = count_nodesHybrid(curr);
					if (i == BinomialHeapNode.SYMBOLICINT)
						return true;
					tam_sibling+= i;
					curr = curr.sibling;
					if (curr == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || curr.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
						return true;
				}
			}

			if (tam_child!=tam_sibling){
				return false;
			}
		}
		return true;	
	}

	/**
	 * 
	 * @param start
	 * @return number of nodes reachable from start.child through fields child and sibling + 1;
	 */
	private static int count_nodesHybrid(BinomialHeapNode start) {

		int node_count = 1;
		if (start == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || start.child == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
			return BinomialHeapNode.SYMBOLICINT;
		BinomialHeapNode child = start.child;
		while (child!=null) {	
			node_count += count_nodesHybrid(child);
			if (node_count == BinomialHeapNode.SYMBOLICINT)
				return BinomialHeapNode.SYMBOLICINT;
			if (child == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || child.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
				return BinomialHeapNode.SYMBOLICINT;
			child=child.sibling;
		}
		return node_count;
	}


	private static boolean orderedHybrid(final BinomialHeapNode node) {
		if (node == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || node.child == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
			return true;
		if (node.child != null) {
			if (node == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || node.key == BinomialHeapNode.SYMBOLICINT || node.child == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || node.child.key == BinomialHeapNode.SYMBOLICINT)
				return true;
			if (node.child.key < node.key) {
				return false;
			}
			if (node == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || node.child == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
				return true;
			if (!orderedHybrid(node.child)) {
				return false;
			}
			if (node == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || node.child == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || node.child.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
				return true;
			for (BinomialHeapNode ns = node.child.sibling; ns != null; ns = ns.sibling) {
				if (ns.key < node.key) {
					return false;
				}
				if (!orderedHybrid(ns)) {
					return false;
				}
				if (ns == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE || ns.sibling == BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE)
					return true;
			}
			return true;
		} 	 
		return true;
	}





	private static int count_nodes_hybrid(BinomialHeapNode start) {

		int node_count = 1;
		BinomialHeapNode child = start.child;
		while (child != BinomialHeapNode.SYMBOLICBINOMIALHEAPNODE && child != null) {	
			if (node_count != BinomialHeapNode.SYMBOLICINT){
				int cnh = count_nodes_hybrid(child);
				if (cnh != BinomialHeapNode.SYMBOLICINT){
					node_count += count_nodes_hybrid(child);
				} else {
					return BinomialHeapNode.SYMBOLICINT;
				}
			} else {
				return SYMBOLICINT;
			}
			child=child.sibling;
		}
		if (child != null)
			return SYMBOLICINT;
		else
			return node_count;
	}








	private static final long serialVersionUID=6495900899527469812L;

	public /*@ nullable @*/ BinomialHeapNode Nodes;

	public int size;

	public BinomialHeap() {
		Nodes = null;
		size = 0;
	}

	// 2. Find the minimum key
	/**
	 * @Modifies_Everything
	 * 
	 * @Requires some this.nodes ; 
	 * @Ensures ( some x: BinomialHeapNode | x in this.nodes && x.key == return ) && 
	 *          ( all y : BinomialHeapNode | ( y in this.nodes && y!=return ) => return <= y.key ) ;  
	 */
	public int findMinimum() {
		if (Nodes == null) return -1;
		return Nodes.findMinNode().key;
	}

	// 3. Unite two binomial heaps
	// helper procedure
	private void merge(/*@ nullable @*/BinomialHeapNode binHeap) {
		BinomialHeapNode temp1 = Nodes, temp2 = binHeap;
		while ((temp1 != null) && (temp2 != null)) {
			if (temp1.degree == temp2.degree) {
				BinomialHeapNode tmp = temp2;
				temp2 = temp2.sibling;
				tmp.sibling = temp1.sibling;
				temp1.sibling = tmp;
				temp1 = tmp.sibling;
			} else {
				if (temp1.degree < temp2.degree) {
					if ((temp1.sibling == null)
							|| (temp1.sibling.degree > temp2.degree)) {
						BinomialHeapNode tmp = temp2;
						temp2 = temp2.sibling;
						tmp.sibling = temp1.sibling;
						temp1.sibling = tmp;
						temp1 = tmp.sibling;
					} else {
						temp1 = temp1.sibling;
					}
				} else {
					BinomialHeapNode tmp = temp1;
					temp1 = temp2;
					temp2 = temp2.sibling;
					temp1.sibling = tmp;
					if (tmp == Nodes) {
						Nodes = temp1;
					} 
				}
			}
		}

		if (temp1 == null) {
			temp1 = Nodes;
			while (temp1.sibling != null) {
				temp1 = temp1.sibling;
			}
			temp1.sibling = temp2;
		} 
	}

	// another helper procedure
	private void unionNodes(/*@ nullable @*/BinomialHeapNode binHeap) {
		merge(binHeap);

		BinomialHeapNode prevTemp = null, temp = Nodes , nextTemp = Nodes.sibling;

		while (nextTemp != null) {
			if ((temp.degree != nextTemp.degree)
					|| ((nextTemp.sibling != null) && (nextTemp.sibling.degree == temp.degree))) {
				prevTemp = temp;
				temp = nextTemp;
			} else {
				if (temp.key <= nextTemp.key) {
					temp.sibling = nextTemp.sibling;
					nextTemp.parent = temp;
					nextTemp.sibling = temp.child;
					temp.child = nextTemp;
					temp.degree++;
				} else {
					if (prevTemp == null) {
						Nodes = nextTemp;
					} else {
						prevTemp.sibling = nextTemp;
					}
					temp.parent = nextTemp;
					temp.sibling = nextTemp.child;
					nextTemp.child = temp;
					nextTemp.degree++;
					temp = nextTemp;
				}
			}

			nextTemp = temp.sibling;
		}
	}

	// 4. Insert a node with a specific value
	/**
	 * @Modifies_Everything
	 * 
	 * @Ensures some n: BinomialHeapNode | (
	 *            n !in @old(this.nodes) &&
	 *            this.nodes = @old(this.nodes) @+ n &&
	 *            n.key = value ) ;
	 */
	public void insert(int value) {
		BinomialHeapNode temp = new BinomialHeapNode(value);
		if (Nodes == null) {
			Nodes = temp;
			size = 1;
		} else {
			unionNodes(temp);
			size++;
		}
	}

	// 5. Extract the node with the minimum key
	/**
	 * @Modifies_Everything
	 * 
	 * @Ensures ( @old(this).@old(Nodes)==null => ( this.Nodes = null && return = null ) ) 
	 *       && ( @old(this).@old(Nodes)!=null => ( (return in @old(this.nodes)) &&
	 *                                              ( all y : BinomialHeapNode | ( y in @old(this.nodes.key) && y.key >= return.key ) ) && 
	 *                                              (this.nodes = @old(this.nodes) @- return ) &&
	 *                                              (this.nodes.key  @+ return.key = @old(this.nodes.key) )
	 *                                             ));
	 */
	public /*@ nullable @*/BinomialHeapNode extractMin() {
		if (Nodes == null)
			return null;

		BinomialHeapNode temp = Nodes, prevTemp = null;
		BinomialHeapNode minNode = Nodes.findMinNode();
		while (temp.key != minNode.key) {
			prevTemp = temp;
			temp = temp.sibling;
		}

		if (prevTemp == null) {
			Nodes = temp.sibling;
		} else {
			prevTemp.sibling = temp.sibling;
		}
		temp = temp.child;
		BinomialHeapNode fakeNode = temp;
		while (temp != null) {
			temp.parent = null;
			temp = temp.sibling;
		}

		if ((Nodes == null) && (fakeNode == null)) {
			size = 0;
		} else {
			if ((Nodes == null) && (fakeNode != null)) {
				Nodes = fakeNode.reverse(null);
				// FIX
				size = Nodes.getSize();
				// BUG
				// size--;
			} else {
				if ((Nodes != null) && (fakeNode == null)) {
					// FIX
					size = Nodes.getSize();
					// BUG
					//size--;
				} else {
					unionNodes(fakeNode.reverse(null));
					// FIX
					size = Nodes.getSize();
					// BUG
					//size--;
				}
			}
		}

		return minNode;
	}

	// 6. Decrease a key value
	public void decreaseKeyValue(int old_value, int new_value) {
		if (Nodes != null) {
			BinomialHeapNode temp = Nodes.findANodeWithKey(old_value);
			decreaseKeyNode(temp, new_value);
		}
	}

	/**
	 * 
	 * @Modifies_Everything
	 * 
	 * @Requires node in this.nodes && node.key >= new_value ;
	 * 
	 * @Ensures (some other: BinomialHeapNode | other in this.nodes && other!=node && @old(other.key)=@old(node.key))
	 *          ? this.nodes.key = @old(this.nodes.key) @+ new_value
	 *          : this.nodes.key = @old(this.nodes.key) @- @old(node.key) @+ new_value ;  
	 */
	public void decreaseKeyNode(BinomialHeapNode node, int new_value) {
		if (node == null)
			return;
		node.key = new_value;
		BinomialHeapNode tempParent = node.parent;

		while ((tempParent != null) && (node.key < tempParent.key)) {
			int z = node.key;
			node.key = tempParent.key;
			tempParent.key = z;

			node = tempParent;
			tempParent = tempParent.parent;
		}
	}

	// 7. Delete a node with a certain key
	public void delete(int value) {
		if ((Nodes != null) && (Nodes.findANodeWithKey(value) != null)) {
			decreaseKeyValue(value, findMinimum() - 1);
			extractMin();
		}
	}



	/*** ORACLE METHODS ***/

	public int findMinimumOracle() {
		if (Nodes == null) return -1;
		return Nodes.findMinNode().key;
	}

	// 3. Unite two binomial heaps
	// helper procedure
	private void mergeOracle(/*@ nullable @*/BinomialHeapNode binHeap) {
		BinomialHeapNode temp1 = Nodes, temp2 = binHeap;
		while ((temp1 != null) && (temp2 != null)) {
			if (temp1.degree == temp2.degree) {
				BinomialHeapNode tmp = temp2;
				temp2 = temp2.sibling;
				tmp.sibling = temp1.sibling;
				temp1.sibling = tmp;
				temp1 = tmp.sibling;
			} else {
				if (temp1.degree < temp2.degree) {
					if ((temp1.sibling == null)
							|| (temp1.sibling.degree > temp2.degree)) {
						BinomialHeapNode tmp = temp2;
						temp2 = temp2.sibling;
						tmp.sibling = temp1.sibling;
						temp1.sibling = tmp;
						temp1 = tmp.sibling;
					} else {
						temp1 = temp1.sibling;
					}
				} else {
					BinomialHeapNode tmp = temp1;
					temp1 = temp2;
					temp2 = temp2.sibling;
					temp1.sibling = tmp;
					if (tmp == Nodes) {
						Nodes = temp1;
					} 
				}
			}
		}

		if (temp1 == null) {
			temp1 = Nodes;
			while (temp1.sibling != null) {
				temp1 = temp1.sibling;
			}
			temp1.sibling = temp2;
		} 
	}

	// another helper procedure
	private void unionNodesOracle(/*@ nullable @*/BinomialHeapNode binHeap) {
		mergeOracle(binHeap);

		BinomialHeapNode prevTemp = null, temp = Nodes , nextTemp = Nodes.sibling;

		while (nextTemp != null) {
			if ((temp.degree != nextTemp.degree)
					|| ((nextTemp.sibling != null) && (nextTemp.sibling.degree == temp.degree))) {
				prevTemp = temp;
				temp = nextTemp;
			} else {
				if (temp.key <= nextTemp.key) {
					temp.sibling = nextTemp.sibling;
					nextTemp.parent = temp;
					nextTemp.sibling = temp.child;
					temp.child = nextTemp;
					temp.degree++;
				} else {
					if (prevTemp == null) {
						Nodes = nextTemp;
					} else {
						prevTemp.sibling = nextTemp;
					}
					temp.parent = nextTemp;
					temp.sibling = nextTemp.child;
					nextTemp.child = temp;
					nextTemp.degree++;
					temp = nextTemp;
				}
			}

			nextTemp = temp.sibling;
		}
	}

	// 4. Insert a node with a specific value
	/**
	 * @Modifies_Everything
	 * 
	 * @Ensures some n: BinomialHeapNode | (
	 *            n !in @old(this.nodes) &&
	 *            this.nodes = @old(this.nodes) @+ n &&
	 *            n.key = value ) ;
	 */
	public void insertOracle(int value) {
		BinomialHeapNode temp = new BinomialHeapNode(value);
		if (Nodes == null) {
			Nodes = temp;
			size = 1;
		} else {
			unionNodesOracle(temp);
			size++;
		}
	}

	// 5. Extract the node with the minimum key
	/**
	 * @Modifies_Everything
	 * 
	 * @Ensures ( @old(this).@old(Nodes)==null => ( this.Nodes = null && return = null ) ) 
	 *       && ( @old(this).@old(Nodes)!=null => ( (return in @old(this.nodes)) &&
	 *                                              ( all y : BinomialHeapNode | ( y in @old(this.nodes.key) && y.key >= return.key ) ) && 
	 *                                              (this.nodes = @old(this.nodes) @- return ) &&
	 *                                              (this.nodes.key  @+ return.key = @old(this.nodes.key) )
	 *                                             ));
	 */
	public /*@ nullable @*/BinomialHeapNode extractMinOracle() {
		if (Nodes == null)
			return null;

		BinomialHeapNode temp = Nodes, prevTemp = null;
		BinomialHeapNode minNode = Nodes.findMinNode();
		while (temp.key != minNode.key) {
			prevTemp = temp;
			temp = temp.sibling;
		}

		if (prevTemp == null) {
			Nodes = temp.sibling;
		} else {
			prevTemp.sibling = temp.sibling;
		}
		temp = temp.child;
		BinomialHeapNode fakeNode = temp;
		while (temp != null) {
			temp.parent = null;
			temp = temp.sibling;
		}

		if ((Nodes == null) && (fakeNode == null)) {
			size = 0;
		} else {
			if ((Nodes == null) && (fakeNode != null)) {
				Nodes = fakeNode.reverse(null);
				// FIX
				size = Nodes.getSize();
				// BUG
				// size--;
			} else {
				if ((Nodes != null) && (fakeNode == null)) {
					// FIX
					size = Nodes.getSize();
					// BUG
					//size--;
				} else {
					unionNodesOracle(fakeNode.reverse(null));
					// FIX
					size = Nodes.getSize();
					// BUG
					//size--;
				}
			}
		}

		return minNode;
	}

	// 6. Decrease a key value
	public void decreaseKeyValueOracle(int old_value, int new_value) {
		if (Nodes != null) {
			BinomialHeapNode temp = Nodes.findANodeWithKey(old_value);
			decreaseKeyNodeOracle(temp, new_value);
		}
	}

	/**
	 * 
	 * @Modifies_Everything
	 * 
	 * @Requires node in this.nodes && node.key >= new_value ;
	 * 
	 * @Ensures (some other: BinomialHeapNode | other in this.nodes && other!=node && @old(other.key)=@old(node.key))
	 *          ? this.nodes.key = @old(this.nodes.key) @+ new_value
	 *          : this.nodes.key = @old(this.nodes.key) @- @old(node.key) @+ new_value ;  
	 */
	public void decreaseKeyNodeOracle(BinomialHeapNode node, int new_value) {
		if (node == null)
			return;
		node.key = new_value;
		BinomialHeapNode tempParent = node.parent;

		while ((tempParent != null) && (node.key < tempParent.key)) {
			int z = node.key;
			node.key = tempParent.key;
			tempParent.key = z;

			node = tempParent;
			tempParent = tempParent.parent;
		}
	}

	// 7. Delete a node with a certain key
	public void deleteOracle(int value) {
		if ((Nodes != null) && (Nodes.findANodeWithKey(value) != null)) {
			decreaseKeyValueOracle(value, findMinimumOracle() - 1);
			extractMinOracle();
		}
	}	

	/*** END OF ORACLE METHODS ***/

	public String toString() {
		if (Nodes == null)
			return size + "\n()\n";
		else
			return size + "\n" + Nodes.toString();
	}

	public void printHeap() {
		if (Nodes == null)
			System.out.println(size + "\n()\n");
		else
			Nodes.printHeap();
		System.out.println(size + "\n()\n");
	}



	boolean checkDegrees() {
		int degree_ = size;
		int rightDegree = 0;
		for (BinomialHeapNode current = Nodes; current != null; current = current.sibling) {
			if (degree_ == 0)
				return false;
			while ((degree_ / 2) == 0) {
				rightDegree++;
				// degree_ /= 2;
				degree_ = degree_ / 2;
			}
			if (current.degree != rightDegree)
				return false;
			if (!current.checkDegree(rightDegree))
				return false;
			rightDegree++;
			//degree_ /= 2;
			degree_ = degree_ / 2;
		}
		return (degree_ == 0);
	}

	boolean checkHeapified() {
		for (BinomialHeapNode current = Nodes; current != null; current = current.sibling) {
			if (!current.isHeapified())
				return false;
		}
		return true;
	}

	public boolean repOK() {
		BinomialHeapNode root = this.Nodes;
		final LinkedList<BinomialHeapNode> seen = new LinkedList<BinomialHeapNode>();
		if (root!=null) {

			// son aciclicos los hijos de root?
			if (!isAcyclic(root, seen)){
				return false;
			}	
			// esta ordenado el arbol que cuelga de root?
			if (!ordered(root)){
				return false;
			}

			// el parent de root es null?
			if (root.parent!=null){
				return false;
			}
			// si hay un root.sibling, el degree de root es menor
			if (root.sibling!=null) {
				if (root.degree >= root.sibling.degree){
					return false;
				}	
			}

			// los ns son los siblings de root
			BinomialHeapNode ns = root.sibling;

			while (ns != null) {

				// son aciclicos los hijos del sibling de root
				if (!isAcyclic(ns, seen)){
					return false;
				}
				// el parent del sibling es root?
				if (ns.parent!=null){
					return false;
				}
				// el degree de este sibling es menor al del siguiente?
				if (ns.sibling!=null) {
					if (ns.degree>=ns.sibling.degree){
						return false;
					}	
				}

				// estan ordenados el subarbol que cuelga de este sibling?
				if (!ordered(ns)){
					return false;
				}

				// proximo sibling de root
				ns = ns.sibling;
			}

		}

		return size == seen.size();
	}

	private static boolean ordered(final BinomialHeapNode node) {
		if (node.child != null) {
			if (node.child.key < node.key) {
				return false;
			}
			if (!ordered(node.child)) {
				return false;
			}
			for (BinomialHeapNode ns = node.child.sibling; ns != null; ns = ns.sibling) {
				if (ns.key < node.key) {
					return false;
				}
				if (!ordered(ns)) {
					return false;
				}
			}
			return true;
		} 	 
		return true;
	}


	public static boolean isAcyclic(final BinomialHeapNode start, final LinkedList<BinomialHeapNode> seen) {
		if (seen.contains(start)){
			return false;
		}
		if (start.degree<0){
			return false;
		}

		// enable these lines if key is instance of Object
		//if (start.key==null)
		//	 return false;

		seen.add(start);

		BinomialHeapNode currchild = start.child;

		int child_count = 0;

		while (currchild!=null) {
			child_count++;

			if (currchild.parent != start){
				return false;
			}
			if (!isAcyclic(currchild, seen)){
				return false;
			}
			if (currchild.sibling!=null) {
				if (currchild.degree<=currchild.sibling.degree){
					return false;
				}
			}
			currchild = currchild.sibling;
		}

		if (start.degree!=child_count){
			return false;
		}

		if (start.child!=null) {
			int tam_child=1;
			if (start.child.child!=null) {
				BinomialHeapNode curr = start.child.child;
				while (curr!=null) {
					tam_child+= count_nodes(curr);
					curr = curr.sibling;
				}
			}

			int tam_sibling=1;
			if (start.child.sibling!=null) {
				BinomialHeapNode curr = start.child.sibling;
				while (curr!=null) {
					tam_sibling+= count_nodes(curr);
					curr = curr.sibling;
				}
			}

			if (tam_child!=tam_sibling){
				return false;
			}
		}
		return true;	
	}


	/**
	 * 
	 * @param start
	 * @return number of nodes reachable from start.child through fields child and sibling + 1;
	 */
	private static int count_nodes(BinomialHeapNode start) {

		int node_count = 1;
		BinomialHeapNode child = start.child;
		while (child!=null) {	
			node_count += count_nodes(child);
			child=child.sibling;
		}
		return node_count;
	}



	public BinomialHeap deepcopy() {

		LinkedList<BinomialHeapNode> visited = new LinkedList<BinomialHeapNode>();
		ArrayList<BinomialHeapNode> nodes = new ArrayList<BinomialHeapNode>(size);		
		ArrayList<BinomialHeapNode> newnodes = new ArrayList<BinomialHeapNode>(size);

		int ind = 0;

		if (Nodes == null) {
			BinomialHeap res = new BinomialHeap();
			res.Nodes = null;
			res.size = size;
			return res;
		}
		else {
			visited.add(Nodes);

			while (!visited.isEmpty()) {
				BinomialHeapNode currNode = visited.removeFirst();

				nodes.add(currNode);
				currNode._index = ind;
				ind++;

				if (currNode.child != null) 
					visited.add(currNode.child);
				if (currNode.sibling != null) 
					visited.add(currNode.sibling);
			}

			for (int i=0; i<nodes.size();i++) {
				BinomialHeapNode newnode = new BinomialHeapNode();
				newnodes.add(newnode);
			}

			for (int i=0; i<nodes.size();i++) {
				BinomialHeapNode currnode = nodes.get(i);
				BinomialHeapNode newnode = newnodes.get(i);

				newnode.key = currnode.key;
				newnode.degree = currnode.degree;

				if (currnode.child != null)
					newnode.child = newnodes.get(currnode.child._index);
				else
					newnode.child = null;

				if (currnode.sibling != null)
					newnode.sibling = newnodes.get(currnode.sibling._index);
				else
					newnode.sibling = null;

				if (currnode.parent != null)
					newnode.parent = newnodes.get(currnode.parent._index);
				else
					newnode.parent = null;

			}

			BinomialHeap res = new BinomialHeap();
			res.Nodes = newnodes.get(Nodes._index);
			res.size = size;
			return res;

		}	
	}


	/*
	private String nodeToString(BinomialHeapNode n) {
		if (n == null) {
			return "null";
		}
		else {
			return "N" + n._index;
		}
	}

	public void writeToFile(Path filename) throws IOException {

		LinkedList<BinomialHeapNode> visited = new LinkedList<BinomialHeapNode>();
		ArrayList<BinomialHeapNode> nodes = new ArrayList<BinomialHeapNode>(size);
		ArrayList<BinomialHeapNode> newnodes = new ArrayList<BinomialHeapNode>(size);

		LinkedList<String> element = new LinkedList<String>();
		LinkedList<String> degree = new LinkedList<String>();
		LinkedList<String> sibling = new LinkedList<String>();
		LinkedList<String> child = new LinkedList<String>();
		LinkedList<String> parent = new LinkedList<String>();

		int ind = 0;

		if (Nodes == null) {
			BinomialHeap res = new BinomialHeap();
			res.Nodes = null;
			res.size = size;
		}
		else {
			visited.add(Nodes);

			while (!visited.isEmpty()) {
				BinomialHeapNode currNode = visited.removeFirst();

				nodes.add(currNode);
				currNode._index = ind;
				ind++;

				if (currNode.child != null) 
					visited.add(currNode.child);
				if (currNode.sibling != null) 
					visited.add(currNode.sibling);
			}

			for (int i=0; i<nodes.size();i++) {
				BinomialHeapNode newnode = new BinomialHeapNode();
				newnodes.add(newnode);
			}

			for (int i=0; i<nodes.size();i++) {
				BinomialHeapNode currnode = nodes.get(i);
				BinomialHeapNode newnode = newnodes.get(i);

				newnode.key = currnode.key;
				newnode.degree = currnode.degree;

				if (currnode.child != null)
					newnode.child = newnodes.get(currnode.child._index);
				else
					newnode.child = null;

				if (currnode.sibling != null)
					newnode.sibling = newnodes.get(currnode.sibling._index);
				else
					newnode.sibling = null;

				if (currnode.parent != null)
					newnode.parent = newnodes.get(currnode.parent._index);
				else
					newnode.parent = null;

			}

			BinomialHeap res = new BinomialHeap();
			res.Nodes = newnodes.get(Nodes._index);
			res.size = size;

			BufferedWriter b = Files.newBufferedWriter(filename, Charset.defaultCharset(), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

			b.write("nodes=");
			for (int i = 0; i < nodes.size(); i++) {
				b.write(nodeToString(nodes.get(i)));
				if (i < nodes.size()-1) {
					b.write(",");
				}
			}
			b.write("\n");

			b.write("QF.head_0=BinHeapIntVar:" + nodeToString(Nodes));
			b.write("\n");

			b.write("QF.size_0=BinHeapIntVar:" + size);
			b.write("\n");

			b.write("element=");
			for (int i = 0; i < nodes.size(); i++) {
				b.write(nodeToString(nodes.get(i)));
				if (i < nodes.size()-1) {
					b.write(",");
				}
			}
			b.write("\n");		


			b.write("degree=");


			b.write("fsibling=");
			b.write("bsibling=");


			b.write("fchild=");
			b.write("bchild=");


			b.write("fparent=");
			b.write("bparent=");

			b.close();
		}	
	}
	 */





	public String toJava() {

		LinkedList<BinomialHeapNode> visited = new LinkedList<BinomialHeapNode>();
		ArrayList<BinomialHeapNode> nodes = new ArrayList<BinomialHeapNode>(size);
		String res;

		res = "BinomialHeap S0 = (BinomialHeap) BinomialHeap.class.newInstance();\n";

		res += "Field degreeF = BinomialHeapNode.class.getDeclaredField(\"degree\");\n";
		res += "degreeF.setAccessible(true);\n";

		res += "Field NodesF = BinomialHeap.class.getDeclaredField(\"Nodes\");\n";
		res += "NodesF.setAccessible(true);\n";

		res += "Field parentF = BinomialHeapNode.class.getDeclaredField(\"parent\");\n";
		res += "parentF.setAccessible(true);\n";

		res += "Field keyF = BinomialHeapNode.class.getDeclaredField(\"key\");\n";
		res += "keyF.setAccessible(true);\n";

		res += "Field childF = BinomialHeapNode.class.getDeclaredField(\"child\");\n";
		res += "childF.setAccessible(true);\n";	

		res += "Field sizeF = BinomialHeap.class.getDeclaredField(\"size\");\n";
		res += "sizeF.setAccessible(true);\n";			

		res += "Field siblingF = BinomialHeapNode.class.getDeclaredField(\"sibling\");\n";
		res += "siblingF.setAccessible(true);\n";	

		int ind = 0;

		if (Nodes != null) {
			visited.add(Nodes);

			while (!visited.isEmpty()) {
				BinomialHeapNode currNode = visited.removeFirst();

				nodes.add(currNode);
				currNode._index = ind;
				ind++;

				if (currNode.child != null) 
					visited.add(currNode.child);
				if (currNode.sibling != null) 
					visited.add(currNode.sibling);
			}

			for (int i=0; i<nodes.size();i++) {
				res += "BinomialHeapNode N" + nodes.get(i)._index + " = (BinomialHeapNode) BinomialHeapNode.class.newInstance();\n";
			}

			res += "\n";

			for (int i=0; i<nodes.size();i++) {
				BinomialHeapNode currnode = nodes.get(i);

				res += "keyF.set(N" + currnode._index + ", " + currnode.key + ");\n";
				res += "degreeF.set(N" + currnode._index + ", " + currnode.degree + ");\n";

				if (currnode.child != null)
					res += "childF.set(N" + currnode._index + ", N" + currnode.child._index + ");\n"; 
				else
					res += "childF.set(N" + currnode._index + ", null);\n";

				if (currnode.sibling != null)
					res += "siblingF.set(N" + currnode._index + ", N" + currnode.sibling._index + ");\n"; 
				else
					res += "siblingF.set(N" + currnode._index + ", null);\n";

				if (currnode.parent != null)
					res += "parentF.set(N" + currnode._index + ", N" + currnode.parent._index + ");\n"; 
				else
					res += "parentF.set(N" + currnode._index + ", null);\n";

				res += "\n";
			}
		}

		res += "\n";

		if (Nodes == null)
			res += "NodesF.set(S0, null);\n";
		else 
			res += "NodesF.set(S0, " + "N" + Nodes._index + ");\n";
		res += "sizeF.set(S0, " + this.size + ");\n";

		/*
		res += "return S0;\n";
		res += "}\n\n";
		 */
		return res;
	}

	@Override
	public int hashCode() {
		if (this.Nodes == null) return 1;

		Bag thisBag = new TreeBag();
		this.Nodes.toSortedBag(thisBag);
		return thisBag.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BinomialHeap other = (BinomialHeap) obj;

		if (size != other.size)
			return false;

		if (this.Nodes == null)
			if (other.Nodes == null) 
				return true;
			else 
				return false;
		else {
			Bag thisBag = new TreeBag();
			Bag otherBag = new TreeBag();
			this.Nodes.toSortedBag(thisBag);
			other.Nodes.toSortedBag(otherBag);
			return thisBag.equals(otherBag);
		}
	}	







	/*
	public String getNamesAtRuntime() {
		String testname = new Object(){}.getClass().getEnclosingMethod().getName();
		String classname = this.getClass().getSimpleName();
		System.out.println(classname + "-" + testname);
		return "";
	}
	 */


	/*
	public static void main(String [] args) {
		Class c = new Object(){}.getClass();
		String name = c.getEnclosingMethod().getName();	
		BinomialHeap b = new BinomialHeap();
		b.getNamesAtRuntime();
	}
	/*









/*	
	public static void main(String [] args) {
	    BinomialHeap binomialHeap0 = new BinomialHeap();
	    binomialHeap0.delete(1);
	    binomialHeap0.delete(2);
	    boolean b0 = binomialHeap0.equals((Object)10.0d);
	    binomialHeap0.delete(2);
	    binomialHeap0.insert(1);
	    binomialHeap0.insert(4);
	    BinomialHeap binomialHeap1 = new BinomialHeap();
	    binomialHeap1.delete(1);
	    binomialHeap1.delete(2);
	    boolean b2 = binomialHeap1.equals((Object)false);
	    BinomialHeap binomialHeap2 = new BinomialHeap();
	    binomialHeap2.insert(3);
	    boolean b3 = binomialHeap2.equals((Object)(-1.0f));
	    binomialHeap2.insert(2);
	    boolean b4 = binomialHeap1.equals((Object)2);
	    BinomialHeapNode binomialHeapNode0 = binomialHeap1.extractMin();
	    binomialHeap1.delete(0);
	    BinomialHeap binomialHeap3 = new BinomialHeap();
	    binomialHeap3.delete(1);
	    BinomialHeap binomialHeap4 = new BinomialHeap();
	    binomialHeap4.delete(1);
	    binomialHeap4.delete(2);
	    binomialHeap4.delete(4);
	    boolean b5 = binomialHeap4.equals((Object)(short)100);
	    boolean b6 = binomialHeap3.equals((Object)binomialHeap4);
	    BinomialHeap binomialHeap5 = new BinomialHeap();
	    binomialHeap5.delete(1);
	    binomialHeap5.delete(2);
	    boolean b8 = binomialHeap5.equals((Object)false);
	    BinomialHeap binomialHeap6 = new BinomialHeap();
	    boolean b9 = binomialHeap5.equals((Object)binomialHeap6);
	    binomialHeap5.delete(4);
	    binomialHeap5.delete(4);
	    BinomialHeapNode binomialHeapNode1 = binomialHeap5.extractMin();
	    boolean b10 = binomialHeap3.equals((Object)binomialHeap5);
	    boolean b11 = binomialHeap1.equals((Object)binomialHeap5);
	    binomialHeap5.delete(1);
	    boolean b12 = binomialHeap0.equals((Object)binomialHeap5);
	    binomialHeap0.delete(3);
	    binomialHeap0.insert(4);
	    binomialHeap0.insert(1);
	    BinomialHeap binomialHeap7 = new BinomialHeap();
	    binomialHeap7.delete(1);
	    binomialHeap7.insert(4);
	    binomialHeap7.insert(4);
	    binomialHeap7.delete(1);
	    BinomialHeapNode binomialHeapNode2 = binomialHeap7.extractMin();
	    BinomialHeap binomialHeap8 = new BinomialHeap();
	    binomialHeap8.delete(1);
	    binomialHeap8.insert(4);
	    binomialHeap8.insert(1);
	    BinomialHeapNode binomialHeapNode3 = binomialHeap8.extractMin();
	    boolean b13 = binomialHeap7.equals((Object)binomialHeap8);
	    binomialHeap8.insert(4);
	    binomialHeap8.insert(4);
	    binomialHeap8.insert(1);
	    boolean b14 = binomialHeap0.equals((Object)1);
	    BinomialHeapNode binomialHeapNode4 = binomialHeap0.extractMin();



	    HashBag theBag = new HashBag();
	    binomialHeap0.Nodes.toBag(theBag);
	    System.out.println(theBag.toString());

	    theBag = new HashBag();
	    binomialHeap8.Nodes.toBag(theBag);
	    System.out.println(theBag.toString());

	    System.out.println(binomialHeap0.equals(binomialHeap8));
	    System.out.println(binomialHeap0.hashCode() == binomialHeap8.hashCode());

	    System.out.println(binomialHeap8.equals(binomialHeap0));
	    System.out.println(binomialHeap0.hashCode() == binomialHeap8.hashCode());

		    // Checks the contract:  equals-hashcode on binomialHeap0 and binomialHeap8
	    org.junit.Assert.assertTrue("Contract failed: equals-hashcode on binomialHeap0 and binomialHeap8", binomialHeap0.equals(binomialHeap8) ? binomialHeap0.hashCode() == binomialHeap8.hashCode() : true);







		BinomialHeap tree = new BinomialHeap();
		tree.insert(1);
		tree.insert(2);
		tree.insert(3);
		tree.insert(0);
		tree.printHeap();


		BinomialHeap copy = tree.deepcopy();
		tree.delete(0);
		tree.printHeap();

		copy.printHeap();
		copy.insert(5);
		copy.printHeap();
		tree.printHeap();

	}
	 */	

	private static void dumpStructure() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {

		BinomialHeap X = new BinomialHeap();
		X = (BinomialHeap) Debug.makeSymbolicRef("X", X);

		if (X != null) {
			try {
				if (X.repOK()){
					dumpStructure();
				}
			} catch (Exception e) {}
		}

	}	

}
// end of class BinomialHeap
