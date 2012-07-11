package fr.nikokode.dsexo.hash;

import java.util.Arrays;

import fr.nikokode.dsexo.DoubleChainedList;

final class ValueList {

	private final int hashCode;
	private final int maxSize;
	private int size;
	private Object values;

	public ValueList(
			final int hashCode,
			final int maxSize,
			final Object key, 
			final Object value) {
		this.hashCode = hashCode;
		this.maxSize = maxSize;
		size = 1;
		values = new Object[] { key, value };
	}

	void add(Object key, Object value) {
		DoubleChainedList l;
		if (size == 1) {
			l = new DoubleChainedList(maxSize);
			l.addLast(this.values);
		} else {
			l = (DoubleChainedList) this.values;
		}
		l.addLast(new Object[] { key, value });
		this.values = l;
		size++;
	}

	/**
	 * Searches for a key in a subset of values with the same hash.
	 * This is a simple linear search, should be optimized.
	 * @param l
	 * @param key
	 * @return NIL if no match
	 */
	int findKeyIndex(Object key) {
		if (size == 1) {
			return key.equals(((Object[]) values)[0]) 
					? 0 : DoubleChainedList.NIL; 
		}
		Object[] traversal = ((DoubleChainedList) values).toArray(false);
		int length = traversal.length;
		for (int i = 0; i < length; i++) {
			Object[] o = (Object[]) traversal[i];
			if (o[0].equals(key)) {
				return i;
			}
		}

		return DoubleChainedList.NIL;
	}
	
	void replaceAt(int index, Object value) {
		if (size == 1) {
			assert index == 0;
			values = new Object[] { ((Object[]) values)[0], value };
		} else {
			((DoubleChainedList) values).replaceAt(index, value);
		}
	}

	@Override
	public String toString() {
		if (size == 1) {
			Object[] v = (Object[]) values;
			return "" + hashCode + " > (" + v[0] + ", " + v[1] + ")";
		}
		String s = "" + hashCode + " > ";
		Object[] array = ((DoubleChainedList) values).toArray(false);
		for (Object o : array) {
			Object[] pair = (Object[]) o;
			s+= "(" + pair[0] + ", " + pair[1] + ")";
		}
		return s;
	}
	
}
