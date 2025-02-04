package symbolicheap.bounded.auxiliary;



public class BinaryExpression {

	public static BinaryExpression SYMBOLICBINARYEXPRESSION = new BinaryExpression();
	public static int SYMBOLICINT = (int) Integer.MIN_VALUE;

	/*
	 * 0 = "=="
     * 1 = "!="
     * 2 = "&"
     * 3 = "/"
     * 4 = ">"
     * 5 = ">="
     * 6 = "<<"
     * 7 = "<"
     * 8 = "<="
     * 9 = "-"
     * 10 = "+"
     * 11 = "%"
     * 12 = ">>"
     * 13 = ">>>"
     * 14 = "*"
     * 15 = "^"
     * 16 = "||"
     * 17 = "&&"
     * 18 = "|"
	 */
	public int binaryOp;

	/*
	 * null: dummyvar
	 */
	public FieldReference fieldRefOrDummyOneB;

	/*
	 * null: dummyvar
	 */
	public FieldReference fieldRefOrDummyTwoB;

	//@CheckRep
	public boolean repOK() {
		
		if (binaryOp<0 || binaryOp>18) return false;
		if (fieldRefOrDummyOneB!=null && !fieldRefOrDummyOneB.repOK()) return false;
		if (fieldRefOrDummyTwoB!=null && !fieldRefOrDummyTwoB.repOK()) return false;
		
		if(fieldRefOrDummyOneB ==null && fieldRefOrDummyTwoB ==null) return false;
		
		if(fieldRefOrDummyOneB!=null &&fieldRefOrDummyTwoB!=null){

			if(fieldRefOrDummyOneB!= fieldRefOrDummyTwoB)
				return false;
			
			
		}	
		if(fieldRefOrDummyOneB!=null && fieldRefOrDummyOneB.isFieldRefParenthesis())
			return false;
		
		if(fieldRefOrDummyTwoB!=null && fieldRefOrDummyTwoB.isFieldRefParenthesis())
			return false;
	
		return true;
	}

	public String toString() {
		String result = "";
		if (fieldRefOrDummyOneB==null) result += "dummyVariable ";
		else result += fieldRefOrDummyOneB.toString()+" ";
		switch (binaryOp) {
		case 0: result += "== "; break;
		case 1: result += "!= "; break;
		case 2: result += "& "; break;
		case 3: result += "/ "; break;
		case 4: result += "> "; break;
		case 5: result += ">= "; break;
		case 6: result += "<< "; break;
		case 7: result += "< "; break;
		case 8: result += "<= "; break;
		case 9: result += "- "; break;
		case 10: result += "+ "; break;
		case 11: result += "% "; break;
		case 12: result += ">> "; break;
		case 13: result += ">>> "; break;
		case 14: result += "* "; break;
		case 15: result += "^ "; break;
		case 16: result += "|| "; break;
		case 17: result += "&& "; break;
		case 18: result += "| "; break;
		default: break;		
		}
		if (fieldRefOrDummyTwoB==null) result += "dummyVariable";
		else result += fieldRefOrDummyTwoB.toString();
		return result;
	}

	public int getBinaryOp() {
		return binaryOp;
	}

	public void setBinaryOp(int binaryOp) {
		if (binaryOp<0 || binaryOp>17) throw new IllegalArgumentException();
		this.binaryOp = binaryOp;
	}

	public FieldReference getFieldRefOrDummyOne() {
		return fieldRefOrDummyOneB;
	}

	public void setFieldRefOrDummyOne(FieldReference fieldRefOrDummyOne) {
		if (fieldRefOrDummyOne!=null && !fieldRefOrDummyOne.repOK()) throw new IllegalArgumentException();
		this.fieldRefOrDummyOneB = fieldRefOrDummyOne;
	}

	public FieldReference getFieldRefOrDummyTwo() {
		return fieldRefOrDummyTwoB;
	}

	public void setFieldRefOrDummyTwo(FieldReference fieldRefOrDummyTwo) {
		if (fieldRefOrDummyTwo!=null && !fieldRefOrDummyTwo.repOK()) throw new IllegalArgumentException();
		this.fieldRefOrDummyTwoB = fieldRefOrDummyTwo;
	}
	
}
