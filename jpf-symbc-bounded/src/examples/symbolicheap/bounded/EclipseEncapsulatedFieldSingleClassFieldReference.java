package symbolicheap.bounded;
import gov.nasa.jpf.symbc.Debug;
import symbolicheap.bounded.auxiliary.Assignment;
import symbolicheap.bounded.auxiliary.BinaryExpression;
import symbolicheap.bounded.auxiliary.ConditionalExpression;
import symbolicheap.bounded.auxiliary.FieldReference;
import symbolicheap.bounded.auxiliary.UnaryExpression;
//import randoop.CheckRep;

/*
 * public class A {
    public <Byte | Integer | Boolean | Object> f;
    public void dummyMethod_A(){
        // dummyVariable has the same type of f
        <Integer | Boolean | Object> dummyVariable=0;
        expr; | Object o=expr; | if (expr) { } | while (expr) { }
    }
}

expr = fieldRef | <fieldRef|dummyVariable> <AssignOp> <fieldRef|dummyVariable> |  <dummyVariable | fieldRef> <== | !=>? <dummyVariable | fieldRef>? ? <dummyVariable | fieldRef> : <dummyVariable | fieldRef> | <fieldRef|dummyVariable> <BinOp> <fieldRef|dummyVariable> | <UnOp> fieldRef |

fieldRef = f | new A().f | this.f | A.this.f | null | (fieldRef)
 */
public class EclipseEncapsulatedFieldSingleClassFieldReference {

	public static final EclipseEncapsulatedFieldSingleClassFieldReference SYMBOLICECLIPSEENCAPSULATEDFIELDSINGLECLASSFIELDREFERENCE = new EclipseEncapsulatedFieldSingleClassFieldReference(); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.


	public boolean hybridRepOK(){
		return true;
	}



	/*
	 * Integer = 0
	 * Boolean = 1
	 * Object = 2
	 * Byte = 3
	 */
	public int fType;

	/*
	 * expr = 0
	 * assign = 1
	 * if = 2
	 * while = 3
	 */
	public int methodBody;

	public int getfType() {
		return fType;
	}

	public void setfType(int fType) {
		if (fType<0 || fType>3) throw new IllegalArgumentException();
		this.fType = fType;
	}

	public int getMethodBody() {
		return methodBody;
	}

	public void setMethodBody(int methodBody) {
		if (methodBody<0 || methodBody>3) throw new IllegalArgumentException();
		this.methodBody = methodBody;
	}

	public FieldReference getExprCase1() {
		return exprCase1;
	}

	public void setExprCase1(FieldReference exprCase1) {
		if (exprCase1==null) throw new IllegalArgumentException();
		if (!exprCase1.repOK()) throw new IllegalArgumentException();
		this.exprCase1 = exprCase1;
		this.exprCase2 = null;
		this.exprCase3 = null;
		this.exprCase4 = null;
		this.exprCase5 = null;
	}

	public Assignment getExprCase2() {
		return exprCase2;
	}

	public void setExprCase2(Assignment exprCase2) {
		if (exprCase2==null) throw new IllegalArgumentException();
		if (!exprCase2.repOK()) throw new IllegalArgumentException();
		this.exprCase2 = exprCase2;
		this.exprCase1 = null;
		this.exprCase3 = null;
		this.exprCase4 = null;
		this.exprCase5 = null;
	}

	public ConditionalExpression getExprCase3() {
		return exprCase3;
	}

	public void setExprCase3(ConditionalExpression exprCase3) {
		if (exprCase3==null) throw new IllegalArgumentException();
		if (!exprCase3.repOK()) throw new IllegalArgumentException();
		this.exprCase3 = exprCase3;
		this.exprCase1 = null;
		this.exprCase2 = null;
		this.exprCase4 = null;
		this.exprCase5 = null;
	}

	public BinaryExpression getExprCase4() {
		return exprCase4;
	}

	public void setExprCase4(BinaryExpression exprCase4) {
		if (exprCase4==null) throw new IllegalArgumentException();
		if (!exprCase4.repOK()) throw new IllegalArgumentException();
		this.exprCase4 = exprCase4;
		this.exprCase1 = null;
		this.exprCase2 = null;
		this.exprCase3 = null;
		this.exprCase5 = null;
	}

	public UnaryExpression getExprCase5() {
		return exprCase5;
	}

	public void setExprCase5(UnaryExpression exprCase5) {
		if (exprCase5==null) throw new IllegalArgumentException();
		if (!exprCase5.repOK()) throw new IllegalArgumentException();
		this.exprCase5 = exprCase5;
		this.exprCase1 = null;
		this.exprCase2 = null;
		this.exprCase3 = null;
		this.exprCase4 = null;
	}

	public FieldReference exprCase1;

	public Assignment exprCase2;

	public ConditionalExpression exprCase3;

	public BinaryExpression exprCase4;

	public UnaryExpression exprCase5;


	public EclipseEncapsulatedFieldSingleClassFieldReference() {
		super();
		exprCase1 = new FieldReference();
	}

	//	@CheckRep
	public boolean repOK() {
		if (fType<0 || fType>3) return false;
		if (methodBody<0 || methodBody>3) return false;
		if (exprCase1!=null) {
			if (!exprCase1.repOK()) return false;
			if (exprCase2!=null) return false;
			if (exprCase3!=null) return false;
			if (exprCase4!=null) return false;
			if (exprCase5!=null) return false;
		}
		if (exprCase2!=null) {
			if (!exprCase2.repOK()) return false;
			if (exprCase1!=null) return false;
			if (exprCase3!=null) return false;
			if (exprCase4!=null) return false;
			if (exprCase5!=null) return false;
		}
		if (exprCase3!=null) {
			if (!exprCase3.repOK()) return false;
			if (exprCase1!=null) return false;
			if (exprCase2!=null) return false;
			if (exprCase4!=null) return false;
			if (exprCase5!=null) return false;
		}
		if (exprCase4!=null) {
			if (!exprCase4.repOK()) return false;
			if (exprCase1!=null) return false;
			if (exprCase2!=null) return false;
			if (exprCase3!=null) return false;
			if (exprCase5!=null) return false;
		}
		if (exprCase5!=null) {
			if (!exprCase5.repOK()) return false;
			if (exprCase1!=null) return false;
			if (exprCase2!=null) return false;
			if (exprCase3!=null) return false;
			if (exprCase4!=null) return false;
		}
		return true;
	}

	public String toString() {
		String result = "public class A { public ";
		switch (fType) {
		case 0: result += "Integer f; "; break;
		case 1: result += "Boolean f; "; break;
		case 2: result += "Object f;  "; break;
		case 3: result += "Byte f; "; break;
		default: break;
		}
		result += "public void dummyMethod_A() { ";
		switch (fType) {
		case 0: result += "Integer dummyVariable=0; "; break;
		case 1: result += "Boolean dummyVariable=0; "; break;
		case 2: result += "Object dummyVariable=0; "; break;
		case 3: result += "Byte dummyVariable=0; "; break;
		default: break;
		}
		String expr = "";
		if (exprCase1!=null) {
			expr = exprCase1.toString();
		}
		else if (exprCase2!=null) {
			expr = exprCase2.toString();
		}
		else if (exprCase3!=null) {
			expr = exprCase3.toString();
		}
		else if (exprCase4!=null) {
			expr = exprCase4.toString();
		}
		else if (exprCase5!=null) {
			expr = exprCase5.toString();
		}
		switch (methodBody) {
		case 0: result += expr+"; } }";
		case 1: result += "Object o ="+expr+"; } }";
		case 2: result += "if ("+expr+") { }; } }";
		case 3: result += "while ("+expr+") { }; } }";
		default: break;
		}
		return result;
	}

	private static void dumpStructure() {
		// TODO Auto-generated method stub

	}


	public static void main(String[] args) {

		EclipseEncapsulatedFieldSingleClassFieldReference X = new EclipseEncapsulatedFieldSingleClassFieldReference();
		X = (EclipseEncapsulatedFieldSingleClassFieldReference) Debug.makeSymbolicRef("X", X);

		if (X != null && X.repOK()) {
			dumpStructure();
		}

	}
}
