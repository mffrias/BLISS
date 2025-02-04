package symbolicheap.bounded;

import gov.nasa.jpf.symbc.Debug;

//import randoop.CheckRep;

/*
 * public class A {
    (public|private) static? Boolean f;
}

class B extends A {
    Boolean g = <f | new A().f | this.f | super.f | A.this.f | null>;
}

-----

public class A {
    (public|private) static? Boolean f;
    class B extends A {
        Boolean g = <f | new A().f | this.f | super.f | A.this.f | null>;
    }
}

-----

public class A {
    (public|private) static? Boolean f;
    public void dummyMethod_A() {
        class B extends A {
            Boolean g = <f | new A().f | this.f | super.f | A.this.f | null>;
        }
    }
}
 */
public class EclipsePullUpFieldDualClassChildField {

	public static final EclipsePullUpFieldDualClassChildField SYMBOLICECLIPSEPULLUPFIELDDUALCLASSCHILDFIELD = new EclipsePullUpFieldDualClassChildField(); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.

	public boolean hybridRepOK(){
		return true;
	}
	
	
	public int classCase;

	public boolean publicF;

	public boolean staticF;
	/*0:f 
	 *1: new A().f 
	 *2: this.f 
	 *3: super.f 
	 *4:A.this.f 
	 *5: null
	 */
	public int gDeclaration;






	public int getClassCase() {
		return classCase;
	}

	public void setClassCase(int classCase) {
		if (classCase<0 || classCase>2) throw new IllegalArgumentException();
		this.classCase = classCase;
	}

	public boolean isPublicF() {
		return publicF;
	}

	public void setPublicF(boolean publicF) {
		this.publicF = publicF;
	}

	public boolean isStaticF() {
		return staticF;
	}

	public void setStaticF(boolean staticF) {
		this.staticF = staticF;
	}

	public int getgDeclaration() {
		return gDeclaration;
	}

	public void setgDeclaration(int gDeclaration) {
		if (gDeclaration<0 || gDeclaration>5) throw new IllegalArgumentException();
		this.gDeclaration = gDeclaration;
	}

	//	@CheckRep
	public boolean repOK() {
		if (classCase<0 || classCase>2) return false;
		if (gDeclaration<0 || gDeclaration>5) return false;
		return true;
	}

	public String toString() {
		String result = "public class A { ";
		if (publicF) result += "public ";
		else result += "private ";
		if (staticF) result += "static ";
		result += "Boolean f; ";
		if (classCase==0) {
			result += "} class B extends A { Boolean g = ";
			switch (gDeclaration) {
			case 0: result += "f; }"; break;
			case 1: result += "new A().f; }"; break;
			case 2: result += "this.f; }"; break;
			case 3: result += "super.f; }"; break;
			case 4: result += "A.this.f; }"; break;
			case 5: result += "null; }"; break;
			default: break;
			}
		}
		else if (classCase==1) {
			result += "class B extends A { Boolean g = ";
			switch (gDeclaration) {
			case 0: result += "f; } }"; break;
			case 1: result += "new A().f; } }"; break;
			case 2: result += "this.f; } }"; break;
			case 3: result += "super.f; } }"; break;
			case 4: result += "A.this.f; } }"; break;
			case 5: result += "null; } }"; break;
			default: break;
			}			
		}
		else if (classCase==2) {
			result += "public void dummyMethod_A() { class B extends A { Boolean g = ";
			switch (gDeclaration) {
			case 0: result += "f; } } }"; break;
			case 1: result += "new A().f; } } }"; break;
			case 2: result += "this.f; } } }"; break;
			case 3: result += "super.f; } } }"; break;
			case 4: result += "A.this.f; } } }"; break;
			case 5: result += "null; } } }"; break;
			default: break;
			}						
		}
		return result;
	}


	private static void dumpStructure() {
		// TODO Auto-generated method stub

	}


	public static void main(String[] args) {

		EclipsePullUpFieldDualClassChildField X = new EclipsePullUpFieldDualClassChildField();
		X = (EclipsePullUpFieldDualClassChildField) Debug.makeSymbolicRef("X", X);

		if (X != null && X.repOK()) {
			dumpStructure();
		}

	}
}
