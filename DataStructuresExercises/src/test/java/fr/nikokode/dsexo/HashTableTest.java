package fr.nikokode.dsexo;

import org.junit.Test;

import fr.nikokode.dsexo.hash.HashFunction;
import fr.nikokode.dsexo.hash.HashTable;

import junit.framework.TestCase;

public class HashTableTest extends TestCase {
	
	/**
	 * Simplest integer hash function.
	 */
	class ModuloHash extends HashFunction<Integer> {

		private final int prime;
		
		public ModuloHash(int prime) {
			super();
			this.prime = prime;
		}

		@Override
		public int hash(Integer key) {
			return key.intValue() % prime;
		}
		
	}
	
	class ModuloHashTable extends HashTable<Integer, String> {
		
		ModuloHashTable() {
			super(PRIME_SMALL, new ModuloHash(PRIME_SMALL));
		}
		
	}
	
	private final static int PRIME_SMALL = 31;
	
	private final static int PRIME_BIG = 967;

	public HashTableTest(String name) {
		super(name);
	}
	
	@Test
	public final void testModuloHash() {
		ModuloHashTable t = new ModuloHashTable();
		
		t.put(0, "a");
		assertEquals("{\n0 > (0, a)\n}", t.printContents());
		
		t.put(1, "b");
		assertEquals("{\n0 > (0, a)\n1 > (1, b)\n}", t.printContents());
		
		t.put(PRIME_SMALL - 1, "c");
		assertEquals(
				"{\n0 > (0, a)\n1 > (1, b)\n30 > (30, c)\n}", 
				t.printContents());
		
		// First collision
		t.put(PRIME_SMALL, "d");
		assertEquals(
				"{\n0 > (0, a)(31, d)\n1 > (1, b)\n30 > (30, c)\n}", 
				t.printContents());
		
	}
	
}
