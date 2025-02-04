package demo;

import gov.nasa.jpf.symbc.Debug;

public class ClassExample {

    public static boolean test(int a, int b){
        if (a > b)
            b = a-b;
        if (a - b > 0) {
            b = -a;
            return true;
        } else {
            a = -b;
            return false;
        }
    }

    public static void main(String[] args){
        boolean c = test(0,0);
        //System.out.println("a = " + Debug.getSymbolicIntegerValue(a) + "  b = " + Debug.getSymbolicIntegerValue(b));
	//Debug.printPC("\n Path Condition: ");
    }

}
