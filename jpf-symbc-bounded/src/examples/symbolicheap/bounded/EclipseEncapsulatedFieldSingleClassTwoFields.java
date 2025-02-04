package symbolicheap.bounded;
//import randoop.CheckRep;

import gov.nasa.jpf.symbc.Debug;

/*
 * public class A {
    < int theField=10 | boolean theField=true | String theField="abcd" | Byte theField | Integer theField=10 | Boolean theField=true >, theField2 = < new A().theField | A.this.theField | this.theField | theField | super.theField >;
    public static void main(  String[] args){
      A a=new A();
      System.out.println("a.theField" + a.theField + "a.theField2"+ a.theField2);
    }
}

-----

public class A {
    < int theField=10 | boolean theField=true | String theField="abcd" | Byte theField | Integer theField=10 | Boolean theField=true >;
    < same type as theField above > theField2 = < new A().theField | A.this.theField | this.theField | theField | super.theField >;
    public static void main(  String[] args){
      A a=new A();
      System.out.println("a.theField" + a.theField + "a.theField2"+ a.theField2);
    }
}
 */
public class EclipseEncapsulatedFieldSingleClassTwoFields {

	public static final EclipseEncapsulatedFieldSingleClassTwoFields SYMBOLICECLIPSEENCAPSULATEDFIELDSINGLECLASSTWOFIELDS = new EclipseEncapsulatedFieldSingleClassTwoFields(); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.


	public boolean hybridRepOK(){
		return true;
	}


	public boolean classCase;

	/*
	 * 0: int theField=10 
	 * 1: boolean theField=true
	 * 2: String theField="abcd"
	 * 3: Byte theField 
	 * 4: Integer theField=10 
	 * 5: Boolean theField=true 
	 */
	public int fieldOne;

	public boolean isClassCase() {
		return classCase;
	}

	public void setClassCase(boolean classCase) {
		this.classCase = classCase;
	}

	public int getFieldOne() {
		return fieldOne;
	}

	public void setFieldOne(int fieldOne) {
		if (fieldOne<0 || fieldOne>5) throw new IllegalArgumentException();
		this.fieldOne = fieldOne;
	}

	public int getFieldTwo() {
		return fieldTwo;
	}

	public void setFieldTwo(int fieldTwo) {
		if (fieldTwo<0 || fieldTwo>4) throw new IllegalArgumentException();
		this.fieldTwo = fieldTwo;
	}

	/*
	 * 0: new A().theField 
	 * 1: A.this.theField 
	 * 2: this.theField 
	 * 3: theField 
	 * 4: super.theField 
	 */
	public int fieldTwo;






	//	@CheckRep
	public boolean repOK() {
		if (fieldOne<0 || fieldOne>5) return false;
		if (fieldTwo<0 || fieldTwo>4) return false;
		return true;
	}

	public String toString() {
		String result = "public class A { ";
		if (classCase) {
			switch (fieldOne) {
			case 0: result += "int theField=10, "; break;
			case 1: result += "boolean theField=true, "; break;
			case 2: result += "String theField=\"abcd\", "; break;
			case 3: result += "Byte theField, "; break;
			case 4: result += "Integer theField=10, "; break;
			case 5: result += "Boolean theField=true, "; break;
			default: break;			
			}
			switch (fieldTwo) {
			case 0: result += "theField2 = new A().theField; "; break;
			case 1: result += "theField2 = A.this.theField; "; break;
			case 2: result += "theField2 = this.theField; "; break;
			case 3: result += "theField2 = theField; "; break;
			case 4: result += "theField2 = super.theField; "; break;
			default: break;
			}
		}
		else {
			switch (fieldOne) {
			case 0: result += "int theField=10; int "; break;
			case 1: result += "boolean theField=true; boolean "; break;
			case 2: result += "String theField=\"abcd\"; String "; break;
			case 3: result += "Byte theField; Byte "; break;
			case 4: result += "Integer theField=10; Integer "; break;
			case 5: result += "Boolean theField=true; Boolean "; break;
			default: break;			
			}
			switch (fieldTwo) {
			case 0: result += "theField2 = new A().theField; "; break;
			case 1: result += "theField2 = A.this.theField; "; break;
			case 2: result += "theField2 = this.theField; "; break;
			case 3: result += "theField2 = theField; "; break;
			case 4: result += "theField2 = super.theField; "; break;
			default: break;
			}			
		}
		result += "public static void main(  String[] args){ A a=new A(); System.out.println(\"a.theField\" + a.theField + \"a.theField2\"+ a.theField2); } }";
		return result;
	}

	private static void dumpStructure() {
		// TODO Auto-generated method stub

	}


	public static void main(String[] args) {

		EclipseEncapsulatedFieldSingleClassTwoFields X = new EclipseEncapsulatedFieldSingleClassTwoFields();
		X = (EclipseEncapsulatedFieldSingleClassTwoFields) Debug.makeSymbolicRef("X", X);

		if (X != null && X.repOK()) {
			dumpStructure();
		}

	}

}
