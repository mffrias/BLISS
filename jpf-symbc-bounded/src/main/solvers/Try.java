package solvers;

import java.util.Arrays;

public class Try {

    //static {
	//	System.loadLibrary("minisat220");
	//}

	public static void main(String[] args) {

		System.out.println("Hello. Looks like the JNI lib was correctly loaded.");
		
		//System.out.println("Now creating a MiniSat220 instance.");
		
		MiniSat220 slvr = new MiniSat220();

		System.out.println("nvars: " + slvr.numberOfVariables() + ", nclauses: " + slvr.numberOfClauses());

		slvr.addVariables(8);
		System.out.println("Adding 8 variables");
		
		System.out.println("nvars: " + slvr.numberOfVariables() + ", nclauses: " + slvr.numberOfClauses());
		
		int[] cl = {5, -2, -1};
		slvr.addClause(cl);
		System.out.println("Added clause: " + Arrays.toString(cl));

		System.out.println("nvars: " + slvr.numberOfVariables() + ", nclauses: " + slvr.numberOfClauses());
		

		boolean issat = false;
		int[] ass = {3, 4, 5, 8};

		System.out.println("Solving under assumptions: " + Arrays.toString(ass) + " ...");
		issat = slvr.solveWithAssumptions(ass);
		
		System.out.println("Got verdict: " + issat);

		if (issat) {
			for (int v = 1; v < slvr.numberOfVariables(); v++) {
				boolean x = slvr.valueOf(v);
				System.out.println("  variable " + v + " = " + x);
			}
		}

		
		
		int[] ass2 = {3, 4, -5, 8};
		
		System.out.println("Solving under assumptions: " + Arrays.toString(ass2) + " ...");
		issat = slvr.solveWithAssumptions(ass2);
		
		System.out.println("Got verdict: " + issat);
		
		if (issat) {
			for (int v = 1; v < slvr.numberOfVariables(); v++) {
				boolean x = slvr.valueOf(v);
				System.out.println("  variable " + v + " = " + x);
			}
		}
		
		
	}

}

