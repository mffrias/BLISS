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
package gov.nasa.jpf.symbc.bytecode.bounded;

import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.KernelState;
import gov.nasa.jpf.vm.SystemState;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.symbc.heap.Helper;
import gov.nasa.jpf.symbc.string.StringExpression;
import gov.nasa.jpf.symbc.string.SymbolicStringBuilder;

public class GETSTATICBounded extends gov.nasa.jpf.jvm.bytecode.GETSTATIC {

	public GETSTATICBounded(String fieldName, String clsName,
			String fieldDescriptor) {
		super(fieldName, clsName, fieldDescriptor);
	}

	@Override
	public Instruction execute(ThreadInfo ti) {
		if (!BoundedHelper.isLazy(ti)) {
			return super.execute(ti);
		}

		// GETSTATIC basic checks
		FieldInfo fi = getFieldInfo();
		if (fi == null) {
			return ti.createAndThrowException("java.lang.NoSuchFieldException",
					(className + '.' + fname));
		}
		ClassInfo ci = fi.getClassInfo();
		ElementInfo ei = ci.getModifiableStaticElementInfo(); // statics does not exist
		Object attr = ei.getFieldAttr(fi);
		if (!(fi.isReference() && attr != null && attr != Helper.SymbolicNull)) {
			return super.execute(ti);
		}
		if (attr instanceof StringExpression
				|| attr instanceof SymbolicStringBuilder) {
			return super.execute(ti);
		}

		// Bounded lazy initialization
		// TODO FIXME
		assert false;
		return this;
//		Integer daIndex = BoundedHelper.handleBoundedLazyInitialization(ss, ks,
//				ti, ci, "FIXME", "FIXME", "FIXME", attr, "GETSTATIC");
//		if (daIndex == null) {
//			return this;
//		} else {
//			ei.setReferenceField(fi, daIndex);
//			ei.setFieldAttr(fi, Helper.SymbolicNull);
//			ti.push(ei.getReferenceField(fi), fi.isReference());
//			return getNext(ti);
//		}
	}

}
