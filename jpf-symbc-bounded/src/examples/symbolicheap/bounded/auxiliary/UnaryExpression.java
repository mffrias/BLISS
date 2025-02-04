package symbolicheap.bounded.auxiliary;



public class UnaryExpression {

	
	public static UnaryExpression SYMBOLICUNARYEXPRESSION = new UnaryExpression();
	public static int SYMBOLICINT = (int) Integer.MIN_VALUE;

    /*
     * 0 = "~"
     * 1 = "--"
     * 2 = "--"
     * 3 = "++"
     * 4 = "++"
     * 5 = "-"
     * 6 = "+"
     * 7 = "!"
     */
	public int unaryOp;

	/*
	 * null: dummyvar
	 */
	public FieldReference fieldRefOrDummy;

	//@CheckRep
	public boolean repOK() {
		if(fieldRefOrDummy==null)
			return false;
		if (unaryOp<0 || unaryOp>7) return false;
		if (fieldRefOrDummy!=null && !fieldRefOrDummy.repOK()) return false;
		if(fieldRefOrDummy!=null && fieldRefOrDummy.isFieldRefParenthesis())
			return false;
		return true;
	}

	public String toString() {
		String field = "";
		if (fieldRefOrDummy==null) field = "dummyVariable";
		else field = fieldRefOrDummy.toString();
		String result = "";
		switch (unaryOp) {
		case 0: result += "~"+field; break;
		case 1: result += "--"+field; break;
		case 2: result += field+"--"; break;
		case 3: result += "++"+field; break;
		case 4: result += field+"++"; break;
		case 5: result += "-"+field; break;
		case 6: result += "+"+field; break;
		case 7: result += "!"+field; break;
		default: break;		
		}
		return result;
	}

	public int getBinaryOp() {
		return unaryOp;
	}

	public void setBinaryOp(int unaryOp) {
		if (unaryOp<0 || unaryOp>7) throw new IllegalArgumentException();
		this.unaryOp = unaryOp;
	}

	public FieldReference getFieldRefOrDummyOne() {
		return fieldRefOrDummy;
	}

	public void setFieldRefOrDummyOne(FieldReference fieldRefOrDummyOne) {
		if (fieldRefOrDummyOne!=null && !fieldRefOrDummyOne.repOK()) throw new IllegalArgumentException();
		this.fieldRefOrDummy = fieldRefOrDummyOne;
	}

}
