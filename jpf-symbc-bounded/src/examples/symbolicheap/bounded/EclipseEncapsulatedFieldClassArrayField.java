package symbolicheap.bounded;
//import randoop.CheckRep;

import gov.nasa.jpf.symbc.Debug;

/* public class A {
  <boolean | int | byte | short | char | long | float | double> <[] | [][]>? theField <[] | [][]>?;
}
 */
public class EclipseEncapsulatedFieldClassArrayField {


	public static EclipseEncapsulatedFieldClassArrayField SYMBOLICECLIPSEENCAPSULATEDFIELDCLASSARRAYFIELD = new EclipseEncapsulatedFieldClassArrayField();
	public static int SYMBOLICINT = (int) Integer.MIN_VALUE;

	public boolean hybridRepOK(){
		if (type == SYMBOLICINT)
			return true;
		if (type<0 || type>7) return false;
		if (arrayDimension == SYMBOLICINT)
			return true;
		if (arrayDimension<0 || arrayDimension>2) return false;
		if (fieldDimension == SYMBOLICINT)
			return true;
		if (fieldDimension<0 || fieldDimension>2) return false;
		return true;

	}
	/*
	 * boolean = 0
	 * int = 1
	 * byte = 2
	 * short = 3
	 * char = 4
	 * long = 5
	 * float = 6
	 * double = 7
	 */
	public int type;

	/*
	 * none = 0
	 * [] = 1
	 * [][] = 2
	 */
	public int arrayDimension;

	/*
	 * none = 0
	 * [] = 1
	 * [][] = 2
	 */
	public int fieldDimension;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		if (type<0 || type>7) throw new IllegalArgumentException();
		this.type = type;
	}

	public int getArrayDimension() {
		return arrayDimension;
	}

	public void setArrayDimension(int arrayDimension) {
		if (arrayDimension<0 || arrayDimension>2) throw new IllegalArgumentException();
		this.arrayDimension = arrayDimension;
	}

	public int getFieldDimension() {
		return fieldDimension;
	}

	public void setFieldDimension(int fieldDimension) {
		if (fieldDimension<0 || fieldDimension>2) throw new IllegalArgumentException();
		this.fieldDimension = fieldDimension;
	}

	public boolean repOK() {
		if (type<0 || type>7) return false;
		if (arrayDimension<0 || arrayDimension>2) return false;
		if (fieldDimension<0 || fieldDimension>2) return false;
		return true;
	}

	public String toString() {
		String returnType = "";
		switch (type) {
		case 0: returnType = "boolean"; break;
		case	1: returnType = "int"; break;
		case	2: returnType = "byte"; break;
		case	3: returnType = "short"; break;
		case	4: returnType = "char"; break;
		case	5: returnType = "long"; break;
		case	6: returnType = "float"; break;
		case	7: returnType = "double"; break;
		default: break;	
		}
		String aDim = "";
		switch (arrayDimension) {
		case 0: aDim = ""; break;
		case 1: aDim ="[]"; break;
		case 2: aDim ="[][]"; break;
		default: break;
		}
		String fDim = "";
		switch (fieldDimension) {
		case 0: aDim = ""; break;
		case 1: aDim ="[]"; break;
		case 2: aDim ="[][]"; break;
		default: break;
		}
		return "public class A { " + returnType + " " + aDim + " theField " + fDim + "; }";
	}






	private static void dumpStructure() {
		// TODO Auto-generated method stub

	}


	// marked in tables as bfsTraverse
	public static void main(String[] args) {

		EclipseEncapsulatedFieldClassArrayField X = new EclipseEncapsulatedFieldClassArrayField();
		X = (EclipseEncapsulatedFieldClassArrayField) Debug.makeSymbolicRef("X", X);

		if (X != null && X.repOK()) {
			dumpStructure();
		}

	}


}
