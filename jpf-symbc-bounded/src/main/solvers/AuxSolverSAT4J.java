package solvers;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Arrays;

import java.io.IOException;
import java.io.FileNotFoundException;

import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.ContradictionException;
import org.sat4j.tools.ModelIterator;
import org.sat4j.reader.Reader;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.core.VecInt;



/**
 * A wrapper around the auxiliary SAT-solver back-end.
 *
 * (Currently implemented using SAT4J's "light" SAT-solver.)
 *
 */
public class AuxSolverSAT4J {

	private static long numOfvalidInstancesGeneratedBySolver = 0;

	public static long getNumOfvalidInstancesGeneratedBySolver() {
		return numOfvalidInstancesGeneratedBySolver;
	}

	public static void incNumOfvalidInstancesGeneratedBySolver(
			long incr) {
		AuxSolverSAT4J.numOfvalidInstancesGeneratedBySolver += incr;
	}


	private ISolver _solver;
	private IProblem _problem;

	boolean _everRun = false, _lastResult = false;

	final int NPV = 102;  // HACK! TODO: Parametrizar

	public AuxSolverSAT4J() {

		// testing ModelIterator!
		//_solver = new ModelIterator(SolverFactory.newLight());

		_solver = SolverFactory.newLight();
	}

	public void read(String pathname) {

		Reader reader = new DimacsReader(_solver);

		try {            
			System.out.println("Reading DIMACS from " + pathname);
			_problem = reader.parseInstance(pathname);

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + pathname);
		} catch (ParseFormatException e) {
			System.out.println("File contains something fishy: " + pathname);
		} catch (IOException e) {
			System.out.println("IOException while parsing: " + pathname);
		} catch (ContradictionException e) {
			System.out.println("Unsatisfiable (trivial)!");
		}
	}

	public boolean solve(int[] assumptions) {

		VecInt assvecint = new VecInt();
		for (int j = 0; j < assumptions.length; ++j) {
			//System.out.println("Adding assumption: " + assumptions[j]);
			if (assumptions[j] > 0)
				assvecint.push(assumptions[j]);
		}
		//System.out.println("Added " + assvecint.size() + " assumptions");
		//System.out.println("Starting solver...");
		_everRun = true;

		boolean satisfiable = false;

		try {
			satisfiable = _problem.isSatisfiable(assvecint);
		}
		catch (TimeoutException e) {
			assert false;  // PEND: let's suppose this doesn't happen, for now
		}

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

		IVecInt reason = _solver.unsatExplanation();
		return (int[]) reason.toArray().clone();  // Could avoid clone() if guaranteed read-only
	}


	public int[] primeImplicant() {
		assert _everRun == true && _lastResult == true;

		return _solver.primeImplicant();
	}


	boolean modelValue(int varNumber) {
		assert _everRun == true && _lastResult == true;

		return _solver.model(varNumber);
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

		VecInt negatedPrimaryPartOfModel = new VecInt();
		int largestPVAR = StateSpaceExplorer.getPvars().getHighestPvarNumber();
		
//		int[] myModel = _solver.model();
		
		for (int v = 1; v <= largestPVAR; v++) {
//			System.out.println(v);
			
//			if (v == 98){
//				v = v;
//			}
//			
			boolean truthValue = _solver.model(v);
			if (truthValue) {
				// Positive in model; we want negative in clause
				negatedPrimaryPartOfModel.push(-v);
			} else {
				// Negative in model; we want positive in clause
				negatedPrimaryPartOfModel.push(v);
			}
		}
		try {
			_solver.addClause(negatedPrimaryPartOfModel);
		} catch (ContradictionException e) {
			return false;
		}

		return true;
	}
	*/


	int[] modelValue() {
//		System.out.println("everRun = " + _everRun + ", " + " lastResult = " + _lastResult);
		assert _everRun == true && _lastResult == true;

		return _solver.model();
	}

	/*
	int countPrimaryModels(int [] assumptions) {
		// Enumerate all models and return the total number found,
		// including current one.
		// This assumes that lastResult was SAT.

		assert _everRun == true && _lastResult == true;

		VecInt assvecint = new VecInt();
		for (int j = 0; j < assumptions.length; ++j) {
			if (assumptions[j] > 0)
				assvecint.push(assumptions[j]);
		}

		int result = 1;

//		System.out.print("*");
		boolean gotUNSAT = false;

		while (!gotUNSAT) {

			//System.out.println("Adding a clause forbidding the last primary model");
			boolean ok = addNegatedPrimaryModelClause();
			assert ok;

			try {

				gotUNSAT = ! _solver.isSatisfiable(assvecint);

			} catch(TimeoutException e) {
				throw new RuntimeException("AuxSolver.countModels(): TIMEOUT!");
			}

			if (!gotUNSAT) {
				result++;
//				System.out.print("*");
			}

		}
		_lastResult = false;
		return result;
	}
	*/


	public void reset(){
		this._solver.reset();
	}

	@Override
	public String toString() {
		return "<AuxSolver instance>";
	}


	public static void main(String[] args) {

		assert args.length > 0;
		String pathname_cnf = args[0];
		AuxSolverSAT4J solver = new AuxSolverSAT4J();
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
