package solvers;


/**
 * Java wrapper for newer MiniSat 2.2.0 (2010) solver.
 * @author NR-RFM
 */
public class MiniSat220 extends NativeSolver {
	
	/**
	 * Constructs a new MiniSAT wrapper.
	 */
	public MiniSat220() {
		super(make());
	}
	
	static {
		loadLibrary("minisat220foo");
	}

	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "MiniSat220";
	}
	
	/**
	 * Returns a pointer to an instance of  MiniSAT.
	 * @return a pointer to an instance of minisat.
	 */
	private static native long make();
	
	/**
	 * {@inheritDoc}
	 * @see kodkod.engine.satlab.NativeSolver#free(long)
	 */
	native void free(long peer);
	
	/**
	 * {@inheritDoc}
	 * @see kodkod.engine.satlab.NativeSolver#addVariables(long, int)
	 */
	native void addVariables(long peer, int numVariables);

	/**
	 * {@inheritDoc}
	 * @see kodkod.engine.satlab.NativeSolver#addClause(long, int[])
	 */
	native boolean addClause(long peer, int[] lits);
	
	/**
	 * {@inheritDoc}
	 * @see kodkod.engine.satlab.NativeSolver#solve(long)
	 */
	native boolean solve(long peer);

	native boolean solveWithAssumptions(long peer, int[] assumps);

	native int[] unsatReason(long peer);
	
	/**
	 * {@inheritDoc}
	 * @see kodkod.engine.satlab.NativeSolver#valueOf(long, int)
	 */
	native boolean valueOf(long peer, int literal);

}
