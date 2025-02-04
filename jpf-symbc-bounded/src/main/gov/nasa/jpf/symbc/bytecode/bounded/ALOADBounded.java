package gov.nasa.jpf.symbc.bytecode.bounded;

import gov.nasa.jpf.vm.ClassInfo;

import gov.nasa.jpf.vm.KernelState;
import gov.nasa.jpf.vm.SystemState;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.symbc.string.StringExpression;
import gov.nasa.jpf.symbc.string.SymbolicStringBuilder;

public class ALOADBounded extends gov.nasa.jpf.jvm.bytecode.ALOAD {

	public ALOADBounded(int localVarIndex) {
		super(localVarIndex);
	}

	public Instruction execute(ThreadInfo ti) {
		if (!BoundedHelper.isLazy(ti)) {
			return super.execute( ti);
		}

		// ALOAD basic checks
		Object attr = ti.getTopFrame().getLocalAttr(index);
		String typeOfLocalVar = super.getLocalVariableType();
		if (attr == null || typeOfLocalVar.equals("?")
				|| attr instanceof SymbolicStringBuilder
				|| attr instanceof StringExpression) {
			return super.execute(ti);
		}

		// Bounded lazy initialization
		// TODO FIXME
		assert false;
		return this;
//		Integer daIndex = BoundedHelper.handleBoundedLazyInitialization(ss, ks,
//				ti, ClassInfo.getResolvedClassInfo(typeOfLocalVar), "FIXME",
//				"FIXME", "FIXME", attr, "ALOAD");
//		if (daIndex == null) {
//			return this;
//		} else {
//			ti.setLocalVariable(index, daIndex, true);
//			ti.setLocalAttr(index, null);
//			ti.push(daIndex, true);
//			return getNext(ti);
//		}
	}

}