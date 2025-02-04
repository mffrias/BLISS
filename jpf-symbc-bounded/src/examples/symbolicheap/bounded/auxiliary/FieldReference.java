package symbolicheap.bounded.auxiliary;
//import randoop.CheckRep;

/*
 * fieldRef = f | new A().f | this.f | super.f | A.this.f | null | (fieldRef) 
 * */
public class FieldReference {

	
	public static FieldReference SYMBOLICFIELDREFERENCE = new FieldReference();
	public static int SYMBOLICINT = (int) Integer.MIN_VALUE;

	/*
	 * F = 0
	 * NewAF = 1 
	 * ThisF = 2
	 * SuperF = 3
	 * AThisF = 4
	 * Null = 5
	 */
	public int fieldRef;
	
	public boolean fieldRefParenthesis;

	public int getFieldRef() {
		return fieldRef;
	}

	public void setFieldRef(int fieldRef) {
		if (fieldRef<0 || fieldRef>5) throw new IllegalArgumentException();
		this.fieldRef = fieldRef;
	}

	public boolean isFieldRefParenthesis() {
		return fieldRefParenthesis;
	}

	public void setFieldRefParenthesis(boolean fieldRefParenthesis) {
		this.fieldRefParenthesis = fieldRefParenthesis;
	}

	//@CheckRep
	public boolean repOK() {
		if (fieldRef<0 || fieldRef>5) return false;
		return true;
	}
	
	public String toString() {
		String result = "";
		switch (fieldRef) {
		case 0: result += "theField"; break;
		case 1: result += "new A().f";break;
		case 2: result += "this.f";break;
		case 3: result += "super.f";break;
		case 4: result += "A.this.f";break;
		case 5: result += "null";break;
		}
		if (fieldRefParenthesis) {
			result = "("+result+")";
		}
		return result;
	}
	
}
