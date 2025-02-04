package symbolicheap.bounded;

import java.util.Set;

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

public class FibHeapNode {

	public static final FibHeapNode SYMBOLICFIBHEAPNODE = new FibHeapNode(0); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.

    public FibHeapNode parent, left, right, child;

    public boolean mark=false;
    public int cost, degree=0;

    public FibHeapNode(int c) {cost = c; right=this; left=this;}
    
    public FibHeapNode() {}
    
    public String toString(int k, boolean flag) {
        String res="{"+mark+" ";
		if (flag) {
			res += cost + " ";
		}
		if (k!=0) {
            if (left == null || left == this) {
				res += "null";
			} else {
				res += left.toString(k - 1, flag);
			}
			if (right == null || right == this) {
				res += "null";
			} else {
				res += right.toString(k - 1, flag);
			}
			if (child == null) {
				res += "null";
			}
	}
	return res;
    }
    
    public String toString() {
    	return toString(0,true);
    }
    
    
 public   boolean isStructural(Set<FibHeapNode> visited,
            FibHeapNode parent) {
        FibHeapNode current = this;
        do {
            if (current.parent != parent)
                return false;
            // if (!visited.add(new IdentityWrapper(current)))
            if (!visited.add(current))
                return false;
            if ((current.child != null)
                    && (!current.child.isStructural(visited, current)))
                return false;
            if (current.right == null)
                return false;
            if (current.right.left != current)
                return false;
            current = current.right;
        } while (current != this);
        return true;
    }
    
    
    
 public   int numberOfChildren() {
        if (child == null)
            return 0;
        int num = 1;
        for (FibHeapNode current = child.right; current != child; current = current.right) {
            num++;
        }
        return num;
    }

    
    
    
    
    
 public boolean checkDegrees() {
        FibHeapNode current = this;
        do {
            if (current.numberOfChildren() != current.degree)
                return false;
            if (current.child != null)
                if (!current.child.checkDegrees())
                    return false;
            current = current.right;
        } while (current != this);
        return true;
    }

    
    
    
 public  boolean checkHeapified() {
        if (child == null)
            return true;
        FibHeapNode current = child;
        do {
            if (current.cost < cost)
                return false;
            if (!current.checkHeapified())
                return false;
            current = current.right;
        } while (current != child);
        return true;
    }

    
   
 
 
 public   int numberOfChildrenHybrid() {
     if (child == null)
         return 0;
     if (child == SYMBOLICFIBHEAPNODE)
    	 return SYMBOLICINT;
     int num = 1;
     FibHeapNode current;
     for (current = child.right; current != SYMBOLICFIBHEAPNODE && current != child; current = current.right) {
         num++;
     }
     if (current == SYMBOLICFIBHEAPNODE)
    	 return SYMBOLICINT;
     else
    	 return num;
 }

 
 
 public  boolean checkHeapifiedHybrid() {
     if (child == null)
         return true;
     FibHeapNode current = child;
     do {
         if (current != SYMBOLICFIBHEAPNODE && current.cost != SYMBOLICINT && current.cost < cost)
             return false;
         if (current != SYMBOLICFIBHEAPNODE && !current.checkHeapifiedHybrid())
             return false;
         current = current.right;
     } while (current != SYMBOLICFIBHEAPNODE && current != child);
     return true;
 }

    
    
    
    /** 
     * requires this != SYMBOLICFIBHEAPNODE;
     * @param visited
     * @param parent
     * @return
     */
 public   boolean isStructuralHybrid(Set<FibHeapNode> visited, FibHeapNode parent) {
        FibHeapNode current = this;
        do {
            if (current.parent != SYMBOLICFIBHEAPNODE && current.parent != parent)
                return false;
            
            // if (!visited.add(new IdentityWrapper(current)))
            if (!visited.add(current))
                return false;
            
            if ((current.child != SYMBOLICFIBHEAPNODE) 
            		&& (current.child != null) 
            		&& (!current.child.isStructuralHybrid(visited, current)))
                return false;
            
            if (current.right == null)
                return false;
            
            if (current.right != SYMBOLICFIBHEAPNODE 
            		&& current.right.left != SYMBOLICFIBHEAPNODE 
            		&& current.right.left != current)
                return false;
            
            current = current.right;
        } while (current != SYMBOLICFIBHEAPNODE && current != this);
        return true;
    }

    
    /**
     * requires this != SYMBOLICFIBHEAPNODE;
     * @return
     */
 public   boolean checkDegreesHybrid() {
        FibHeapNode current = this;
        do {
            if (current.degree != SYMBOLICINT && current.numberOfChildrenHybrid() != SYMBOLICINT && current.numberOfChildrenHybrid() != current.degree)
                return false;
            if (current.child != SYMBOLICFIBHEAPNODE && current.child != null)
                if (!current.child.checkDegreesHybrid())
                    return false;
            current = current.right;
        } while (current != SYMBOLICFIBHEAPNODE && current != this);
        return true;
    }

    
    

    
    
}
