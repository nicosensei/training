package fr.nikokode.dsexo.hash;

import java.util.Arrays;

import fr.nikokode.dsexo.DoubleChainedList;

/**
 * 
 * Implements a simple generic hashtable with an arrays of prime size. 
 * We use chaining to resolve hash collisions. As we use our implementation of
 * {@link DoubleChainedList}, there might be overflow if collisions occur 
 * too often.
 * 
 * @author nicolas
 *
 * @param <K> type of key objects
 * @param <V> type of value objects
 */
public abstract class HashTable<K, V> {
	
	private class Pair {
		private K key;
		private V value;
		public Pair(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}
		@Override
		public String toString() {
			return "(" + key + ", " + value + ")";
		}		
	}
	
	private DoubleChainedList[] buckets;
	
	private final HashFunction<K> hashFunction;
	
	private final int bucketSize;
	
	public HashTable(
			final int prime, 
			final int maxExpectedElementCount,
			final HashFunction<K> hashFunction) {
		buckets = new DoubleChainedList[prime];
		
		double loadFactor = 
				(double) maxExpectedElementCount / (double) prime;
		bucketSize = new Double(Math.rint(loadFactor)).intValue();
		System.out.println("Max expected = " + maxExpectedElementCount
				+ ": loadFactor=" + loadFactor +", bucketSize=" + bucketSize
				+ " (" + hashFunction.getClass().getSimpleName() + ")");
		
		
		this.hashFunction = hashFunction;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value) {
		int hash = hashFunction.hash(key);
		DoubleChainedList l = buckets[hash];
		if (l == null) {
			l = new  DoubleChainedList(bucketSize);
			l.addLast(new Pair(key, value));
			buckets[hash] = l;
		} else {
			int keyIndex = findKeyIndex(key, l);
			if (DoubleChainedList.NIL == keyIndex) {
				l.addLast(new Pair(key, value));
			} else {
				// We simply overwrite the value with the same key.
				l.replaceAt(keyIndex, value);
			}	
		}
		
	}

	@SuppressWarnings("unchecked")
	public String printContents() {
		String s = "{";
		for (int i = 0; i < buckets.length; i++) {
			DoubleChainedList b = buckets[i];
			if (b != null) {
				s += "\n" + hashFunction.hash(((Pair) b.getFirst()).key) + " > " 
						+ Arrays.toString(b.toArray(false));
			}
		}
		return s + "\n}";
	}
	
	/**
	 * Searches for a key in a subset of values with the same hash.
	 * This is a simple linear search, should be optimized.
	 * @param l
	 * @param key
	 * @return NIL if no match
	 */
	@SuppressWarnings("unchecked")
	private int findKeyIndex(K key, DoubleChainedList l) {
		Object[] traversal = l.toArray(false);
		int length = traversal.length;
		for (int i = 0; i < length; i++) {
			Pair p = (Pair) traversal[i];
			if (p.key.equals(key)) {
				return i;
			}
		}

		return DoubleChainedList.NIL;
	}
	
}
