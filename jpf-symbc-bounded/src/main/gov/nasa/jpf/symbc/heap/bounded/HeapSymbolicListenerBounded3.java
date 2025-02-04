// Copyright (C) 2007 United States Government as represented by the
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
package gov.nasa.jpf.symbc.heap.bounded;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.vm.DynamicElementInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.KernelState;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.SystemState;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.IRETURN;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.bytecode.InvokeInstruction;
import gov.nasa.jpf.report.PublisherExtension;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.bytecode.bounded.BoundedHelper;
import gov.nasa.jpf.symbc.heap.HeapChoiceGenerator;
import gov.nasa.jpf.symbc.heap.HeapNode;
import gov.nasa.jpf.symbc.heap.Helper;
import gov.nasa.jpf.symbc.heap.SymbolicInputHeap;

public class HeapSymbolicListenerBounded3 extends PropertyListenerAdapter
implements PublisherExtension {

	public static int structureCount = 0;

	public static int uniqueStructureCount = 0;

	public static int dumpStructureCalls = 0;

	public static int numOfRepOKCalls = 0;

	public static int numOfRepOKFails = 0;

	public static int numOfSolverCalls = 0;

	public static int numOfUNSATVerdicts = 0;

	private static Set<String> structures = new HashSet<String>();

	private DynamicElementInfo repOKRoot = null;

	private int repOKSize = 0;

	@Override
	public void searchFinished(Search search) {
		System.out.println("targetCount = " + BoundedHelper.targetCount + " (# of structures that passed the hybridRepOk)");
		System.out.println("structureCount = " + structureCount + " (# of structures generated at the point where the count was inserted)");
		System.out.println("uniqueStructureCount = " + uniqueStructureCount + " (# of structurally different structures)");
		System.out.println("dumpStructureCalls = " + dumpStructureCalls);
		System.out.println("numOfRepOKCalls = " + numOfRepOKCalls);
		System.out.println("numOfRepOKFails = " + numOfRepOKFails);
		System.out.println("ResetTime = " + BoundedHelper.RESETHEAPTime);
		System.out.println("RefineTime = " + BoundedHelper.REFINETime);
		System.out.println("HYBRIDREPOKTime = " + BoundedHelper.HYBRIDREPOKTime);
		System.out.println("PathConditionsSolvingTime = " + BoundedHelper.PATHCONDITIONSolvingTime);
		System.out.println("");
		System.out.println("numOfSolverCalls = " + numOfSolverCalls);
		System.out.println("numOfUNSATVerdicts = " + numOfUNSATVerdicts);
		System.out.println("");
		System.out.println("SATTraverseTime = " + BoundedHelper.SATTraverseTime + " ms");
		System.out.println("SATCheckTime = " + BoundedHelper.SATCheckTime + " ms");
		System.out.println("SATDBTime = " + BoundedHelper.SATDBTime + " ms");
		System.out.println("SATDBHits = " + BoundedHelper.SATDBHits);
		System.out.println("SATDBInserts = " + BoundedHelper.SATDBInserts);
		System.out.println("SATCount = " + BoundedHelper.SATCount);
		System.out.println("UNSATCount = " + BoundedHelper.UNSATCount);
		System.out.println("SATCount + UNSATCount = " + (BoundedHelper.SATCount + BoundedHelper.UNSATCount));
	}

	@Override
	//	public void executeInstruction(JVM vm) {
	//		Instruction insn = vm.getLastInstruction();
	//		if (insn instanceof InvokeInstruction) {
	//			InvokeInstruction call = (InvokeInstruction) insn;
	//			MethodInfo mi = call.getInvokedMethod();
	//			if (mi != null) {
	//				String mn = mi.getName();
	//				if (mn.equals("dumpStructure")) {
	//					ChoiceGenerator<?>[] cgs = vm.getSystemState().getChoiceGenerators();
	//					HeapChoiceGeneratorBounded hcgb0 = null;
	//					for (ChoiceGenerator<?> cg : cgs){
	//						if (cg instanceof HeapChoiceGeneratorBounded){
	//							hcgb0 = (HeapChoiceGeneratorBounded) cg;
	//						}
	//					}
	//					assert (hcgb0 instanceof HeapChoiceGeneratorBounded) : "expected HeapChoiceGeneratorBounded, got: " + hcgb0;
	//					HeapChoiceGeneratorBounded hcgb = (HeapChoiceGeneratorBounded) hcgb0;
	//					SymbolicInputHeapBounded heap = (SymbolicInputHeapBounded) (hcgb.getNextChoice());
	//					Integer dei = BoundedHelper.getRootHackICSE2014Index();
	//					System.out.println(dumpRootStructure(heap, dei));
	//					dumpStructureCalls++;
	//				} else if (mn.equals("countStructure")) {
	//					ChoiceGenerator<?>[] cgs = vm.getSystemState().getChoiceGenerators();
	//					HeapChoiceGeneratorBounded hcgb0 = null;
	//					for (ChoiceGenerator<?> cg : cgs){
	//						if (cg instanceof HeapChoiceGeneratorBounded){
	//							hcgb0 = (HeapChoiceGeneratorBounded) cg;
	//						}
	//					}
	//					assert (hcgb0 instanceof HeapChoiceGeneratorBounded) : "expected HeapChoiceGeneratorBounded, got: " + hcgb0;
	//					HeapChoiceGeneratorBounded hcgb = (HeapChoiceGeneratorBounded) hcgb0;
	//					SymbolicInputHeapBounded heap = (SymbolicInputHeapBounded) (hcgb.getNextChoice());
	//					Integer dei = BoundedHelper.getRootHackICSE2014Index();
	//					countStructure(heap, dei);
	//					structureCount++;
	//				} else if (mn.equals("repOK")) {
	//					ThreadInfo ti = vm.getLastThreadInfo();
	//					repOKRoot = (DynamicElementInfo) call.getArgumentValue("root", ti);
	//					repOKSize = (Integer) call.getArgumentValue("size", ti);
	//				}
	//			}
	//		} 


	//TODO new method signature symbolicValueReferences
	public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute ) {
		// Instruction insn = vm.getLastInstruction(); REVIEW
		Instruction insn = instructionToExecute;
		if (insn instanceof InvokeInstruction) {
			String methodName = ((InvokeInstruction) insn).getInvokedMethodName();
			String methodNameWithoutParameterTypes = methodName.substring(0, methodName.indexOf('('));
			if (methodNameWithoutParameterTypes != null) {
				if (methodNameWithoutParameterTypes.equals("dumpRootStructure")) {
					System.out.println(dumpRootStructure(vm));
					dumpStructureCalls++;
				} else if (methodNameWithoutParameterTypes.equals("dumpStructure"))	 {
					System.out.println(dumpStructure(vm));
				} else if (methodNameWithoutParameterTypes.equals("countStructure")) {
					countRootStructure(vm);
//					structureCount++;
				} else if (methodNameWithoutParameterTypes.equals("setCurrentStateToInputState")) {
					setCurrentStateToInputState(vm);
				} 
			}


			//			InvokeInstruction call = (InvokeInstruction) insn;
			//			MethodInfo mi = null;
			//			try {
			//				mi = call.getInvokedMethod();
			//			} catch (Exception e) {
			//				System.out.println("Method does not exist");
			//			}
			//			if (mi != null) {
			//				String mn = mi.getName();
			//				if (mn.equals("dumpRootStructure")) {
			//					System.out.println(dumpRootStructure(vm));
			//					dumpStructureCalls++;
			//				} else if (mn.equals("dumpStructure"))	 {
			//					System.out.println(dumpStructure(vm));
			//				} else if (mn.equals("countStructure")) {
			//					//					countStructure(ti, dei);
			//					countRootStructure(vm);
			//					structureCount++;
			//				} else if (mn.equals("setCurrentStateToInputState")) {
			//					setCurrentStateToInputState(vm);
			//				} 
			//			} else {
			//				System.out.println("Method is null");
			//			}
		}
	}

	private String dumpStructure(VM vm) {
		HeapChoiceGenerator heapCG = vm.getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
		SymbolicInputHeap sih = heapCG.getCurrentSymInputHeap();
		Integer rootIndex = Helper.getRootIndex(sih);

		return this.dumpStructure(vm.getCurrentThread(), vm.getElementInfo(rootIndex));
	}

	private static void recursiveStateUpdate(SymbolicInputHeap sih, Integer nodeIndex, SystemState ss, KernelState ks, ThreadInfo ti, HashSet<Integer> visited){
		HeapNode node = sih.getNodeByIndex(nodeIndex);
		int fieldCount = node.getType().getNumberOfDeclaredInstanceFields();
		ElementInfo ei = ti.getElementInfo(nodeIndex);
		for (int i = 0; i < fieldCount; i++) {
			FieldInfo fi = node.getType().getDeclaredInstanceField(i);
			String fieldName = fi.getName();
			if (fi.isReference()) {
				Integer targetNode = sih.indexViaFieldToIndex.get(new ImmutablePair<Integer, String>(nodeIndex, fieldName));
				if (!fi.getType().contains("[]")){ //we skip arrays for the time being
					if (targetNode != null/* && targetNode != -1*/){
						ei.setReferenceField(fi, targetNode);
					} 
					if (targetNode != null && targetNode != MJIEnv.NULL && visited.add(targetNode))
						recursiveStateUpdate(sih, targetNode, ss, ks, ti, visited);
				} else {
					throw new RuntimeException("Array type not supported");
				}
			} else {
				if (fi.isIntField()){
					ei.setFieldAttr(fi, JPF.symbolicValueReferences.get(new ImmutablePair<Integer, String>(nodeIndex, fi.getName())));
				}
			}
		}

	}



	private void setCurrentStateToInputState(VM vm) {
		HeapChoiceGenerator heapCG = vm.getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
		SymbolicInputHeap sih = heapCG.getCurrentSymInputHeap();
		Integer rootIndex = Helper.getRootIndex(sih);
		SystemState ss = vm.getSystemState();
		KernelState ks = vm.getKernelState();
		ThreadInfo ti = vm.getCurrentThread();
		recursiveStateUpdate(sih, rootIndex, ss, ks, ti, new HashSet<Integer>());
		//		ti.pop();
		//		ti.push(rootIndex);
	}





	private static Set<Integer> dumped = new HashSet<Integer>();

	public String dumpStructure(ThreadInfo ti, ElementInfo ei) {
		dumped.clear();
		return dumpStructure0(ti, ei);
	}



	public String dumpRootStructure(HeapChoiceGenerator heap, Integer nodeIndex) {
		dumped.clear();
		return dumpRootStructure0(heap, nodeIndex);
	}



	public String dumpRootStructure(VM vm) {
		dumped.clear();
		HeapChoiceGenerator heapCG = vm.getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
		SymbolicInputHeap sih = heapCG.getCurrentSymInputHeap();

		Integer rootIndex = sih.getRootIndexFromAnySymbolicIntegerInHeap();

		String s = dumpRootStructure0(heapCG, rootIndex);
		return s;
	}







	private static String dumpRootStructure0(HeapChoiceGenerator heap, Integer currIndex) {
		SymbolicInputHeap sihb = heap.getCurrentSymInputHeap();
		StringBuffer b = new StringBuffer();
		if (currIndex == null) {
			b.append("SYMBOLICOBJ ");
		} else if (currIndex.equals(MJIEnv.NULL)) {
			b.append("NULL ");
		} else {
			if (dumped.add(currIndex)) {
				b.append(currIndex + " ( ");

				HeapNode currNode = sihb.getNodeByIndex(currIndex);

				//				hm.put(Integer.valueOf(currIndex), o);

				int fieldCount = currNode.getType().getNumberOfDeclaredInstanceFields();
				for (int i = 0; i < fieldCount; i++) {
					FieldInfo fi = currNode.getType().getDeclaredInstanceField(i);
					String fieldName = fi.getName();

					if (!fi.isReference()) {
						b.append(fi.getName() + ": SYMBOLICINT ");
					} else {

						Integer targetBoundedNodeIndex = sihb.pointsToThroughField(currIndex, fieldName);
						if (targetBoundedNodeIndex == null){
							b.append(fi.getName() + " : " + "SYMBOLICOBJ ");
						} else if (targetBoundedNodeIndex.equals(MJIEnv.NULL)){
							b.append(fi.getName() + " : " + "NULL ");
						} else {
							String recurResult = dumpRootStructure0(heap, targetBoundedNodeIndex);
							b.append(fi.getName() + " : " + recurResult);
						}	
					}
				}
				b.append(")");

			} else {
				b.append(currIndex + "!");
			}
		}

		return b.toString();
	}		




	private static String dumpStructure0(ThreadInfo ti, ElementInfo ei) {
		StringBuffer b = new StringBuffer();
		if (ei == null) {
			b.append("NULL");
		} else {
			int thisRef = ei.getObjectRef(); 
			if (thisRef == MJIEnv.NULL) {
				b.append("NULL");
			} else if (dumped.add(thisRef)) {
				b.append(thisRef);
				b.append('(');
				int fieldCount = ei.getNumberOfFields();
				for (int i = 0; i < fieldCount; i++) {
					FieldInfo fi = ei.getFieldInfo(i);
					if (i > 0) {
						b.append(", ");
					}
					b.append(fi.getName());
					b.append(": ");
					if (ei.getFieldAttr(fi) != null) {
						b.append("SYMBOLIC");
					} else if (fi.isReference()) {
						int ref = ei.getReferenceField(fi);
						ElementInfo fei = ti.getElementInfo(ref);
						b.append(dumpStructure0(ti, fei));
					} else if (fi.isIntField()) {
						b.append("INT:");
						b.append(ei.getIntField(fi));
					} else {
						b.append("VALUE");
					}
				}
				b.append(')');
			} else {
				b.append(thisRef);
				b.append('!');
			}
		}
		return b.toString();
	}

	private String dumpStructure_NullOnly(ThreadInfo ti, ElementInfo ei) {
		dumped.clear();
		return dumpStructure_NullOnly0(ti, ei);
	}

	private String dumpStructure_NullOnly0(ThreadInfo ti, ElementInfo ei) {
		StringBuffer b = new StringBuffer();
		if (ei == null) {
			b.append("NULL");
		} else {
			int thisRef = ei.getObjectRef();
			if (thisRef == MJIEnv.NULL) {
				b.append("NULL");
			} else if (dumped.add(thisRef)) {
				b.append(thisRef);
				b.append('(');
				int fieldCount = ei.getNumberOfFields();
				for (int i = 0; i < fieldCount; i++) {
					FieldInfo fi = ei.getFieldInfo(i);
					if (i > 0) {
						b.append(", ");
					}
					b.append(fi.getName());
					b.append(": ");
					if (ei.getFieldAttr(fi) != null) {
						b.append("NULL");
					} else if (fi.isReference()) {
						int ref = ei.getReferenceField(fi);
						ElementInfo fei = ti.getElementInfo(ref);
						b.append(dumpStructure_NullOnly0(ti, fei));
					} else if (fi.isIntField()) {
						b.append("INT:");
						b.append(ei.getIntField(fi));
					} else {
						b.append("VALUE");
					}
				}
				b.append(')');
			} else {
				b.append(thisRef);
				b.append('!');
			}
		}
		return b.toString();
	}


	private String dumpRootStructure_NullOnly(HeapChoiceGenerator heap, Integer currIndex) {
		dumped.clear();
		return dumpRootStructure_NullOnly0(heap, currIndex);
	}

	private String dumpRootStructure_NullOnly0(HeapChoiceGenerator heap, Integer currIndex) {
		SymbolicInputHeap sihb = heap.getCurrentSymInputHeap();
		StringBuffer b = new StringBuffer();
		if (currIndex == null) {
			b.append("NULL ");
		} else if (currIndex.equals(MJIEnv.NULL)) {
			b.append("NULL ");
		} else {
			if (dumped.add(currIndex)) {
				b.append(currIndex + " ( ");

				HeapNode currNode = sihb.getNodeByIndex(currIndex);

				//				hm.put(Integer.valueOf(currIndex), o);

				int fieldCount = currNode.getType().getNumberOfDeclaredInstanceFields();
				for (int i = 0; i < fieldCount; i++) {
					FieldInfo fi = currNode.getType().getDeclaredInstanceField(i);
					String fieldName = fi.getName();


					if (!fi.isReference()) {
						b.append(fi.getName() + ": SYMBOLICINT ");
					} else {

						Integer targetBoundedNodeIndex = sihb.pointsToThroughField(currIndex, fieldName);
						if (targetBoundedNodeIndex == null){
							b.append(fi.getName() + " : " + "NULL ");
						} else if (targetBoundedNodeIndex.equals(MJIEnv.NULL)){
							b.append(fi.getName() + " : " + "NULL ");
						} else {
							String recurResult = dumpRootStructure_NullOnly0(heap, targetBoundedNodeIndex);
							b.append(fi.getName() + " : " + recurResult);
						}	
					}
				}
				b.append(") ");

			} else {
				b.append(currIndex + "!");
			}
		}

		return b.toString();
	}





	
	



//	private void countStructure(ThreadInfo ti, ElementInfo ei) {
//
//		if (structures.add(dumpStructure_NullOnly(ti, ei))) {
//			uniqueStructureCount++;
//			System.out.println();
//		}
//	}






	private void countRootStructure(VM vm) {
		this.structureCount++;
		HeapChoiceGenerator heapCG = vm.getLastChoiceGeneratorOfType(HeapChoiceGenerator.class);
		SymbolicInputHeap sih = heapCG.getCurrentSymInputHeap();
		Integer rootIndex = sih.getRootIndexFromAnySymbolicIntegerInHeap();
		String struc = dumpRootStructure_NullOnly(heapCG, rootIndex);
		if (structures.add(struc)) {
			uniqueStructureCount++;
		}

	}






	private Integer getField(ThreadInfo ti, Integer node, String fieldName) {
		if (node == MJIEnv.NULL) {
			return null;
		}
		ElementInfo ei = ti.getElementInfo(node);
		int index = getFieldIndex(ei, fieldName);
		assert(index != MJIEnv.NULL);
		FieldInfo fi = ei.getFieldInfo(index);
		if (ei.getFieldAttr(fi) != null) {
			return null;
		} else {
			int ref = ei.getReferenceField(fieldName);
			//			if (ref == -1) {
			//				return null;
			//			} else {
			//				return ref;
			//			}
			return ref;
		}
	}

	private Integer getIntField(ThreadInfo ti, Integer node, String fieldName) {
		if (node == MJIEnv.NULL) {
			return null;
		}
		ElementInfo ei = ti.getElementInfo(node);
		return ei.getIntField(fieldName);
	}

	private int getFieldIndex(ElementInfo ei, String fieldName) {
		for (int i = 0; i < ei.getNumberOfFields(); i++) {
			FieldInfo fi = ei.getFieldInfo(i);
			if (fi.getName().equals(fieldName)) {
				return i;
			}
		}
		return MJIEnv.NULL;
	}
}
