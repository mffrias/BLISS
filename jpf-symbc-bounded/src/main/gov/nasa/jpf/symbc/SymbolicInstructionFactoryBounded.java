//
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
//

package gov.nasa.jpf.symbc;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.symbc.bytecode.GETSTATIC;
import gov.nasa.jpf.symbc.bytecode.bounded.ALOADBounded;
import gov.nasa.jpf.symbc.bytecode.bounded.GETFIELDBounded;
import gov.nasa.jpf.symbc.bytecode.bounded.GETSTATICBounded;

public class SymbolicInstructionFactoryBounded extends
		SymbolicInstructionFactory {

	@Override
	public Instruction aload(int localVarIndex) {
		return filter.isPassing(ci)? new ALOADBounded(localVarIndex)
				: super.aload(localVarIndex);
	}

	@Override
	public Instruction getfield(String fieldName, String clsName,
			String fieldDescriptor) {
		return filter.isPassing(ci) ? new GETFIELDBounded(fieldName,
				clsName, fieldDescriptor) : super.getfield(fieldName, clsName,
				fieldDescriptor);
	}

	@Override
	public Instruction getstatic(String fieldName, String clsName,
			String fieldDescriptor) {
		return filter.isPassing(ci) ? new GETSTATIC(fieldName,
				clsName, fieldDescriptor) : super.getstatic(fieldName, clsName,
				fieldDescriptor);
	}

	public static String lazyBoundDB = null;
	
	public static boolean bounded = false;

	public static boolean boundedRefine = false;
	
	public static String pvarsDB = null;
	
	public static boolean useAuxSolver = false;

	public static boolean useAuxSolverDebug = false;
	
	public static boolean boundedSatDB = false;

	public static boolean boundedSatDBDebug = false;

	
	public SymbolicInstructionFactoryBounded(Config conf) {
		super(conf);
		if (lazyMode) {
			String[] db = conf.getStringArray("symbolic.lazy.bounddb");
			if (db != null) {
				lazyBoundDB = db[0];
				System.out.println("symbolic.lazy.bounddb=" + lazyBoundDB);
			}
			
			String isBounded = conf.getProperty("symbolic.lazy.bounded");
			bounded = (isBounded != null && isBounded.equals("true"));
			
			String refine = conf.getProperty("symbolic.lazy.refine");
			boundedRefine = (refine != null && refine.equals("true"));

			String[] pvarsdb = conf.getStringArray("symbolic.lazy.pvarsdb");
			if (pvarsdb != null) {
				pvarsDB = pvarsdb[0];
				System.out.println("symbolic.lazy.pvarsdb=" + pvarsDB);
			}
			String usesolver = conf.getProperty("symbolic.lazy.useauxsolver");
			useAuxSolver = (usesolver != null && usesolver.equals("true"));

			String solverdebug = conf.getProperty("symbolic.lazy.useauxsolver.debug");
			useAuxSolverDebug = (solverdebug != null && solverdebug.equals("true"));
			
			String satDB = conf.getProperty("symbolic.lazy.satdb");
			boundedSatDB = (satDB != null && satDB.equals("true"));

			String satDBDebug = conf.getProperty("symbolic.lazy.satdb.debug");
			boundedSatDBDebug = (satDBDebug != null && satDBDebug.equals("true"));

		}
	}

}
