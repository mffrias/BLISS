package symbolicheap.bounded;

import java.util.HashSet;
import java.util.Set;

import gov.nasa.jpf.symbc.Debug;

public class SinglyLinkedList {

	
	//fields must be public for the concretization of the hybrid heap to work

	public SinglyLinkedList(){}
	
	public static final SinglyLinkedList SYMBOLICOBJ = new SinglyLinkedList(); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.

	
	public int key;

	public SinglyLinkedList next;

	public SinglyLinkedList(int key) {
		this.key = key;
		next = null;
	}


	public boolean contains(SinglyLinkedList root, int key) {
		SinglyLinkedList current = root;
		while (current != null) {
			if (current.key == key) {
				return true;
			}
			current = current.next;
		}
		return false;
	}

	public boolean repOK_Concrete(SinglyLinkedList root) {
		Set<SinglyLinkedList> seen = new HashSet<SinglyLinkedList>();
		SinglyLinkedList current = root;
		while (current != null) {
			if (!seen.add(current)) {
				return false;
			}
			current = current.next;
		}
		return seen.size() <= LIMIT;
	}
	
	
	
	
	
	
	public boolean contains(int value) {
		SinglyLinkedList current;
		boolean result;

		current = this;
		result = false;
		while (result == false && current != null) {

			boolean equalVal;

			if (value == current.key) { 
				equalVal = true;
			} else {
				equalVal = false;
			}
			if (equalVal == true) {
				result = true;
			}
			current = current.next;
		}
		return result;
	}

    
    
    
	
    public SinglyLinkedList remove(int index) {

    	if (index<0) {
    		throw new RuntimeException();
    	}

    	SinglyLinkedList current;
    	current = this;
    	SinglyLinkedList previous;
    	previous = null;
    	int current_index;
    	current_index = 0;

    	boolean found = false;

    	while (found==false && current != null) {

    		if (index == current_index) {
    			found = true;
    		} else {
    			current_index = current_index + 1;
    			previous = current;
    			current = current.next;
    		}
    	}

    	if (found==false) {
    		throw new RuntimeException();
    	}

    	if (previous == null){
    		return current.next;
    	} else {
    		{/*$goal 6 reachable*/}
    		previous.next = current.next;
    		return this;
    	}
    }



public SinglyLinkedList insertBack(int arg) {
        SinglyLinkedList freshNode = new SinglyLinkedList();
        freshNode.key = arg;
        freshNode.next = null;

        if (this == null) {
                return freshNode;
        } else {
                SinglyLinkedList current;
                current = this;
                while (current.next != null) {
                        current = current.next;
                }
                current.next = freshNode;
                return this;
        }
}
	
	
	
	

	public void dumpStructure(SinglyLinkedList root) {
	}

	public void countStructure(SinglyLinkedList root) {
	}
	
	
	
	
	public boolean hybridRepOK() {
		Set<SinglyLinkedList> visited = new HashSet<SinglyLinkedList>();
		SinglyLinkedList current = this;
		while (current != null && current != SYMBOLICOBJ) {
			if (!visited.add(current)) {
				return false;
			}
			current = current.next;
		}
		return visited.size() <= LIMIT;
	}


	private static final int LIMIT = 100;

	public static void main(String[] args) {
		SinglyLinkedList X = new SinglyLinkedList(0);
//		X = (SinglyLinkedList) Debug.makeSymbolicRefBounded("X", X);
		X = (SinglyLinkedList) Debug.makeSymbolicRef("X", X);
		if (X != null) {
			if (X.repOK_Concrete(X)) {
				 X.countStructure(X);
				 X.dumpStructure(X);
			}
		}
	}

}
