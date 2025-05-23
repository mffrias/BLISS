/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * The Java Pathfinder core (jpf-core) platform is licensed under the
 * Apache License, Version 2.0 (the "License"); you may not use this file except
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

// author aymeric fromherz aymeric.fromherz@ens.fr 

package gov.nasa.jpf.symbc.bytecode.symarrays;

import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.symbc.arrays.ArrayExpression;
import gov.nasa.jpf.symbc.arrays.SelectExpression;
import gov.nasa.jpf.symbc.arrays.RealStoreExpression;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.IntegerExpression;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.RealConstant;
import gov.nasa.jpf.symbc.numeric.RealExpression;
import gov.nasa.jpf.symbc.numeric.SymbolicReal;


import gov.nasa.jpf.vm.ArrayIndexOutOfBoundsExecutiveException;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;


/**
 * Store into float array
 * ..., arrayref, index, value  => ...
 */
public class FASTORE extends gov.nasa.jpf.jvm.bytecode.FASTORE {

	 @Override
	  public Instruction execute (ThreadInfo ti) {
         // We may need to add the case where we have a smybolic index and a concrete array

          IntegerExpression indexAttr = null;
          ArrayExpression arrayAttr = null;
		  StackFrame frame = ti.getModifiableTopFrame();
          int arrayRef = peekArrayRef(ti); // need to be polymorphic, could be LongArrayStore

		  if (arrayRef ==MJIEnv.NULL) {
		        return ti.createAndThrowException("java.lang.NullPointerException");
		  } 

          // Retrieve the array expression if it was previously in the pathcondition, and store it as an array attr
          PCChoiceGenerator temp_cg = (PCChoiceGenerator)ti.getVM().getLastChoiceGeneratorOfType(PCChoiceGenerator.class);
          if (temp_cg != null) {
              if (temp_cg.getCurrentPC().arrayExpressions.containsKey(ti.getElementInfo(ti.getModifiableTopFrame().peek(2)).toString())) {
                  ti.getModifiableTopFrame().setOperandAttr(2, temp_cg.getCurrentPC().arrayExpressions.get(ti.getElementInfo(ti.getModifiableTopFrame().peek(2)).toString()));
              }
          }

          // If only the value is symbolic, we use the concrete instruction
          if (peekArrayAttr(ti)==null || !(peekArrayAttr(ti) instanceof ArrayExpression)) {
             //In this case, the array isn't symbolic
             if (peekIndexAttr(ti) == null || !(peekIndexAttr(ti) instanceof IntegerExpression)) {
                 return super.execute(ti);
             }
          }

          ChoiceGenerator<?> cg;

          if (!ti.isFirstStepInsn()) { // first time around
              cg = new PCChoiceGenerator(3);
              ((PCChoiceGenerator) cg).setOffset(this.position);
              ((PCChoiceGenerator) cg).setMethodName(this.getMethodInfo().getFullName());
              ti.getVM().setNextChoiceGenerator(cg);
              return this;
          } else { // this is what really returns results
              cg = ti.getVM().getChoiceGenerator();
              assert (cg instanceof PCChoiceGenerator) : "expected PCChoiceGenerator, got: " + cg;
          }
          
          PathCondition pc;
          ChoiceGenerator<?> prev_cg = cg.getPreviousChoiceGeneratorOfType(PCChoiceGenerator.class);
          
          if (prev_cg == null)
              pc = new PathCondition();
          else
              pc = ((PCChoiceGenerator)prev_cg).getCurrentPC();
          
          assert pc != null;

		  if (peekIndexAttr(ti)==null || !(peekIndexAttr(ti) instanceof IntegerExpression)) {
              int index = ti.getTopFrame().peek(1);
              indexAttr = new IntegerConstant(index); 
		  } else {
              indexAttr = (IntegerExpression)peekIndexAttr(ti);
          }

          assert (indexAttr != null) : "indexAttr shouldn't be null in FASTORE instruction";
  
          if (peekArrayAttr(ti)==null || !(peekArrayAttr(ti) instanceof ArrayExpression)) {
              //In this case, the array isn't symbolic
              if (peekIndexAttr(ti) == null || !(peekIndexAttr(ti) instanceof IntegerExpression)) {
                  return super.execute(ti);
              } else {
                  // We create a symbolic array out of the concrete array
                  ElementInfo arrayInfo = ti.getElementInfo(arrayRef);   
                  arrayAttr = ArrayExpression.create(arrayInfo.toString(), arrayInfo.arrayLength());
                  // We add the constraints about all the elements of the array
                  for (int i = 0; i < arrayInfo.arrayLength(); i++) {
                      float arrValue = arrayInfo.getFloatElement(i);
                      pc._addDet(Comparator.EQ, new SelectExpression(arrayAttr, new IntegerConstant(i)), new RealConstant(arrValue));
                  }
              }
          } else {
              arrayAttr = (ArrayExpression)peekArrayAttr(ti);
          }
          assert (arrayAttr != null) : "arrayAttr shouldn't be null in FASTORE instruction";

          if ((Integer)cg.getNextChoice() == 1) { // check bounds of the index
              pc._addDet(Comparator.GE, indexAttr, arrayAttr.length);
              if (pc.simplify()) { // satisfiable
                  ((PCChoiceGenerator) cg).setCurrentPC(pc);
                  return ti.createAndThrowException("java.lang.ArrayIndexOutOfBoundsException", "index greater than array bounds");
              }
              else {
                  ti.getVM().getSystemState().setIgnored(true);
                  return getNext(ti);
              }
          }
          else if ((Integer)cg.getNextChoice() == 2) {
              pc._addDet(Comparator.LT, indexAttr, new IntegerConstant(0));
              if (pc.simplify()) { // satisfiable
                  ((PCChoiceGenerator) cg).setCurrentPC(pc);
                  return ti.createAndThrowException("java.lang.ArrayIndexOutOfBoundsException", "index smaller than array bounds");
              }
              else {
                  ti.getVM().getSystemState().setIgnored(true);
                  return getNext(ti);
              }
          }
          else {
              pc._addDet(Comparator.LT, indexAttr, arrayAttr.length);
              pc._addDet(Comparator.GE, indexAttr, new IntegerConstant(0));
              if (pc.simplify()) { // satisfiable
                  ((PCChoiceGenerator) cg).setCurrentPC(pc);
                  RealExpression sym_value = null;
		          if (frame.getOperandAttr(0) == null || !(frame.getOperandAttr(0) instanceof RealExpression)) {
                      float value = frame.popFloat();
                      sym_value = new RealConstant(value);
                  }
                  else {
                      // The value is symbolic.
                      sym_value = (RealExpression)frame.getOperandAttr(0);
                      frame.popFloat();
                  }
                  // We create a new arrayAttr, and inherits information from the previous attribute
                  ArrayExpression newArrayAttr = new ArrayExpression(arrayAttr);
                  frame.pop(2); // We pop the array and the index

                  RealStoreExpression se = new RealStoreExpression(arrayAttr, indexAttr, sym_value);
                  pc._addDet(Comparator.EQ, se, newArrayAttr);
                  pc.arrayExpressions.put(newArrayAttr.getRootName(), newArrayAttr);

                  return getNext(ti);
             }
             else {
                 ti.getVM().getSystemState().setIgnored(true);
                 return getNext(ti);
             }
          }
      }
	 
}
