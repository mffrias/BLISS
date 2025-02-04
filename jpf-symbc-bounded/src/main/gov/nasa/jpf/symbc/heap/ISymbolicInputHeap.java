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

package gov.nasa.jpf.symbc.heap;

import gov.nasa.jpf.vm.ClassInfo;

public interface ISymbolicInputHeap<E> {

//    public E header;
//    public int count = 0;

//    public ISymbolicInputHeap() {
//    	setHeader(null);
//    }

	public ISymbolicInputHeap<E> make_copy(); //{
//		ISymbolicInputHeap<E> sih_new = new ISymbolicInputHeap<E>();
//		sih_new.setHeader(this.getHeader());
//	    sih_new.count = this.count;
//		return sih_new;
//	}

	public void _add(E n); 
//	{
//
//		if (!hasNode(n)) {
//			n.setNext(getHeader());
//			setHeader(n);
//			count++;
//		}
//
//	}

	public int count();
//	{
//		return count;
//	}

	public E header();
//	{
//		return getHeader();
//	}

	public boolean hasNode(E n);
//	{
//		E t = getHeader();
//
//		while (t != null) {
//			if (n.equals(t)) {
//				return true;
//			}
//
//			t = t.getNext();
//		}
//
//		return false;
//	}
	
	public E[] getNodesOfType(ClassInfo type);
//	{
//
//
//		int numSymRefs = 0;
//		E n = getHeader();
//		while (null != n){
//			//String t = (String)n.getType();
//			ClassInfo tClassInfo = n.getType();
//			//reference only objects of same class or super
//			//if (fullType.equals(t)){
//			//if (typeClassInfo.isInstanceOf(tClassInfo)) {
//			if (tClassInfo.isInstanceOf(type)) {
//				numSymRefs++;
//			}
//			n = n.getNext();
//		}
//
//		n = getHeader();
//		Object[] nodes = new Object[numSymRefs]; // estimate of size; should be changed
//		int i = 0;
//		while (null != n){
//			//String t = (String)n.getType();
//			ClassInfo tClassInfo = n.getType();
//			//reference only objects of same class or super
//			//if (fullType.equals(t)){
//			//if (typeClassInfo.isInstanceOf(tClassInfo)) {
//			if (tClassInfo.isInstanceOf(type)) {
//				nodes[i] = n;
//				i++;
//			}
//			n = n.getNext();
//		}
//		return (E[])nodes;
//	}
	
	public String toString();
//	{
//		return "SymbolicInputHeap = " + count + ((getHeader() == null) ? "" : "\n" + getHeader().toString());
//	}

	public E getHeader();
//	{
//		return header;
//	}

	public void setHeader(E header);
//	{
//		this.header = header;
//	}


	
}
