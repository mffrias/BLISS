package symbolicheap.bounded;

import gov.nasa.jpf.symbc.Debug;

//import randoop.CheckRep;

/*
 * public class A {
    (public|private) Object theField;
}

class B extends C {
    (public | private) void m() {
        (empty | new A().theField=null; | A.this.theField=null; | this.theField=null; | theField=null; | super.theField=null;)
    }

    void ( mPrime() | m(Object newArg)) {
        (m(); | new B().m(); | this.m(); | B.this.m();
    }
}

class C {
    (public|private) Object theField; (el mismo f que A siempre)
}

Todas las ubicaciones de los campos son identicas en las variantes abajo:
 */
public class EclipsePullUpMethodTripleClassChildMethod {

	public static final EclipsePullUpMethodTripleClassChildMethod SYMBOLICECLIPSEPULLUPMETHODTRIPLECLASSCHILDMETHOD = new EclipsePullUpMethodTripleClassChildMethod(); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.



	public int classCase;

	public boolean publicTheField;

	public boolean publicM;

	public int mBody;

	public boolean methodName;

	public int methodBody;


	//	@CheckRep
	public boolean repOK() {
		if (classCase<0 || classCase>5) return false;
		if (mBody<0 || mBody>5) return false;
		if (methodBody<0 || methodBody>3) return false;
		return true;
	}


	public int getClassCase() {
		return classCase;
	}

	public void setClassCase(int classCase) {
		if (classCase<0 || classCase>5) throw new IllegalArgumentException();
		this.classCase = classCase;
	}

	public boolean isPublicTheField() {
		return publicTheField;
	}

	public void setPublicTheField(boolean publicTheField) {
		this.publicTheField = publicTheField;
	}

	public boolean isPublicM() {
		return publicM;
	}

	public void setPublicM(boolean publicM) {
		this.publicM = publicM;
	}

	public int getmBody() {
		return mBody;
	}

	public void setmBody(int mBody) {
		if (mBody<0 || mBody>5) throw new IllegalArgumentException();
		this.mBody = mBody;
	}

	public boolean isMethodName() {
		return methodName;
	}

	public void setMethodName(boolean methodName) {
		this.methodName = methodName;
	}

	public int getMethodBody() {
		return methodBody;
	}

	public void setMethodBody(int methodBody) {
		if (methodBody<0 || methodBody>3) throw new IllegalArgumentException();
		this.methodBody = methodBody;
	}



	public String toString() {
		String result = "public class A { ";
		if (publicTheField) result+= "public ";
		else result += "private ";
		result += "Object theField; ";
		if (classCase==0) {
			result += "} class B extends C { ";
			if (publicM) result += "public ";
			else result += "private ";
			result += "void m() { ";
			switch (mBody) {
			case 0: result += " } "; break;
			case 1: result += "new A().theField=null; } "; break;
			case 2: result += "A.this.theField=null; } "; break;
			case 3: result += "this.theField=null; } "; break;
			case 4: result += "theField=null; } "; break;
			case 5: result += "super.theField=null; } "; break;
			default: break;			
			}
			if (methodName) result += "void mPrime() { ";
			else result += "void m(Object new Arg) { ";
			switch (methodBody) {
			case 0: result += "m(); } }"; break;
			case 1: result += "new B().m(); } }"; break;
			case 2: result += "this.m(); } }"; break;
			case 3: result += "B.this.m(); } }"; break;
			default: break;
			}
			result += "class C { ";
			if (publicTheField) result += "public ";
			else result += "private ";
			result += "Object theField; } }";
		}
		else if (classCase==1) {
			result += "static class C { ";
			if (publicTheField) result += "public ";
			else result += "private ";
			result += "Object theField; } } class B extends A.C { ";
			if (publicM) result += "public ";
			else result += "private ";
			result += "void m() { ";
			switch (mBody) {
			case 0: result += " } "; break;
			case 1: result += "new A().theField=null; } "; break;
			case 2: result += "A.this.theField=null; } "; break;
			case 3: result += "this.theField=null; } "; break;
			case 4: result += "theField=null; } "; break;
			case 5: result += "super.theField=null; } "; break;
			default: break;			
			}
			if (methodName) result += "void mPrime() { ";
			else result += "void m(Object newArg) { ";
			switch (methodBody) {
			case 0: result += "m(); } }"; break;
			case 1: result += "new B().m(); } }"; break;
			case 2: result += "this.m(); } }"; break;
			case 3: result += "B.this.m(); } }"; break;
			default: break;
			}
		}
		else if (classCase==2) {
			result += "class B extends A.C { ";
			if (publicM) result += "public ";
			else result += "private ";
			result += "void m() { ";
			switch (mBody) {
			case 0: result += " } "; break;
			case 1: result += "new A().theField=null; } "; break;
			case 2: result += "A.this.theField=null; } "; break;
			case 3: result += "this.theField=null; } "; break;
			case 4: result += "theField=null; } "; break;
			case 5: result += "super.theField=null; } "; break;
			default: break;			
			}
			if (methodName) result += "void mPrime() { ";
			else result += "void m(Object newArg) { ";
			switch (methodBody) {
			case 0: result += "m(); } }"; break;
			case 1: result += "new B().m(); } }"; break;
			case 2: result += "this.m(); } }"; break;
			case 3: result += "B.this.m(); } }"; break;
			default: break;
			}
			result += "static class C { ";
			if (publicTheField) result += "public ";
			else result += "private ";
			result += "Object theField; } }";
		}
		else if (classCase==3) {
			result += "class B extends C { ";
			if (publicM) result += "public ";
			else result += "private ";
			result += "void m() { ";
			switch (mBody) {
			case 0: result += " } "; break;
			case 1: result += "new A().theField=null; } "; break;
			case 2: result += "A.this.theField=null; } "; break;
			case 3: result += "this.theField=null; } "; break;
			case 4: result += "theField=null; } "; break;
			case 5: result += "super.theField=null; } "; break;
			default: break;			
			}
			if (methodName) result += "void mPrime() { ";
			else result += "void m(Object newArg) { ";
			switch (methodBody) {
			case 0: result += "m(); } } } "; break;
			case 1: result += "new B().m(); } } } "; break;
			case 2: result += "this.m(); } } } "; break;
			case 3: result += "B.this.m(); } } } "; break;
			default: break;
			}
			result += "class C { ";
			if (publicTheField) result += "public ";
			else result += "private ";
			result += "Object theField; }";			
		}
		else if (classCase==4) {
			result += "public void dummyMethod_A() { class B extends A.C { ";
			if (publicM) result += "public ";
			else result += "private ";
			result += "void m() { ";
			switch (mBody) {
			case 0: result += " } "; break;
			case 1: result += "new A().theField=null; } "; break;
			case 2: result += "A.this.theField=null; } "; break;
			case 3: result += "this.theField=null; } "; break;
			case 4: result += "theField=null; } "; break;
			case 5: result += "super.theField=null; } "; break;
			default: break;			
			}
			if (methodName) result += "void mPrime() { ";
			else result += "void m(Object newArg) { ";
			switch (methodBody) {
			case 0: result += "m(); } } } "; break;
			case 1: result += "new B().m(); } } } "; break;
			case 2: result += "this.m(); } } } "; break;
			case 3: result += "B.this.m(); } } } "; break;
			default: break;
			}
			result += "static class C { ";
			if (publicTheField) result += "public ";
			else result += "private ";
			result += "Object theField; } }";				
		}
		else if (classCase==5) {
			result += "public void dummyMethod_A() { class B extends C { ";
			if (publicM) result += "public ";
			else result += "private ";
			result += "void m() { ";
			switch (mBody) {
			case 0: result += " } "; break;
			case 1: result += "new A().theField=null; } "; break;
			case 2: result += "A.this.theField=null; } "; break;
			case 3: result += "this.theField=null; } "; break;
			case 4: result += "theField=null; } "; break;
			case 5: result += "super.theField=null; } "; break;
			default: break;			
			}
			if (methodName) result += "void mPrime() { ";
			else result += "void m(Object newArg) { ";
			switch (methodBody) {
			case 0: result += "m(); } } } } "; break;
			case 1: result += "new B().m(); } } } } "; break;
			case 2: result += "this.m(); } } } } "; break;
			case 3: result += "B.this.m(); } } } } "; break;
			default: break;
			}
			result += "class C { ";
			if (publicTheField) result += "public ";
			else result += "private ";
			result += "Object theField; }";	
		}
		return result;
	}


	private static void dumpStructure() {
		// TODO Auto-generated method stub

	}


	public static void main(String[] args) {

		EclipsePullUpMethodTripleClassChildMethod X = new EclipsePullUpMethodTripleClassChildMethod();
		X = (EclipsePullUpMethodTripleClassChildMethod) Debug.makeSymbolicRef("X", X);

		if (X != null && X.repOK()) {
			dumpStructure();
		}

	}
}
