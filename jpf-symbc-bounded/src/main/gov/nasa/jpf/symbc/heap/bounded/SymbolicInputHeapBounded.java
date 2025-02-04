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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.symbc.bytecode.bounded.BoundedHelper;
import gov.nasa.jpf.symbc.heap.HeapNode;
import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import org.apache.commons.lang3.tuple.ImmutablePair;



public class SymbolicInputHeapBounded extends SymbolicInputHeap {

	public HashMap<Integer, BitSet> refinedBoundByIndex;
	private Integer indexToFirstNodePointingToSymbolic;	
	private String nameFieldPointingToFirstSymbolic;
	private Integer nextMinimumNrToUseInRefinement;
	private HashSet<Integer> indicesForAlreadyRefinedNodes;
	
	

	
	
	public SymbolicInputHeapBounded() {
		super();
		refinedBoundByIndex = new HashMap<Integer, BitSet>();
		indexToFirstNodePointingToSymbolic = null;
		nameFieldPointingToFirstSymbolic = null;
		nextMinimumNrToUseInRefinement = null;
		indicesForAlreadyRefinedNodes = new HashSet<Integer>();
	}

	
	public HashSet<Integer> getIndicesForAlreadyRefinedNodes(){
		return indicesForAlreadyRefinedNodes;
	}
	
	public BitSet getBoundByIndex(Integer index){
		return refinedBoundByIndex.get(index);
	}
	
	public void setBoundedName(HeapNode n, BitSet bound){
		this.refinedBoundByIndex.put(n.getIndex(), bound);
	}
	
	
	
	
	
	@Override
	public SymbolicInputHeapBounded make_copy() {
		SymbolicInputHeapBounded sih_new = new SymbolicInputHeapBounded();
		sih_new.setHeader(this.header());
	    sih_new.count = this.count;
	    sih_new.nodesByIndex = new HashMap<Integer, HeapNode>(nodesByIndex);
	    sih_new.refinedBoundByIndex = new HashMap<Integer, BitSet>();
	    for (int index : this.refinedBoundByIndex.keySet()){
	    	BitSet bs = new BitSet();
	    	bs.or(this.getBoundByIndex(index));
	    	sih_new.refinedBoundByIndex.put(index, bs);
	    }
	    
	    sih_new.indexToFirstNodePointingToSymbolic = indexToFirstNodePointingToSymbolic;
	    sih_new.nameFieldPointingToFirstSymbolic = nameFieldPointingToFirstSymbolic;
	    sih_new.nextMinimumNrToUseInRefinement = nextMinimumNrToUseInRefinement;
	    sih_new.indicesForAlreadyRefinedNodes = indicesForAlreadyRefinedNodes;
	    sih_new.indexViaFieldToIndex = new HashMap<ImmutablePair<Integer,String>, Integer>(indexViaFieldToIndex);
		return sih_new;
	}

	
	public Integer getIndexToFirstNodePointingToSymbolic(){
		return indexToFirstNodePointingToSymbolic;
	}
	
	
	public String getNameFieldPointingToFirstSymbolic(){
		return nameFieldPointingToFirstSymbolic;
	}
	
	
	public Integer getNextMinimumNrToUseInRefinement(){
		return nextMinimumNrToUseInRefinement;
	}


	public void _add(HeapNode b, BitSet bound) {
		super._add(b);
//		nodesByIndex.put(b.getIndex(), b);
		BitSet boundCopy = new BitSet();
		boundCopy.or(bound);
		refinedBoundByIndex.put(b.getIndex(), boundCopy);
		
	}

	public Iterable<HeapNode> getHeapNodes() {
		return nodesByIndex.values();
	}
	
	

	
	

//	public HeapNode getRootByName(BitSet boundedName) {
//		HeapNode node = (HeapNode) getHeader();
////		HeapNode node = (HeapNode) this.;
//		return node;
//	}
	
	
	
	
	public HeapNode[] getIntersectingNodesOfType(ClassInfo type, HeapNode hnb, 
			String fieldName, BoundsMap bounds){


		int numSymRefs = 0;
		HeapNode n = (HeapNode)header();
		BitSet zeroSet = new BitSet();
		zeroSet.set(0);
		BitSet targetsBound = bounds.getTargets(type.getName(), fieldName, this.getBoundByIndex(hnb.getIndex()));
		targetsBound.andNot(zeroSet);
		while (n != null){
			//String t = (String)n.getType();
			ClassInfo tClassInfo = n.getType();
			//reference only objects of same class or super
			//if (fullType.equals(t)){
			//if (typeClassInfo.isInstanceOf(tClassInfo)) {
			if (tClassInfo.isInstanceOf(type) && getBoundByIndex(n.getIndex()).intersects(targetsBound)) {
				numSymRefs++;
			}
			n = (HeapNode) n.getNext();
		}

		n = (HeapNode) header();
		HeapNode[] nodes = new HeapNode[numSymRefs]; // estimate of size; should be changed
		int i = 0;
		while (n != null){
			//String t = (String)n.getType();
			ClassInfo tClassInfo = n.getType();
			//reference only objects of same class or super
			//if (fullType.equals(t)){
			//if (typeClassInfo.isInstanceOf(tClassInfo)) {
			if (tClassInfo.isInstanceOf(type) && getBoundByIndex(n.getIndex()).intersects(targetsBound)) {
				nodes[i] = n;
				i++;
			}
			n = (HeapNode) n.getNext();
		}
		return nodes;
	}


	
	
	
	public Integer[] getIndicesForIntersectingNodesOfType(ClassInfo type, HeapNode hnb, 
			String fieldName, BoundsMap bounds){


		int numSymRefs = 0;
		HeapNode n = (HeapNode)header();
//		if (n.getIndex()==391){
//			System.out.println(n.getType() + " " + type);
//		}
//		System.out.println(n.getIndex());
		BitSet zeroSet = new BitSet();
		zeroSet.set(0);
		BitSet targetsBound = bounds.getTargets(type.getName(), fieldName, this.getBoundByIndex(hnb.getIndex()));
		targetsBound.andNot(zeroSet);
		while (n != null){
			//String t = (String)n.getType();
			ClassInfo tClassInfo = n.getType();
			//reference only objects of same class or super
			//if (fullType.equals(t)){
			//if (typeClassInfo.isInstanceOf(tClassInfo)) {
			if (tClassInfo.isInstanceOf(type) && getBoundByIndex(n.getIndex()).intersects(targetsBound)) {
				numSymRefs++;
			}
			n = (HeapNode) n.getNext();
		}

		n = (HeapNode) header();
		Integer[] nodes = new Integer[numSymRefs]; // estimate of size; should be changed
		int i = 0;
		while (n != null){
			//String t = (String)n.getType();
			ClassInfo tClassInfo = n.getType();
			//reference only objects of same class or super
			//if (fullType.equals(t)){
			//if (typeClassInfo.isInstanceOf(tClassInfo)) {
			if (tClassInfo.isInstanceOf(type) && getBoundByIndex(n.getIndex()).intersects(targetsBound)) {
				nodes[i] = n.getIndex();
				i++;
			}
			n = (HeapNode) n.getNext();
		}
		return nodes;
	}

	
	

//	public HeapNode getNodeByName(BitSet boundedName) {
//		HeapNode node = (HeapNode) getHeader();
//		while (node != null) {
//			if (node.getBoundedName().equals(boundedName)) {
//				return node;
//			}
//			node = (HeapNode) node.getNext();
//		}
//		return null;
//	}
	
	
	

	public void addPCs(PathCondition pc, HeapNode node) {
		for (HeapNode n = header(); n != null; n = n.getNext()) {
			if (n != node) {
				pc._addDet(Comparator.NE, node.getSymbolic(), n.getSymbolic());
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append('[');
		HeapNode node = (HeapNode) header();
		boolean isFirst = true;
		while (node != null) {
			if (!isFirst) {
				b.append(", ");
			} else {
				isFirst = false;
			}
			b.append(node.toString());
			node = (HeapNode) node.getNext();
		}
		b.append(']');
		return b.toString();
	}

	public void dump(String prefix) {
		System.out.println(prefix + "nodes:");
		HeapNode node = (HeapNode) header();
		while (node != null) {
			System.out.println(prefix + "   " + node);
			node = (HeapNode) node.getNext();
		}
	}

}
