//
// Copyright (C) 2007 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
//
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
//
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package gov.nasa.jpf.symbc.heap.bounded;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ChoiceGeneratorBase;
import gov.nasa.jpf.vm.SystemState;
import gov.nasa.jpf.symbc.SymbolicInstructionFactoryBounded;
import gov.nasa.jpf.symbc.bytecode.bounded.BoundedHelper;
import gov.nasa.jpf.symbc.heap.HeapNode;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import hampi.stp.ExtractExpr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * A heap choice generator that uses Bounded Lazy Initialization.
 */
public class CopyOfHeapChoiceGeneratorBounded extends ChoiceGeneratorBase<SymbolicInputHeapBounded> {

	/**
	 * A static counter that is used to uniquely name the heap choice
	 * generators.
	 */
	private static int counter = 0;

	/**
	 * The lazy initialization bounds database.
	 */
	private static BoundsMap bounds = null;
	
	public static BoundsMap getBounds() {
		return bounds;
	}

	/**
	 * The name of the root node. There may be several "true" roots such as
	 * Node0, Node1, Node2, but they are combined in a single bit set {1, 2, 3}.
	 * They then represent all three possible roots simultaneously. If the heap
	 * is refined, they will be resolved to a single name.
	 */
	private static BitSet rootsList = null;

	/**
	 * The heap choice generator uses the bounds to build a list of targets for
	 * the choice for which is was created. These targets are either -1 (to
	 * indicate the choice "null"), -2 (to indicate a brand new node), or some
	 * value >= 0 that refers to the index of an existing node on the heap.
	 */
	protected final List<Integer> targetList;

	/**
	 * The name of a brand new node. If the target in {@link #targetList} is -2,
	 * it means that the choice is a brand new node. The name (= bit set) of
	 * this node is stored in this variable.
	 */
	protected BitSet newTarget;

	/**
	 * The number of choices this choice generator offers.
	 */
	protected int numberOfChoices;

	/**
	 * The currently selected choice. Just after the choice generator is
	 * created, this value is -1, meaning that no choice has been selected so
	 * far.
	 */
	protected int currentChoice;

	/**
	 * The symbolic heaps that may arise as a result of each of the choices. The
	 * structures are created here, but populated by the client of the choice
	 * generator; in many (all?) cases, this is an instruction.
	 */
	protected SymbolicInputHeapBounded[] symbolicHeaps;
	
	public SymbolicInputHeapBounded getMostDefinedLIHeap(){

//		int size = BoundedHelper.getLIHeap().size();
		if (symbolicHeaps.length>0){
			int maxSize = 0;
			SymbolicInputHeapBounded current = null;
			for (SymbolicInputHeapBounded sihb : symbolicHeaps){
				if (sihb != null && sihb.count() > maxSize){
					maxSize = sihb.count();
					current = sihb;
				}	
			}
			if (current != null){
				return current;
			} else {

				throw new RuntimeException("This is unexpected.");
			}
		}
		else
			return null;
	}

	/**
	 * The heap path conditions that may arise as a result of each of the
	 * choices. The path conditions are created here, but populated by the
	 * client of the choice generator; in many (all?) cases, this is an
	 * instruction.
	 */
	protected PathCondition[] pcs;

	/**
	 * A string that records the "original" names (class name, field name,
	 * source object number) that triggered the current set of choices. At the
	 * moment, this is used only for debugging purposes.
	 */
	private String originalNames;

	// The shared database of pvar mappings
	private static Map<String, Integer> pvars = null;

	// The shared sat-solver instance to check declarative invariants
	public static org.sat4j.specs.IProblem auxSolver = null;

	/**
	 * Constructs a new heap choice generator that uses bounded lazy
	 * initialization. It initializes all the variables, including the static
	 * bounds map if this information has not yet been read from the database.
	 * 
	 * @param ss
	 *            system state information, used to access the heap
	 * @param config
	 *            JPF configuration, used to read the database
	 * @param classname
	 *            the name of class for the choice
	 * @param fieldname
	 *            the name of the field for the choice
	 * @param source
	 *            the bit set that indicates the "source" object for the choice
	 */
	public CopyOfHeapChoiceGeneratorBounded(SystemState ss, Config config, String classname, String fieldname, BitSet source) {
		super("HCGB_" + counter++);
		if (bounds == null) {
			try {
				setMap(config);
			} catch (Exception e) {
				bounds = null;
				e.printStackTrace();
			}
			if (pvars == null && SymbolicInstructionFactoryBounded.useAuxSolver) {
				try {
					setPvarsMap(config);
				} catch (Exception e) {
					pvars = null;
					e.printStackTrace();
				}
			}
		}
		originalNames = classname + "/" + fieldname + "/" + source;
		BitSet maximalSetOfTargets = bounds.getTargets(classname, fieldname, source);
		targetList = getTargetList(ss, maximalSetOfTargets);
		numberOfChoices = targetList.size();
		if (numberOfChoices == 0) {
			System.out.println("##$$## NO CHOICES for " + originalNames);
		}
		currentChoice = -1;
		isDone = false;
		// The symbolic heaps and path conditions are allocated but not
		// "populated" at this point.
		symbolicHeaps = new SymbolicInputHeapBounded[numberOfChoices];
		pcs = new PathCondition[numberOfChoices];
	}

	/**
	 * Constructs a heap choice generator for the very first choice made in the
	 * program. This is used when the heap is still empty to choose the root
	 * node.
	 * 
	 * @param config
	 *            JPF configuration, used to read the database
	 */
	public CopyOfHeapChoiceGeneratorBounded(Config config) {
		super("HCGB_" + counter++);
		if (bounds == null) {
			try {
				setMap(config);
			} catch (Exception e) {
				bounds = null;
				e.printStackTrace();
			}
			if (pvars == null && SymbolicInstructionFactoryBounded.useAuxSolver) {
				try {
					setPvarsMap(config);
				} catch (Exception e) {
					pvars = null;
					e.printStackTrace();
				}
			}
		}
		targetList = new ArrayList<Integer>();
		targetList.add(-2);
		
		
//		newTarget = new BitSet();
//		for (HeapNode hnb : rootsList){
//			newTarget.or(hnb.getBoundedName()); 
//		}
		
		newTarget = rootsList;
		numberOfChoices = targetList.size();
		currentChoice = -1;
		isDone = false;
		// The symbolic heaps and path conditions are allocated but not
		// "populated" at this point.
		symbolicHeaps = new SymbolicInputHeapBounded[numberOfChoices];
		pcs = new PathCondition[numberOfChoices];
	}

	public String getOriginalNames() {
		return originalNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nasa.jpf.jvm.ChoiceGenerator#getNextChoice()
	 */
	@Override
	public SymbolicInputHeapBounded getNextChoice() {
		if ((currentChoice >= 0) && (currentChoice < numberOfChoices)) {
			if (symbolicHeaps[currentChoice] == null) {
				System.out.println("##$$## SOMETHING VERY BAD");
			}
			return symbolicHeaps[currentChoice].make_copy();
		} else {
			return null;
		}
	}

	/**
	 * Sets the content of a previously allocated heap to the new (extended)
	 * heap. Several heaps are allocated, one for each possible choice. This
	 * method sets the content of the heap that corresponds to the current
	 * choice.
	 * 
	 * @param nextChoice
	 *            the content of the heap
	 */
	public void setNextChoice(SymbolicInputHeapBounded nextChoice) {
		if ((currentChoice >= 0) && (currentChoice < numberOfChoices)) {
			if (nextChoice == null) {
				System.out.println("##$$## SOMETHING BAD");
			}
			symbolicHeaps[currentChoice] = nextChoice;
		}
	}

	/**
	 * Returns the path condition associated with the current choice.
	 * 
	 * @return the current choice's path condition
	 */
	public PathCondition getNextPC() {
		if ((currentChoice >= 0) && (currentChoice < numberOfChoices) && (pcs[currentChoice] != null)) {
			return pcs[currentChoice].make_copy();
		} else {
			return null;
		}
	}

	/**
	 * Sets the path condition associated with the current choice.
	 * 
	 * @param nextChoice
	 *            the new path condition
	 */
	public void setNextPC(PathCondition nextChoice) {
		if ((currentChoice >= 0) && (currentChoice < numberOfChoices)) {
			pcs[currentChoice] = nextChoice;
		}
	}

	/**
	 * Returns the next target choice, which is either -1 (to indicate "null"),
	 * -2 (to indicate a new node), or the index of an existing node on the heap
	 * associated with the current choice.
	 * 
	 * @return the next target
	 */
	public Integer getNextTarget() {
		if ((currentChoice >= 0) && (currentChoice < numberOfChoices)) {
			return targetList.get(currentChoice);
		} else {
			return null;
		}
	}

	/**
	 * For the case where the target is a new node, this method returns the
	 * "name" of the node as a bit set. In fact, it can be invoked at any time
	 * to obtain the name of the new node, should it exist.
	 * 
	 * @return the name of the new node
	 */
	public BitSet getNewTarget() {
		return newTarget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nasa.jpf.jvm.ChoiceGenerator#getChoiceType()
	 */
	@Override
	public Class<SymbolicInputHeapBounded> getChoiceType() {
		return SymbolicInputHeapBounded.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nasa.jpf.jvm.ChoiceGenerator#hasMoreChoices()
	 */
	@Override
	public boolean hasMoreChoices() {
		return !isDone && (currentChoice < numberOfChoices - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nasa.jpf.jvm.ChoiceGenerator#advance()
	 */
	@Override
	public void advance() {
		currentChoice++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nasa.jpf.jvm.ChoiceGenerator#reset()
	 */
	@Override
	public void reset() {
		currentChoice = -1;
		isDone = false;
		// Important to re-allocate and "empty" heaps, because the client is
		// going to populate the heap.
		symbolicHeaps = new SymbolicInputHeapBounded[numberOfChoices];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nasa.jpf.jvm.ChoiceGenerator#getTotalNumberOfChoices()
	 */
	@Override
	public int getTotalNumberOfChoices() {
		return numberOfChoices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nasa.jpf.jvm.ChoiceGenerator#getProcessedNumberOfChoices()
	 */
	@Override
	public int getProcessedNumberOfChoices() {
		return currentChoice + 1;
	}

	/**
	 * Reads the bounds from a text file and records them in the {@link #bounds}
	 * map. It also reads the list of potential roots and records them in the
	 * {@link #rootsList} variable.
	 * 
	 * @param config
	 *            the JPF configuration
	 * @throws Exception
	 *             if the bounds database is not found
	 */
	private static void setMap(Config config) throws Exception {
		if (bounds != null) {
			// previously initialized
			return;
		}
		if (SymbolicInstructionFactoryBounded.lazyBoundDB != null) {
			bounds = new BoundsMap();
			rootsList = new BitSet();
			BufferedReader reader = new BufferedReader(new FileReader(SymbolicInstructionFactoryBounded.lazyBoundDB));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) {
					int actualNodeIndexInSourceBound = BoundedHelper.extractNumber(line);
					BitSet thisNodeActualBound = new BitSet();
					thisNodeActualBound.set(actualNodeIndexInSourceBound);
					
					rootsList.set(BoundedHelper.extractNumber(line));
				} else {
					String[] fields = line.split("/");
					assert (fields.length == 4);
					int objectNr = BoundedHelper.extractNumber(fields[1]);
					int targetNr = BoundedHelper.extractNumber(fields[3]);
					bounds.addBound(fields[0], fields[2], objectNr, targetNr);
				}
			}
			reader.close();
		} else {
			throw new Exception("No bounds database found");
		}
	}

	private static void setPvarsMap(Config config) throws Exception {
		if (pvars != null) {
			return;
		}
		if (SymbolicInstructionFactoryBounded.pvarsDB != null) {
			pvars = new HashMap<String, Integer>();
			BufferedReader reader = new BufferedReader(new FileReader(SymbolicInstructionFactoryBounded.pvarsDB));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0) {
					String[] fields = line.split("/");
					assert (fields.length == 5);
					String key;
					int src = BoundedHelper.extractNumber(fields[1]);
					if (fields[3].equals("null")) {
						key = "{" + src + "}/" + fields[2] + "/null";
					} else {
						int dst = BoundedHelper.extractNumber(fields[3]);
						key = "{" + src + "}/" + fields[2] + "/{" + dst + "}";
					}
					Integer value = Integer.parseInt(fields[4]);
					assert (!pvars.containsKey(key));
					pvars.put(key, value);
				}
			}
			reader.close();

			if (SymbolicInstructionFactoryBounded.useAuxSolver && auxSolver == null) {
				assert (SymbolicInstructionFactoryBounded.pvarsDB != null) : "useauxsolver is true but no pvarsDB given";
				// For now, by convention, we extrapolate the CNF pathname from
				// the pvarsDB pathname
				String cnfPath = SymbolicInstructionFactoryBounded.pvarsDB.replace(".pvars", ".cnf");
				System.out.println("Parsing DIMACS from: " + cnfPath);
				org.sat4j.specs.ISolver solver = org.sat4j.minisat.SolverFactory.newLight();
				org.sat4j.reader.Reader dimacsReader = new org.sat4j.reader.DimacsReader(solver);
				auxSolver = dimacsReader.parseInstance(cnfPath);
			}
		} else {
			throw new Exception("No pvars database found");
		}
	}

	public static int getPvarFor(String src, String field, String dst) {
		String key = src + "/" + field + "/" + dst;
		if (pvars != null && pvars.containsKey(key)) {
			return pvars.get(key);
		} else {
			return -1;
//			throw new RuntimeException(key + " was not found in pvars database");
		}
	}

//	public static int[] getPvarsFor(String src, String field) {
//		// For now we infer the type from the src string
//		String type = extractType(src);
//		List<String> targets = getTargetList(type, src, field);
//		int[] result = new int[targets.size()];
//		int j = 0;
//		for (String dst : targets)
//			result[j++] = getPvarFor(src, field, dst);
//		return result;
//	}

	private List<Integer> getTargetList(SystemState ss, BitSet targets) {
		ChoiceGenerator<?> hcgb0 = ss.getChoiceGenerator();
		if (!(hcgb0 instanceof CopyOfHeapChoiceGeneratorBounded)) {
			hcgb0 = hcgb0.getPreviousChoiceGeneratorOfType(CopyOfHeapChoiceGeneratorBounded.class);
		}
		CopyOfHeapChoiceGeneratorBounded hcgb = (CopyOfHeapChoiceGeneratorBounded) hcgb0;
		SymbolicInputHeapBounded sihb = hcgb.getNextChoice();
		List<Integer> targetList = new ArrayList<Integer>();
		if (targets.get(0)) {
			targetList.add(-1);
			targets.clear(0);
		}
		if (!targets.isEmpty()) {
			// TODO in the past this "special" "whole" target was marked
			// especially with an exclamation mark (back when it was still a
			// string). Now that is gone. Should we add some other special flag?
			targetList.add(-2);
			newTarget = targets;
			for (HeapNode node : sihb.getHeapNodes()) {
				int nodeIndex = node.getIndex();
				BitSet nodeName = sihb.getBoundByIndex(node.getIndex());
				if (nodeName.intersects(targets) && !targetList.contains(nodeIndex)) {
					targetList.add(nodeIndex);
				}
			}
		}
//		System.out.println("Input target set: " + targets.toString());
//		System.out.println("Output intersecting nodes: " + targetList.toString());
//		HeapSymbolicListenerBounded3 hslb3 = new HeapSymbolicListenerBounded3();
//		hslb3.dumpStructure(ss., ei)
//		System.out.println();
		return targetList;
	}

	/**
	 * Returns the bit set that represents all potential roots.
	 * 
	 * @return the root bit set
	 */
	public static BitSet getRootName() {
		return rootsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nasa.jpf.jvm.ChoiceGeneratorBase#toString()
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder(getClass().getName());
		b.append(" {id:\"").append(id);
		b.append("\", ").append(getProcessedNumberOfChoices());
		b.append('/').append(getTotalNumberOfChoices());
		b.append(", orig:").append(originalNames);
		b.append('}');
		return b.toString();
	}

	
	public HeapNode getNodeByIndexAll(Integer index){
		SymbolicInputHeapBounded[] sihbs = this.symbolicHeaps;
		int i = 0;
		while (i < sihbs.length && sihbs[i].getNodeByIndex(index) == null){
			i++;
		}
		if (i<sihbs.length)
			return sihbs[i].getNodeByIndex(index);
		else
			return null;
	}
	
}
