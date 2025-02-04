package gov.nasa.jpf.symbc.heap.bounded;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A record of the bounds for lazy initialization. Each entry in this map is a
 * four tuple:
 * 
 * <pre>
 * (classname, fieldname, source, targets)
 * </pre>
 * 
 * where {@code classname} is a string that tells the Java class name of the
 * containing class, {@code fieldname} is a string that tells the name of the
 * field, {@code source} identifies the source object, and {@code targets}
 * identifies the possible targets.
 * 
 * @author jaco
 */
public class BoundsMap {

	/**
	 * A nested mapping that records the bounds.
	 */
	private Map<String, Map<String, Map<Integer, BitSet>>> bounds = null;

	/**
	 * Adds a single bound to the map. It may be that there are already other
	 * bounds for the given {@code (classname, fieldname, source)} combination;
	 * in this case, the target is added to the existing bounds. It is also
	 * possible that the combination is entirely new. Note that when a target is
	 * added to the bit set that captures the bounds, we add one to the target
	 * number. That is because the bit for object zero is used to indicate null.
	 * 
	 * @param classname
	 *            the name of the class
	 * @param fieldname
	 *            the name of the field
	 * @param source
	 *            the number of the source object; usually 0 means the (or a)
	 *            root
	 * @param target
	 *            the number of the target object
	 */
	public void addBound(String classname, String fieldname, int source, int target) {
		assert source >= 1;
		assert target >= 0;
		if (bounds == null) {
			bounds = new HashMap<String, Map<String, Map<Integer, BitSet>>>();
		}
		Map<String, Map<Integer, BitSet>> map0 = bounds.get(classname);
		if (map0 == null) {
			map0 = new HashMap<String, Map<Integer, BitSet>>();
			bounds.put(classname, map0);
		}
		Map<Integer, BitSet> map1 = map0.get(fieldname);
		if (map1 == null) {
			map1 = new HashMap<Integer, BitSet>();
			map0.put(fieldname, map1);
		}
		BitSet targets = map1.get(source);
		if (targets == null) {
			targets = new BitSet();
			map1.put(source, targets);
		}
		targets.set(target);
	}

	/**
	 * For a given class, field, and source object(s), returns the bit set of
	 * possible targets. Note that there may be more than one source object, in
	 * which case the return value is the union of the possible targets of each
	 * of the individual source objects.
	 * 
	 * @param classname
	 *            the name of the class
	 * @param fieldname
	 *            the name of the field
	 * @param sourceSet
	 *            a bit set that identifies the source object(s)
	 * @return the potential targets (=values) of the field as a bit set
	 */
	public BitSet getTargets(String classname, String fieldname, BitSet sourceSet) {
		assert !sourceSet.isEmpty();
		BitSet targets = new BitSet();
		if (bounds == null) {
			return targets;
		}
		if (classname.contains(".")){
			String[] classNameParts;
			classNameParts = classname.split("\\.");
			classname = classNameParts[classNameParts.length - 1];
		}
		Map<String, Map<Integer, BitSet>> map0 = bounds.get(classname);
		if (map0 == null) {
			return targets;
		}
		Map<Integer, BitSet> map1 = map0.get(fieldname);
		if (map1 == null) {
			return targets;
		}
		for (int i = sourceSet.nextSetBit(0); i >= 0; i = sourceSet.nextSetBit(i + 1)) {
			BitSet targetsx = map1.get(i);
			if (targetsx != null) {
				targets.or(targetsx);
			}
		}
		return targets;
	}

	@Override
	public String toString() {
		if (bounds == null) {
			return "<EMPTY>";
		}
		StringBuilder b = new StringBuilder();
		for (Entry<String, Map<String, Map<Integer, BitSet>>> e0 : bounds.entrySet()) {
			b.append('<').append(e0.getKey());
			for (Entry<String, Map<Integer, BitSet>> e1 : e0.getValue().entrySet()) {
				b.append('<').append(e1.getKey());
				for (Entry<Integer, BitSet> e2 : e1.getValue().entrySet()) {
					b.append('<').append(e2.getKey());
					b.append(',').append(e2.getValue());
					b.append('>');
				}
				b.append('>');
			}
			b.append('>');
		}
		return b.toString();
	}
}
