package fr.nikokode.dsexo;

import junit.framework.TestCase;

import org.junit.Test;

import fr.nikokode.dsexo.hash.HashTable;
import fr.nikokode.dsexo.hash.ModuloHash;
import fr.nikokode.dsexo.hash.MultiplicationHash;

public class HashTableTest extends TestCase {
	
	class ModuloHashTable extends HashTable<Integer, String> {
		
		ModuloHashTable() {
			super(PRIME_SMALL, 100, new ModuloHash(PRIME_SMALL));
		}
		
	}
	
	class MultiplicationHashTable extends HashTable<Integer, String> {
		
		MultiplicationHashTable() {
			super(PRIME_SMALL, 100, new MultiplicationHash(PRIME_SMALL));
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
		assertEquals("{\n0 > [(0, a)]\n}", t.printContents());
		
		t.put(1, "b");
		assertEquals("{\n0 > [(0, a)]\n1 > [(1, b)]\n}", t.printContents());
		
		t.put(PRIME_SMALL - 1, "c");
		assertEquals(
				"{\n0 > [(0, a)]\n1 > [(1, b)]\n30 > [(30, c)]\n}", 
				t.printContents());
		
		// First collision
		t.put(PRIME_SMALL, "d");
		assertEquals(
				"{\n0 > [(0, a), (31, d)]\n1 > [(1, b)]\n30 > [(30, c)]\n}", 
				t.printContents());
		
	}
	
	@Test
	public final void testMultiplicationHash() {
		MultiplicationHashTable t = new MultiplicationHashTable();
		
		t.put(0, "a");
		assertEquals("{\n0 > [(0, a)]\n}", t.printContents());
		
		t.put(1, "b");
		assertEquals("{\n0 > [(0, a)]\n19 > [(1, b)]\n}", t.printContents());
		
		t.put(PRIME_SMALL - 1, "c");
		assertEquals(
				"{\n0 > [(0, a)]\n17 > [(30, c)]\n19 > [(1, b)]\n}", 
				t.printContents());
		
		// First collision
		t.put(PRIME_SMALL, "d");
		assertEquals(
				"{\n0 > [(0, a)]\n5 > [(31, d)]\n17 > [(30, c)]\n19 > [(1, b)]\n}", 
				t.printContents());
		
	}
	
}
