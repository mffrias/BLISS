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

package gov.nasa.jpf.symbc;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.annotation.MJI;
import gov.nasa.jpf.symbc.bytecode.bounded.BoundedHelper;
import gov.nasa.jpf.symbc.heap.HeapChoiceGenerator;
import gov.nasa.jpf.symbc.heap.HeapNode;
import gov.nasa.jpf.symbc.heap.bounded.HeapChoiceGeneratorBounded;
import gov.nasa.jpf.symbc.heap.bounded.SymbolicInputHeapBounded;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.SystemState;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Verify;




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


public class DebugBounded {

	native public static void printPC(String msg);
	native public static String getSolvedPC();
	native public static String getPC_prefix_notation();
	native public static String PC4Z3();

	native public static String getSymbolicIntegerValue(int v);
	native public static String getSymbolicLongValue(long v);
	native public static String getSymbolicShortValue(short v);
	native public static String getSymbolicByteValue(byte v);
	native public static String getSymbolicCharValue(char v);
	native public static String getSymbolicRealValue(double v);
	native public static String getSymbolicRealValue4Z3(double v);
	native public static String getSymbolicBooleanValue(boolean v);
	native public static String getSymbolicStringValue(String v);

	native public static int addSymbolicInt(int v, String name);
	//native public static long addSymbolic(long v, String name);
	//native public static short addSymbolic(short v, String name);
	native public static byte addSymbolicByte(byte v, String name);
	native public static char addSymbolicChar(char v, String name);
	native public static double addSymbolicDouble(double v, String name);
	native public static boolean addSymbolicBoolean(boolean v, String name);
	//native public static String addSymbolic(String v, String name);

	native public static boolean isSymbolicInteger(int v);
	native public static boolean isSymbolicLong(long v);
	native public static boolean isSymbolicShort(short v);
	native public static boolean isSymbolicByte(byte v);
	native public static boolean isSymbolicChar(char v);

	native public static boolean checkAccuracy(double v, double err); 
	// check accuracy of floating point computation
	// wrt given error

	public static void assume (boolean c) {
		if(!c)
			Verify.ignoreIf(true);
	}

	// puts a new symbolic value in the arg attribute
	native public static int makeSymbolicInteger(String name);
	native public static long makeSymbolicLong(String name);
	native public static short makeSymbolicShort(String name);
	native public static byte makeSymbolicByte(String name);
	native public static double makeSymbolicReal(String name);
	native public static boolean makeSymbolicBoolean(String name);
	native public static char makeSymbolicChar(String name);
	native public static String makeSymbolicString(String name);

	// this method should be used instead of the native one in
	// the no-string-models branch of jpf-core
	public static String makeSymbolicString(String name, int size){
		char str[] = new char[size];
		for(int i = 0; i < size; i++) {
			str[i] = makeSymbolicChar(name + i);
		}
		return new String(str);
	}

	// makes v a symbolic object
	//    public static Object makeSymbolicRef(String name, Object v) {
	//    	assert (v!=null); // needed for type info
	//    	if (Verify.randomBool()) {
	//    		makeFieldsSymbolic(name, v);
	//    	}
	//    	else {
	//    		v = makeSymbolicNull(name);
	//    	}
	//    	return v;
	//    }

	public static Object makeSymbolicRef(String name, Object v) {
		assert (v!=null); // needed for type info
		makeFieldsSymbolic(name, v);
		return v;
	}


	native public static void makeFieldsSymbolic(String name, Object v);
	native public static Object makeSymbolicNull(String name);

	native public static void printSymbolicRef(Object v, String msg);

	native public static void printHeapPC(String msg);


	// performs abstract state matching
	native public static boolean matchAbstractState(Object v);

	/* YN: user-defined cost*/
	native public static void addCost(Object v);
	native public static void setLastObservedInputSize(Object v);
	native public static int getLastObservedInputSize();
	native public static double getLastMeasuredMetricValue();
	native public static void clearMeasurements();

	//	public static Object makeSymbolicRefBounded(String name, Object v) {
	//		assert (v != null); // needed for type info
	//		if (Verify.randomBool()) {
	//			makeFieldsSymbolicBounded(name, v);
	//		} else {
	//			v = makeSymbolicNullBounded(name);
	//		}
	//		return v;
	//	}

	public static Object makeSymbolicRefBounded(String name, Object v) {
		assert (v != null); // needed for type info
		makeFieldsSymbolicBounded(name, v);
		return v;
	}

	native public static void makeFieldsSymbolicBounded(String name, Object v);
	native public static int makeSymbolicNullBounded(String name);


	// the purpose of this method is to set the PCheap to the "eq null" constraint for the input specified w/ stringRef
	//	public static int makeSymbolicNullBounded(MJIEnv env, int objRef, int stringRef) {
	//
	//		// introduce a heap choice generator for the element in the heap
	//		ThreadInfo ti = env.getVM().getCurrentThread();
	//		SystemState ss = env.getVM().getSystemState();
	//		HeapChoiceGeneratorBounded cg;
	//
	//		if (!ti.isFirstStepInsn()) {
	//			Config conf = ti.getVM().getConfig();
	//			String boundsFileName = (conf.getStringArray("symbolic.lazy.bounddb"))[0];
	//			// 			int numNodes = Integer.parseInt(boundsFileName.substring(boundsFileName.lastIndexOf("-")+1, boundsFileName.lastIndexOf(".")));
	//
	//			cg = new HeapChoiceGeneratorBounded(1, conf, new Integer[]{});  //new
	//			// 			System.out.println(cg);
	//
	//			ss.setNextChoiceGenerator(cg);
	//			env.repeatInvocation();
	//			return MJIEnv.NULL; // not used anyways
	//		}
	//		//else this is what really returns results
	//
	//		cg = (HeapChoiceGeneratorBounded) ss.getChoiceGenerator();
	//		assert (cg instanceof HeapChoiceGeneratorBounded) :
	//			"expected HeapChoiceGenerator, got: " + cg;
	//
	//		// see if there were more inputs added before
	//		ChoiceGenerator<?> prevHeapCG = cg.getPreviousChoiceGenerator();
	//		while (!((prevHeapCG == null) || (prevHeapCG instanceof HeapChoiceGenerator))) {
	//			prevHeapCG = prevHeapCG.getPreviousChoiceGenerator();
	//		}
	//
	//		PathCondition pcHeap;
	//		SymbolicInputHeapBounded symInputHeap;
	//		if (prevHeapCG == null){
	//
	//			pcHeap = new PathCondition();
	//			symInputHeap = new SymbolicInputHeapBounded();
	//		}
	//		else {
	//			pcHeap = ((HeapChoiceGeneratorBounded)prevHeapCG).getCurrentPCheap();
	//			symInputHeap = ((HeapChoiceGeneratorBounded)prevHeapCG).getCurrentSymInputHeap();
	//		}
	//
	//		String name = env.getStringObject(stringRef);
	//		String refChain = name + "[-1]"; // why is the type used here? should use the name of the field instead
	//
	//		SymbolicInteger newSymRef = new SymbolicInteger( refChain);
	//
	//
	//		// create new HeapNode based on above info
	//		// update associated symbolic input heap
	//
	//		pcHeap._addDet(Comparator.EQ, newSymRef, new IntegerConstant(-1));
	//		((HeapChoiceGeneratorBounded)cg).setCurrentPCheap(pcHeap);
	//		((HeapChoiceGeneratorBounded)cg).setCurrentSymInputHeap(symInputHeap);
	//		//System.out.println(">>>>>>>>>>>> initial pcHeap: " + pcHeap.toString());
	//		return MJIEnv.NULL;
	//	}
	//
	//	public static void makeFieldsSymbolicBounded(MJIEnv env, int objRef, int stringRef, int objvRef) {
	//		// makes all the fields of obj v symbolic and adds obj v to the symbolic heap to kick off lazy initialization
	//
	//		//			System.out.println("Entering makeFieldsSymbolicBounded");
	//		if (objvRef == MJIEnv.NULL)
	//			throw new RuntimeException("## Error: null object");
	//		// introduce a heap choice generator for the element in the heap
	//		ThreadInfo ti = env.getVM().getCurrentThread();
	//		SystemState ss = env.getVM().getSystemState();
	//		HeapChoiceGeneratorBounded cg;
	//
	//		if (!ti.isFirstStepInsn()) {
	//			Config conf = ti.getVM().getConfig();
	//			String boundsFileName = (conf.getStringArray("symbolic.lazy.bounddb"))[0];
	//			//				int numNodes = Integer.parseInt(boundsFileName.substring(boundsFileName.lastIndexOf("-")+1, boundsFileName.lastIndexOf(".")));
	//
	//			try {
	//				HeapChoiceGeneratorBounded.setMap(ti.getVM().getConfig());
	//			} catch (Exception e) {
	//				HeapChoiceGeneratorBounded.setBounds(null);
	//				e.printStackTrace();
	//			}
	//
	//			int numRoots = HeapChoiceGeneratorBounded.getRootName().cardinality();
	//
	//			cg = new HeapChoiceGeneratorBounded(numRoots, ti.getVM().getConfig(), new Integer[]{});  //new
	//			//				System.out.println(cg);
	//
	//			ss.setNextChoiceGenerator(cg);
	//			env.repeatInvocation();
	//			//				return;  // not used anyways
	//		} else {
	//			//else this is what really returns results
	//
	//			cg = (HeapChoiceGeneratorBounded) ss.getChoiceGenerator();
	//			assert (cg instanceof HeapChoiceGeneratorBounded) :
	//				"expected HeapChoiceGeneratorBounded, got: " + cg;
	//
	//			// see if there were more inputs added before
	//			ChoiceGenerator<?> prevHeapCG = cg.getPreviousChoiceGenerator();
	//			while (!((prevHeapCG == null) || (prevHeapCG instanceof HeapChoiceGeneratorBounded))) {
	//				prevHeapCG = prevHeapCG.getPreviousChoiceGenerator();
	//			}
	//
	//			PathCondition pcHeap;
	//			SymbolicInputHeapBounded symInputHeap;
	//			if (prevHeapCG == null){
	//
	//				pcHeap = new PathCondition();
	//				symInputHeap = new SymbolicInputHeapBounded();
	//
	//			}
	//			else {
	//				pcHeap = ((HeapChoiceGeneratorBounded)prevHeapCG).getCurrentPCheap();
	//				symInputHeap = ((HeapChoiceGeneratorBounded)prevHeapCG).getCurrentSymInputHeap();
	//			}
	//
	//
	//			// set all the fields to be symbolic
	//			ClassInfo ci = env.getClassInfo(objvRef);
	//			FieldInfo[] fields = ci.getDeclaredInstanceFields();
	//			FieldInfo[] staticFields = ci.getDeclaredStaticFields();
	//
	//			String name = env.getStringObject(stringRef);
	//			String refChain = name + "["+objvRef+"]"; // why is the type used here? should use the name of the field instead
	//
	//			SymbolicInteger newSymRef = new SymbolicInteger( refChain);
	//			//ElementInfo eiRef = DynamicArea.getHeap().get(objvRef);
	//			ElementInfo eiRef = env.getVM().getHeap().get(objvRef);
	//			BoundedHelper.initializeInstanceFields(fields, eiRef, refChain);
	//			BoundedHelper.initializeStaticFields(staticFields, ci, ti);
	//
	//			// create new HeapNode based on above info
	//			// update associated symbolic input heap
	//
	//			ClassInfo typeClassInfo = eiRef.getClassInfo();
	//
	//			HeapNode n = new HeapNode(objvRef, typeClassInfo, newSymRef);
	//			symInputHeap._add(n, HeapChoiceGeneratorBounded.getRootName());
	//			pcHeap._addDet(Comparator.NE, newSymRef, new IntegerConstant(-1));
	//			((HeapChoiceGeneratorBounded)cg).setCurrentPCheap(pcHeap);
	//			((HeapChoiceGeneratorBounded)cg).setCurrentSymInputHeap(symInputHeap);
	//			//				System.out.println(">>>>>>>>>>>> initial pcHeap: " + pcHeap.toString());
	//			//			return;
	//		}	
	//
	//	}

}
