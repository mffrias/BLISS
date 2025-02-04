package symbolicheap.bounded.auxiliary;


/*
 * <dummyVariable | fieldRef> <== | !=>? <dummyVariable | fieldRef>? ? <dummyVariable | fieldRef> : <dummyVariable | fieldRef> | 
 * 
 */
public class ConditionalExpression {

	public static ConditionalExpression SYMBOLICCONDITIONALEXPRESSION = new ConditionalExpression();
	public static int SYMBOLICINT = (int) Integer.MIN_VALUE;

	/*
	 * null: dummyvar
	 */
//	private FieldReference fieldRefOrDummyOneC;
	

	public FieldReference fieldRefOrDummyTwoC;

	/*
	 * null: dummyvar
	 */
	public FieldReference fieldRefOrDummyThreeC;

	/*
	 * null: dummyvar
	 */
	public FieldReference fieldRefOrDummyFourC;
	
	/*
	 * 0: == dummyVar
	 * 1: != dummyVar
	 * 2: empty
	 */
	public int equals;
	



	public boolean repOK() {
		
	
		if (fieldRefOrDummyTwoC!=null && !fieldRefOrDummyTwoC.repOK()) return false;
		if (fieldRefOrDummyThreeC!=null && !fieldRefOrDummyThreeC.repOK()) return false;
		if (fieldRefOrDummyFourC!=null && !fieldRefOrDummyFourC.repOK()) return false;
	
		if (fieldRefOrDummyTwoC==null && fieldRefOrDummyThreeC==null&& fieldRefOrDummyFourC==null) return false;
		
		if ( !equalsRefOrDummy(fieldRefOrDummyTwoC,fieldRefOrDummyThreeC)||
				 !equalsRefOrDummy(fieldRefOrDummyTwoC,fieldRefOrDummyFourC) ||!equalsRefOrDummy(fieldRefOrDummyThreeC,fieldRefOrDummyFourC) )
						return false;
		
	
		if (fieldRefOrDummyTwoC!=null && fieldRefOrDummyTwoC.isFieldRefParenthesis()) return false;
		if (fieldRefOrDummyThreeC!=null && fieldRefOrDummyThreeC.isFieldRefParenthesis()) return false;
		if (fieldRefOrDummyFourC!=null && fieldRefOrDummyFourC.isFieldRefParenthesis()) return false;
		
		if(equals==1 && (fieldRefOrDummyTwoC!=null || fieldRefOrDummyThreeC!=null)||fieldRefOrDummyFourC==null ){
			return false;
		}
		if(equals == 0 && (fieldRefOrDummyTwoC==null && fieldRefOrDummyThreeC==null && fieldRefOrDummyFourC!=null ))
			return false;
		

		return true;
	}
	
	
	private boolean equalsRefOrDummy(FieldReference a,FieldReference b){
		if (a!=null && b!=null){
			  if(a!=b)
				  return false;
		}	  
		return true;
	}
	
	
	
/*	public FieldReference getFieldRefOrDummyOne() {
		return fieldRefOrDummyOneC;
	}

	public void setFieldRefOrDummyOne(FieldReference fieldRefOrDummyOne) {
		if (fieldRefOrDummyOne!=null && !fieldRefOrDummyOne.repOK()) throw new IllegalArgumentException();
		this.fieldRefOrDummyOneC = fieldRefOrDummyOne;
	}
*/
	public FieldReference getFieldRefOrDummyTwo() {
		return fieldRefOrDummyTwoC;
	}

	public void setFieldRefOrDummyTwo(FieldReference fieldRefOrDummyTwo) {
		if (fieldRefOrDummyTwo!=null && !fieldRefOrDummyTwo.repOK()) throw new IllegalArgumentException();
		this.fieldRefOrDummyTwoC = fieldRefOrDummyTwo;
	}

	public FieldReference getFieldRefOrDummyThree() {
		return fieldRefOrDummyThreeC;
	}

	public void setFieldRefOrDummyThree(FieldReference fieldRefOrDummyThree) {
		if (fieldRefOrDummyThree!=null && !fieldRefOrDummyThree.repOK()) throw new IllegalArgumentException();
		this.fieldRefOrDummyThreeC = fieldRefOrDummyThree;
	}

	public FieldReference getFieldRefOrDummyFour() {
		return fieldRefOrDummyFourC;
	}

	public void setFieldRefOrDummyFour(FieldReference fieldRefOrDummyFour) {
		if (fieldRefOrDummyFour!=null && !fieldRefOrDummyFour.repOK()) throw new IllegalArgumentException();
		this.fieldRefOrDummyFourC = fieldRefOrDummyFour;
	}

	public int getEquals() {
		return equals;
	}


	public void setEquals(int equals) {
		this.equals = equals;
	}
	
	public String toString() {
		String result = "";

		if (fieldRefOrDummyTwoC==null) {
			result += "dummyVariable ";
		}
		else {
			result += fieldRefOrDummyTwoC.toString()+" ";
		}

		switch (equals) {
		case 0: result += "== dummyVariable?"; break;
		case 1: result += "!= dummyVariable?"; break;
		case 2: result += "? "; break;
		default: break;		
		}
	
		if (fieldRefOrDummyThreeC==null) {
			result += "dummyVariable : ";
		}
		else {
			result += fieldRefOrDummyThreeC.toString()+" : ";
		}
		if (fieldRefOrDummyFourC==null) {
			result += "dummyVariable";
		}
		else {
			result += fieldRefOrDummyFourC.toString();
		}
		return result;
	}
	
}
