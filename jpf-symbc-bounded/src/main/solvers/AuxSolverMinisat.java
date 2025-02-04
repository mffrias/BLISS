package solvers;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

import java.io.IOException;
import java.io.FileNotFoundException;

//import DimacsCnfParser;
//import MiniSat220;

//import org.sat4j.specs.ISolver;
//import org.sat4j.specs.TimeoutException;
//import org.sat4j.specs.IProblem;
//import org.sat4j.specs.IVecInt;
//import org.sat4j.specs.ContradictionException;
//import org.sat4j.tools.ModelIterator;
//import org.sat4j.reader.Reader;
//import org.sat4j.reader.DimacsReader;
//import org.sat4j.reader.ParseFormatException;
//import org.sat4j.minisat.SolverFactory;
//import org.sat4j.core.VecInt;



/**
 * A wrapper around the auxiliary SAT-solver back-end.
 *
 * (Currently implemented using JNI-based solvers.MiniSat220.)
 *
 */
public class AuxSolverMinisat {

	private static long numOfvalidInstancesGeneratedBySolver = 0;

	public static long getNumOfvalidInstancesGeneratedBySolver() {
		return numOfvalidInstancesGeneratedBySolver;
	}

	public static void incNumOfvalidInstancesGeneratedBySolver(
			long incr) {
		AuxSolverMinisat.numOfvalidInstancesGeneratedBySolver += incr;
	}


	private MiniSat220 _solver;

	boolean _everRun = false, _lastResult = false;

	public AuxSolverMinisat() {

		// testing ModelIterator!
		//_solver = new ModelIterator(SolverFactory.newLight());

		_solver = new MiniSat220();
	}


	public void read(String pathname) {

		System.out.println("Reading DIMACS from " + pathname);
		_solver = DimacsCnfParser.parse(pathname, _solver);

	}

	public boolean solve(int[] assumptions) {

		//		System.out.println("Starting solver...");
		_everRun = true;

		boolean satisfiable = false;

		satisfiable = _solver.solveWithAssumptions(assumptions);

		if (satisfiable) {
			//System.out.println("Satisfiable !");
			//System.out.println(reader.decode(_problem.model()));
			_lastResult = true;
		} else {
			//System.out.println("Unsatisfiable !");
			//IVecInt explanation = _solver.unsatExplanation();
			//System.out.println("UNSAT explanation in terms of assumptions: "
			//				   + explanation.toString());
			_lastResult = false;
		}

		// Debug: ask solver to print out some statistics
		//_solver.printStat(System.out, " -> ");

		return _lastResult;
	}


	public int[] unsatReason() {
		assert _everRun == true && _lastResult == false;

		return _solver.unsatReason();
	}



	boolean modelValue(int varNumber) {
		assert _everRun == true && _lastResult == true;

		return _solver.valueOf(varNumber);
	}

	// Add a clause with the negation of each literal in the
	// current model (assuming we just got a SAT verdict).
	// Only mention the first NPV (i.e. primary) variables.
	// This allows for enumeration of primarily-different models.
	//
	// Return true iff OK and ready for next solve().
	// False means that adding the new clause resulted in a conflict.
	//
	/*
	boolean addNegatedPrimaryModelClause() {
		assert _everRun == true && _lastResult == true;

		List<Integer> negatedPrimaryPartOfModel = new LinkedList<Integer>();
		int largestPVAR = StateSpaceExplorer.getPvars().getHighestPvarNumber();
		// int largestPVAR = 462; // hack for TS10 for unit testing purposes

		for (int v = 1; v <= largestPVAR; v++) {
			boolean truthValue = _solver.valueOf(v);
			if (truthValue) {
				// Positive in model; we want negative in clause
				negatedPrimaryPartOfModel.add(-v);
			} else {
				// Negative in model; we want positive in clause
				negatedPrimaryPartOfModel.add(v);
			}
		}

		int[] clause = new int[negatedPrimaryPartOfModel.size()];
		int j = 0;
		for (Integer lit : negatedPrimaryPartOfModel) {
			clause[j++] = lit;
		}

		//System.out.println("addNegatedPrimaryModelClause(): Adding clause: " + Arrays.toString(clause));

		boolean ok = _solver.addClause(clause);

		//if (!ok) {
		//	System.err.println("addNegatedPrimaryModelClause(): UNSAT after adding clause!!");
		//}

		return ok;
	}
	*/


	int[] model() {
		//System.out.println("everRun = " + _everRun + ", " + " lastResult = " + _lastResult);
		assert _everRun == true && _lastResult == true;

		int nvars = _solver.numberOfVariables();

		int[] mdl = new int[nvars];

		for (int v = 1; v <= nvars; v++) {
			mdl[v - 1] = _solver.valueOf(v) ? v : -v;
		}

		return mdl;
	}

	/*
	int countPrimaryModels(int [] assumptions) {
		// Enumerate all models and return the total number found,
		// including current one.
		// This assumes that lastResult was SAT.

		assert _everRun == true && _lastResult == true;

		//		VecInt assvecint = new VecInt();
		//		for (int j = 0; j < assumptions.length; ++j) {
		//			if (assumptions[j] > 0)
		//				assvecint.push(assumptions[j]);
		//		}

		int result = 1;

		//		System.out.print("*");
		boolean gotUNSAT = false;

		while (!gotUNSAT) {

			//			System.out.println("Adding a clause forbidding the last primary model");
			boolean ok = addNegatedPrimaryModelClause();
			if (!ok) {
				gotUNSAT = true;
				continue;
			}

			gotUNSAT = ! _solver.solveWithAssumptions(assumptions);

			if (!gotUNSAT) {
				result++;
				//				System.out.print("*");
//				for (int index = 1; index <= 118; index++){
//					if (_solver.valueOf(index)){
//						System.out.print(" "+index);
//					} else {
//						System.out.print(" -" + index );
//
//					}
//
//				}
//				System.out.println(" ");

			}


		}
		_lastResult = false;
		//		System.out.println("EXIT");
		return result;
	}
	*/


	@Override
	public String toString() {
		return "<AuxSolverMinisat instance>";
	}


	public static void main(String[] args) {

		assert args.length > 0;
		String pathname_cnf = args[0];
		AuxSolverMinisat solver = new AuxSolverMinisat();
		solver.read(pathname_cnf);

		int na = args.length - 1;
		int[] assumptions = new int[na];

		for (int j = 1; j < args.length; ++j) {
			int ass = Integer.parseInt(args[j]);
			assumptions[j-1] = ass;
		}

		System.out.println("Solving (under " + na + " assumptions) ...");
		boolean satisfiable = solver.solve(assumptions);

		if (satisfiable) {
			System.out.println("Satisfiable !");			
			//System.out.println("model: " + Arrays.toString(solver.modelValue()));
			//System.out.println(reader.decode(problem.model()));
			//System.out.println("\nprimeImplicant(): " + Arrays.toString(solver.primeImplicant()));

			//System.out.println("Testing enumeration ...");
			//int numberOfModels = solver.countPrimaryModels(assumptions);
			//System.out.println("Enumeration done! Found " + numberOfModels + " models.");

		} else {
			System.out.println("Unsatisfiable !");
			int[] explanation = solver.unsatReason();
			System.out.println("UNSAT explanation in terms of assumptions: "
					+ Arrays.toString(explanation));
		}

	}


}
