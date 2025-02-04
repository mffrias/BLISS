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
    public <Integer | Boolean | Object> f;
}
class B <extends A>? {
  public void dummyMethod_B(){
      // dummyVariable has the same type of f
    <Integer | Boolean | Object> dummyVariable=0;
    expr; | Object o=expr; | if (expr) { } | while (expr) { }
  }
}

expr = fieldRef | 
<fieldRef|dummyVariable> <AssignOp> <fieldRef|dummyVariable> |  
<dummyVariable | fieldRef> <== | !=>? <dummyVariable | fieldRef>? ? <dummyVariable | fieldRef> : <dummyVariable | fieldRef> | <fieldRef|dummyVariable> <BinOp> <fieldRef|dummyVariable> | <UnOp> fieldRef

fieldRef = f | new A().f | this.f | super.f | A.this.f | null | (fieldRef)


-----

public class A {
    public <Integer | Boolean | Object> f;
    class B <extends A>? {
      public void dummyMethod_B(){
          <Integer | Boolean | Object> dummyVariable=0;
          expr; | Object o=expr; | if (expr) { } | while (expr) { }
      }
    }
}

-----

public class A {
    public <Integer | Boolean | Object> f;
    public void dummyMethod_A() {
        class B <extends A>? {
            public void dummyMethod_B(){
                <Integer | Boolean | Object> dummyVariable=0;
                expr; | Object o=expr; | if (expr) { } | while (expr) { }
            }
        }
    }
}
 */
public class EclipseEncapsulatedFieldDoubleClassFieldReference {

	public static final EclipseEncapsulatedFieldDoubleClassFieldReference SYMBOLICECLIPSEENCAPSULATEDFIELDDOUBLECLASSFIELDREFERENCE = new EclipseEncapsulatedFieldDoubleClassFieldReference(); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.





	public boolean hybridRepOK(){
		return true;
	}




	/*
	 * case 0 = 0
	 * case 1 = 1
	 * case 2 = 2
	 */
	public int classCase;

	/*
	 * Integer = 0
	 * Boolean = 1
	 * Object = 2
	 */
	public int fType;

	/*
	 * true: B extends A
	 * false: no extension
	 */
	public boolean bExtension;

	/*
	 * Integer = 0
	 * Boolean = 1
	 * Object = 2
	 */
	public int dummyVarType;

	/*
	 * expr = 0
	 * assign = 1
	 * if = 2
	 * while = 3
	 */
	public int methodBody;

	public int getClassCase() {
		return classCase;
	}

	public void setClassCase(int classCase) {
		if (classCase<0 || classCase>2) throw new IllegalArgumentException();
		this.classCase = classCase;
	}

	public int getfType() {
		return fType;
	}

	public void setfType(int fType) {
		if (fType<0 || fType>2) throw new IllegalArgumentException();
		this.fType = fType;
	}

	public boolean isbExtension() {
		return bExtension;
	}

	public void setbExtension(boolean bExtension) {
		this.bExtension = bExtension;
	}

	public int getDummyVarType() {
		return dummyVarType;
	}

	public void setDummyVarType(int dummyVarType) {
		if (dummyVarType<0 || dummyVarType>2) throw new IllegalArgumentException();
		this.dummyVarType = dummyVarType;
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


	public EclipseEncapsulatedFieldDoubleClassFieldReference() {
		super();
		exprCase1 = new FieldReference();
	}

	//@CheckRep
	public boolean repOK() {
		if (classCase<0 || classCase>2) return false;
		if (fType<0 || fType>2) return false;
		if (dummyVarType<0 || dummyVarType>2) return false;
		if (fType!=dummyVarType) return false;
		if (methodBody<0 || methodBody>3) return false;
		if (exprCase1!=null) {
			if (!exprCase1.repOK()) return false;
			if (exprCase2!=null) return false;
			if (exprCase3!=null) return false;
			if (exprCase4!=null) return false;
			if (exprCase5!=null) return false;
		}
		if (exprCase2!=null) { //Assignment
			if (!exprCase2.repOK()) return false;
			if (exprCase1!=null) return false;
			if (exprCase3!=null) return false;
			if (exprCase4!=null) return false;
			if (exprCase5!=null) return false;
			if(fType==2 && exprCase2.getAssignmentOp()!=0){ //Type ==object
				return false;
			}
			//Type=boolean
			if(fType==1 && (exprCase2.getAssignmentOp()!=0 &&exprCase2.getAssignmentOp()!=1 &&exprCase2.getAssignmentOp()!=2 
					&&exprCase2.getAssignmentOp()!=3)) //Type=boolean -> op= "="| "&="|"|="|"^="
				return false;

		}
		if (exprCase3!=null) {//ConditionalExpression
			if (!exprCase3.repOK()) return false;
			if (exprCase1!=null) return false;
			if (exprCase2!=null) return false;
			if (exprCase4!=null) return false;
			if (exprCase5!=null) return false;
			if(fType ==1 && exprCase3.getEquals()!=2)
				return false;
			if((fType ==0 ||fType ==2) && exprCase3.getEquals()==2)//Type ==object|integer
				return false;

		}
		if (exprCase4!=null) { //BinaryExpresion
			if (!exprCase4.repOK()) return false;
			if (exprCase1!=null) return false;
			if (exprCase2!=null) return false;
			if (exprCase3!=null) return false;
			if (exprCase5!=null) return false;
			//restriccciones de tipo
			if(fType==2 &&(exprCase4.getBinaryOp()!=0 &&exprCase4.getBinaryOp()!=1 ))//Type==object
				return false;

			//Type =booolean
			if(fType==1 && (exprCase4.getBinaryOp()!=0 && exprCase4.getBinaryOp()!=1 && exprCase4.getBinaryOp()!=2 
					&& exprCase4.getBinaryOp()!=15 && exprCase4.getBinaryOp()!=16 &&exprCase4.getBinaryOp()!=17 
					&&exprCase4.getBinaryOp()!=18)) //type=boolean ->op = "=="|"!="|"&"| "^"| "||"| "&&"| "|"
				return false;


			//Type =integer ->op != "||" and "&&"
			if(fType==0 && (exprCase4.getBinaryOp()==16 ||exprCase4.getBinaryOp()==17))
				return false;

		}
		if (exprCase5!=null) {//UnaryExpresion
			if (!exprCase5.repOK()) return false;
			if (exprCase1!=null) return false;
			if (exprCase2!=null) return false;
			if (exprCase3!=null) return false;
			if (exprCase4!=null) return false;
			//restriccciones de tipo

			//Type =boolean
			if(fType==1 &&(exprCase5.getBinaryOp()!=0 && exprCase5.getBinaryOp()!=7))//type=boolean ->op= "~"|!
				return false;
			//Type= integer 
			if(fType==0 && exprCase5.getBinaryOp()==7)//type=integer->op !=!
				return false;
			if(fType==2)
				return false;	
		}

		if(exprCase1==null && exprCase2 ==null && exprCase3==null && exprCase4==null && exprCase5==null){
			return false;
		}

		return true;
	}

	public String toString() {
		String result = null;
		if (classCase==0) {
			result = "public class A { public ";
			switch (fType) {
			case 0: result += "Integer theField; } class B "; break;
			case 1: result += "Boolean theField; } class B "; break;
			case 2: result += "Object theField; } class B "; break;
			default: break;
			}
			if (bExtension) result += "extends A ";
			result += "{ public void dummyMethod_B() { ";
			switch (fType) {
			case 0: result += "Integer dummyVariable=0; "; break;
			case 1: result += "Boolean dummyVariable=false; "; break;
			case 2: result += "Object dummyVariable=null; "; break;
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
			case 0: result += expr+"; } }";break;
			case 1: result += "Object o ="+expr+"; } }";break;
			case 2: result += "if ("+expr+") { }; } }";break;
			case 3: result += "while ("+expr+") { }; } }";break;
			default: break;
			}
			}
		else if (classCase==1) {
			result = "public class A { public ";
			switch (fType) {
			case 0: result += "Integer theField; class B "; break;
			case 1: result += "Boolean theField; class B "; break;
			case 2: result += "Object theField; class B "; break;
			default: break;
			}
			if (bExtension) result += "extends A ";
			result += "{ public void dummyMethod_B() { ";
			switch (fType) {
			case 0: result += "Integer dummyVariable=0; "; break;
			case 1: result += "Boolean dummyVariable=false; "; break;
			case 2: result += "Object dummyVariable=null; "; break;
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
			case 1: result += "Object o ="+expr+"; } } }";break;
			case 2: result += "if ("+expr+") { }; } } }";break;
			case 3: result += "while ("+expr+") { }; } } }";break;
			default: break;
			}
		}
		else if (classCase==2) {
			result = "public class A { public ";
			switch (fType) {
			case 0: result += "Integer theField; public void dummyMethod_A() { class B "; break;
			case 1: result += "Boolean theField; public void dummyMethod_A() { class B "; break;
			case 2: result += "Object theField; public void dummyMethod_A() { class B "; break;
			default: break;
			}
			if (bExtension) result += "extends A ";
			result += "{ public void dummyMethod_B() { ";
			switch (fType) {
			case 0: result += "Integer dummyVariable=0; "; break;
			case 1: result += "Boolean dummyVariable=false; "; break;
			case 2: result += "Object dummyVariable=null; "; break;
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
			case 0: result += expr+"; } }";break;
			case 1: result += "Object o ="+expr+"; } } } }";break;
			case 2: result += "if ("+expr+") { }; } } } }";break;
			case 3: result += "while ("+expr+") { }; } } } }";break;
			default: break;
			}
		}
		return result;
	}



	private static void dumpStructure() {
		// TODO Auto-generated method stub

	}


	public static void main(String[] args) {

		EclipseEncapsulatedFieldDoubleClassFieldReference X = new EclipseEncapsulatedFieldDoubleClassFieldReference();
		X = (EclipseEncapsulatedFieldDoubleClassFieldReference) Debug.makeSymbolicRef("X", X);

		if (X != null && X.repOK()) {
				dumpStructure();
		}

	}


}
