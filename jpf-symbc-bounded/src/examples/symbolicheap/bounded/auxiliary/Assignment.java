package symbolicheap.bounded.auxiliary;


public class Assignment {

	public static Assignment SYMBOLICASSIGNMENT = new Assignment();
	public static int SYMBOLICINT = (int) Integer.MIN_VALUE;
	
	/*
	 * 0 = "="
	 * 1 = "&="
     * 2 = "|="
     * 3 = "^="
     * 4 = "/="
     * 5 = "<<="
     * 6 = "+="
     * 7 = "-="
     * 8 = "%="
     * 9 = ">>="
     * 10 = ">>>="
     * 11 = "*="
	 */
	public int assignmentOp;
	
	public int getAssignmentOp() {
		return assignmentOp;
	}

	public void setAssignmentOp(int assignmentOp) {
		if (assignmentOp<0 || assignmentOp>11) throw new IllegalArgumentException();
		this.assignmentOp = assignmentOp;
	}

	public FieldReference getFieldOrDummyOne() {
		return fieldOrDummyOne;
	}

	public void setFieldOrDummyOne(FieldReference fieldOrDummyOne) {
		if (fieldOrDummyOne!=null && !fieldOrDummyOne.repOK()) throw new IllegalArgumentException();
		this.fieldOrDummyOne = fieldOrDummyOne;
	}

	public FieldReference getFieldOrDummyTwo() {
		return fieldOrDummyTwo;
	}

	public void setFieldOrDummyTwo(FieldReference fieldOrDummyTwo) {
		if (fieldOrDummyTwo!=null && !fieldOrDummyTwo.repOK()) throw new IllegalArgumentException();
		this.fieldOrDummyTwo = fieldOrDummyTwo;
	}

	/*
	 * null is dummy
	 */
	public FieldReference fieldOrDummyOne;

	/*
	 * null is dummy
	 */
	public FieldReference fieldOrDummyTwo;

//	@CheckRep
	public boolean repOK() {
		if (assignmentOp<0 || assignmentOp>11) return false;
		if (fieldOrDummyOne!=null && !fieldOrDummyOne.repOK()) return false;
		if (fieldOrDummyTwo!=null && !fieldOrDummyTwo.repOK()) return false;	
	
		if(fieldOrDummyOne ==null && fieldOrDummyTwo ==null) return false;
		
		if(fieldOrDummyOne!=null && fieldOrDummyTwo!=null){
			//if(fieldOrDummyOne.getFieldRef() != fieldOrDummyTwo.getFieldRef())
			//	return false;
			  if(fieldOrDummyOne!=fieldOrDummyTwo)
				  return false;
		
	
		}	
		
		if(fieldOrDummyOne!=null && fieldOrDummyOne.isFieldRefParenthesis())
			return false;
		
		if(fieldOrDummyTwo!=null && fieldOrDummyTwo.isFieldRefParenthesis())
			return false;
		
			
		return true;
	}
	
	/*
	 * 0 = "="
	 * 1 = "&="
     * 2 = "|="
     * 3 = "^="
     * 4 = "/="
     * 5 = "<<="
     * 6 = "+="
     * 7 = "-="
     * 8 = "%="
     * 9 = ">>="
     * 10 = ">>>="
     * 11 = "*="
	 */
	public String toString() {
		String result = "";
		if (fieldOrDummyOne==null) {
			result += "dummyVariable ";
		}
		else {
			result += fieldOrDummyOne.toString()+" ";
		}
		switch (assignmentOp) {
		case 0: result+= "= "; break;
		case 1: result+= "&= "; break;
		case 2: result+= "|= "; break;
		case 3: result+= "^= "; break;
		case 4: result+= "/= "; break;
		case 5: result+= "<<= "; break;
		case 6: result+= "+= "; break;
		case 7: result+= "-= "; break;
		case 8: result+= "%= "; break;
		case 9: result+= ">>= "; break;
		case 10: result+= ">>>= "; break;
		case 11: result+= "*= "; break;
		default: break;		
		}
		if (fieldOrDummyTwo==null) {
			result += "dummyVariable";
		}
		else {
			result += fieldOrDummyTwo.toString();
		}
		return result;
	}
}
