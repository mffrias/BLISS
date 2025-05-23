/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * Symbolic Pathfinder (jpf-symbc) is licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import gov.nasa.jpf.symbc.arrays.ArrayHeapNode;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.symbc.heap.bounded.SymbolicInputHeapBounded;


public class SymbolicInputHeap {

    HeapNode header;
    protected int count = 0;
    public HashMap<ImmutablePair<Integer,String>, Integer> indexViaFieldToIndex;

	public HashMap<Integer, HeapNode> nodesByIndex;
	
	public HeapNode getNodeByIndex(Integer index) {
		return nodesByIndex.get(index);
	}

    public SymbolicInputHeap() {
    	header = null;
    	indexViaFieldToIndex = new HashMap<ImmutablePair<Integer,String>, Integer>();
		nodesByIndex = new HashMap<Integer, HeapNode>();
    }
    public boolean pointsToNullThroughField(Integer index, String fieldName){
		ImmutablePair<Integer, String> mp = new ImmutablePair<Integer, String>(index, fieldName);
		return indexViaFieldToIndex.containsKey(mp) && indexViaFieldToIndex.get(mp).equals(MJIEnv.NULL);
	}
	
	
	public Integer pointsToThroughField(Integer index, String fieldName){
		ImmutablePair<Integer, String> mp = new ImmutablePair<Integer, String>(index, fieldName);
		return indexViaFieldToIndex.get(mp);
	}

    
	
	/**
	 * isSymbolic returns true if node(nodeIndex).fieldName is symbolic. It requires that
	 * elementInfo(Node(nodeIndex)).getReferenceField(fieldName) == -1, i.e., we know it points to either
	 * null (-1) or symbolic (null).
	 */
	
	public boolean isSymbolic(Integer nodeIndex, String fieldName){
		return !pointsToNullThroughField(nodeIndex, fieldName);
	}
	
	public Integer getRootIndexFromAnySymbolicIntegerInHeap(){
		HeapNode headerCandidate = header();
		while (headerCandidate.getType().getSimpleName().toUpperCase().equals("INTEGER")){
			headerCandidate = headerCandidate.getNext();
		}
		String headerSymbolicName = headerCandidate.getSymbolic().getName();
		String name = headerSymbolicName.substring(0, headerSymbolicName.indexOf("]"));
		name = name.substring(name.indexOf("[") + 1);
		return Integer.parseInt(name);
	}

	
	
	public void setPointsToIndexThroughField(Integer source, String fieldName, Integer target){
		ImmutablePair<Integer, String> mp = new ImmutablePair<Integer, String>(source, fieldName);
		indexViaFieldToIndex.put(mp, target);
	}

	public SymbolicInputHeap make_copy() {
		SymbolicInputHeap sih_new = new SymbolicInputHeap();
		sih_new.header = this.header;
	    sih_new.count = this.count;
	    sih_new.indexViaFieldToIndex = new HashMap<ImmutablePair<Integer,String>, Integer>(indexViaFieldToIndex);
	    sih_new.nodesByIndex = new HashMap<Integer, HeapNode>(nodesByIndex);
		return sih_new;
	}
	

	public void _add(HeapNode n) {
		nodesByIndex.put((Integer) n.getIndex(), n);

		if (!hasNode(n)) {
			n.setNext(header);
			header = n;
			count++;
		}

	}

	public int count() {
		return count;
	}

	public HeapNode header() {
		return header;
	}

	public boolean hasNode(HeapNode n) {
		HeapNode t = header;

		while (t != null) {
			if (n.equals(t)) {
				return true;
			}

			t = t.getNext();
		}

		return false;
	}
	
	public SymbolicInteger getNode(int daIndex) {
	    HeapNode n = header;
        while (n != null){
            if (n.getIndex() == daIndex)
                return n.getSymbolic();
            n = n.getNext();
        }
        return null;
	}
	
	public HeapNode[] getNodesOfType(ClassInfo type) {
		  
		  int numSymRefs = 0;
		  HeapNode n = header;
		  while (null != n){
			  //String t = (String)n.getType();
			  ClassInfo tClassInfo = n.getType();
			  //reference only objects of same class or super
			  //if (fullType.equals(t)){
			  //if (typeClassInfo.isInstanceOf(tClassInfo)) {
			  if (tClassInfo.isInstanceOf(type)) {
				  numSymRefs++;
			  }
			  n = n.getNext();
		  }
		  
		  n = header;
		  HeapNode[] nodes = new HeapNode[numSymRefs]; // estimate of size; should be changed
		  int i = 0;
		  while (null != n){
			  //String t = (String)n.getType();
			  ClassInfo tClassInfo = n.getType();
			  //reference only objects of same class or super
			  //if (fullType.equals(t)){
			  //if (typeClassInfo.isInstanceOf(tClassInfo)) {
			  if (tClassInfo.isInstanceOf(type)) {
				  nodes[i] = n;
				  i++;
			  }
			  n = n.getNext();
		  }
		  return nodes;
	}

    public ArrayHeapNode[] getArrayNodesOfType(ClassInfo type, int ref) {
        int numSymRefs = 0;
        HeapNode n = header;
        while (null != n) {
            if (n instanceof ArrayHeapNode) {
                ClassInfo tClassInfo = n.getType();
                if (tClassInfo.isInstanceOf(type)) {
                    if (((ArrayHeapNode)n).arrayRef == ref) {
                        numSymRefs++;
                    }
                }
            }
            n = n.getNext();
        }
        n = header;
        ArrayHeapNode[] nodes = new ArrayHeapNode[numSymRefs]; 
        int i = 0;
        while (null != n) {
            if (n instanceof ArrayHeapNode) {
                ClassInfo tClassInfo = n.getType();
                if (tClassInfo.isInstanceOf(type)) {
                    if (((ArrayHeapNode)n).arrayRef == ref) {
                        nodes[i] = (ArrayHeapNode)n;
                        i++;
                    }
                }
            }
            n = n.getNext();
        }
        return nodes;
    }

	
	public String toString() {
		return "SymbolicInputHeap = " + count + ((header == null) ? "" : "\n" + header.toString());
	}
	
	

	protected void setHeader(HeapNode header) {
		this.header = header;
	}

}
