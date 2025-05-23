//
//Copyright (C) 2006 United States Government as represented by the
//Administrator of the National Aeronautics and Space Administration
//(NASA).  All Rights Reserved.
//
//This software is distributed under the NASA Open Source Agreement
//(NOSA), version 1.3.  The NOSA has been approved by the Open Source
//Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
//directory tree for the complete NOSA document.
//
//THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
//KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
//LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
//SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
//A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
//THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
//DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//

package gov.nasa.jpf.symbc.heap.bounded;

import java.util.BitSet;

import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.symbc.heap.HeapNode;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;

public class CopyOfHeapNodeBounded extends HeapNode {

//	private BitSet boundedName;
	
	public CopyOfHeapNodeBounded(int idx, ClassInfo tClassInfo, SymbolicInteger sym, BitSet boundedName) {
		super(idx, tClassInfo, sym);
//		this.boundedName = (BitSet) boundedName.clone();
	}

//	public final BitSet getBound() {
////		return (BitSet) boundedName.clone();
//		return boundedName;
//	}
	
//	public void setBoundedName(BitSet boundedName) {
////		this.boundedName = (BitSet) boundedName.clone();
//		this.boundedName = boundedName;
//	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("[ref=").append(getIndex());
		b.append(", symName=").append(getSymbolic().getName());
//		b.append(", boundedName=").append(boundedName.toString());
		b.append("]");
		return b.toString();
	}

}