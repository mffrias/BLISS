package symbolicheap.bounded;

public class Entry {
	
	public static final Entry SYMBOLICENTRY = new Entry(); //field added to execute the hybridRepOK.
	public static final int SYMBOLICINT = (int) Integer.MIN_VALUE; //field added to execute the hybridRepOK.

	public static final int RED = 0;

	public static final int BLACK = 1;

	
	public int key;

	public Entry left = null;

	public Entry right = null;

	public Entry parent;

	public int color = BLACK;

	public Entry(){}
	
	public Entry(int key, Entry parent) {
		this.key = key;
		this.parent = parent;
	}

	public Entry(int key, Entry left, Entry right, Entry parent, int color) {
		this.key = key;
		this.left = left;
		this.right = right;
		this.parent = parent;
		this.color = color;
	}

	public int getKey() {
		return key;
	}

	public String toString() {
		String res = "{ " + (color == BLACK ? "B" : "R") + " ";
		if (left == null) {
			res += "null";
		} else {
			res += left.toString();
		}
		res += " ";
		if (right == null) {
			res += "null";
		} else {
			res += right.toString();
		}
		res += " }";
		return res;
	}

	public String concreteString(int max_level, int cur_level) {
		String res;
		if (cur_level == max_level) {
			res = "{ subtree }";
			//		System.out.println("Brekekek");
		} else {
			res = "{ " + (color == BLACK ? "B" : "R") + key + " ";
			if (left == null) {
				res += "null";
			} else {
				res += left.concreteString(max_level, cur_level + 1);
			}
			res += " ";
			if (right == null) {
				res += "null";
			} else {
				res += right.concreteString(max_level, cur_level + 1);
			}
			res += " }";
		}

		return res;
	}

	public void print(int k) {
		
		/*for (int i = 0; i < k; i++)
			System.out.print(" ");*/
		//System.out.println(key + (color == BLACK ? "(B)" : "(R)"));
		
		if (left != null) {
			left.print(k + 2);
		}
		if (right != null) {
			right.print(k + 2);
		}
	}

}