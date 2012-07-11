package fr.nikokode.dsexo.hash;

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
	
	private final static int MAX_VALUE_SET_SIZE = 100;
	
	private ValueList[] valueSets;
	
	private final HashFunction<K> hashFunction;
	
	public HashTable(
			final int prime, 
			final HashFunction<K> hashFunction) {
		valueSets = new ValueList[prime];
		this.hashFunction = hashFunction;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value) {
		int hash = hashFunction.hash(key);
		ValueList l = valueSets[hash];
		if (l == null) {
			l = new  ValueList(hash, MAX_VALUE_SET_SIZE, key, value);
			valueSets[hash] = l;
		} else {
			int keyIndex = l.findKeyIndex(key);
			if (DoubleChainedList.NIL == keyIndex) {
				l.add(key, value);
			} else {
				// We simply overwrite the value with the same key.
				l.replaceAt(keyIndex, value);
			}	
		}
		
	}

	public String printContents() {
		String s = "{";
		for (int i = 0; i < valueSets.length; i++) {
			ValueList vl = valueSets[i];
			if (vl != null) {
				s += "\n" + vl.toString();
			}
		}
		return s + "\n}";
	}
	
}
