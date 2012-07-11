package fr.nikokode.dsexo.hash;

/**
 * 
 * @author nicolas
 *
 * @param <K> the type of key object to hash
 */
public abstract class HashFunction<K> {
	
	public abstract int hash(K key);

}
