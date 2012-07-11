package fr.nikokode.dsexo;

import java.util.Arrays;

import org.junit.Test;

import junit.framework.TestCase;

public class DoubleChainedListTest extends TestCase {

	public DoubleChainedListTest(String name) {
		super(name);
	}
	
	@Test
	public final void testOperations() {
		// 5 is enough to get a headache ;-)
		DoubleChainedList l = new DoubleChainedList(5);
		
		assertTrue(l.isEmpty());
		assertEquals(0, l.getLength());
		assertEquals("[]", printTraversal(l, false));
		assertEquals("[]", printTraversal(l, true));
		assertEquals(
				"{(0, null, 0)(0, null, 0)(0, null, 0)(0, null, 0)(0, null, 0)"
				+ ", length=0, first=-1, last=-1}", 
				l.toString());
		
		l.addFirst("a");
		assertFalse(l.isEmpty());
		assertEquals(1, l.getLength());
		assertEquals("[a]", printTraversal(l, false));
		assertEquals("[a]", printTraversal(l, true));
		assertEquals(
				"{(-1, a, -1)(0, null, 0)(0, null, 0)(0, null, 0)(0, null, 0)"
				+ ", length=1, first=0, last=0}", 
				l.toString());
		
		l.addLast("b");
		assertFalse(l.isEmpty());
		assertEquals(2, l.getLength());
		assertEquals("[a, b]", printTraversal(l, false));
		assertEquals("[b, a]", printTraversal(l, true));
		assertEquals(
				"{(-1, a, 1)(0, b, -1)(0, null, 0)(0, null, 0)(0, null, 0)"
				+ ", length=2, first=0, last=1}", 
				l.toString());
		
		l.addFirst("c");
		assertFalse(l.isEmpty());
		assertEquals(3, l.getLength());
		assertEquals("[c, a, b]", printTraversal(l, false));
		assertEquals("[b, a, c]", printTraversal(l, true));
		assertEquals(
				"{(2, a, 1)(0, b, -1)(-1, c, 0)(0, null, 0)(0, null, 0)"
				+ ", length=3, first=2, last=1}", 
				l.toString());
		
		l.add(1, "d");
		assertFalse(l.isEmpty());
		assertEquals(4, l.getLength());
		assertEquals("[c, d, a, b]", printTraversal(l, false));
		assertEquals("[b, a, d, c]", printTraversal(l, true));
		assertEquals(
				"{(3, a, 1)(0, b, -1)(-1, c, 3)(2, d, 0)(0, null, 0)"
				+ ", length=4, first=2, last=1}", 
				l.toString());
		
		assertEquals(2, l.seekOffset(0));
		assertEquals(3, l.seekOffset(1));
		assertEquals(0, l.seekOffset(2));
		assertEquals(1, l.seekOffset(3));
		
		assertEquals("a", l.remove(2));
		assertFalse(l.isEmpty());
		assertEquals(3, l.getLength());
		assertEquals("[c, d, b]", printTraversal(l, false));
		assertEquals("[b, d, c]", printTraversal(l, true));
		assertEquals(
				"{(3, a, 1)(3, b, -1)(-1, c, 3)(2, d, 1)(0, null, 0)"
				+ ", length=3, first=2, last=1}", 
				l.toString());
		
		assertEquals("c", l.removeFirst());
		assertFalse(l.isEmpty());
		assertEquals(2, l.getLength());
		assertEquals("[d, b]", printTraversal(l, false));
		assertEquals("[b, d]", printTraversal(l, true));
		assertEquals(
				"{(3, a, 1)(3, b, -1)(-1, c, 3)(-1, d, 1)(0, null, 0)"
				+ ", length=2, first=3, last=1}", 
				l.toString());
		
		assertEquals("b", l.removeLast());
		assertFalse(l.isEmpty());
		assertEquals(1, l.getLength());
		assertEquals("[d]", printTraversal(l, false));
		assertEquals("[d]", printTraversal(l, true));
		assertEquals(
				"{(3, a, 1)(3, b, -1)(-1, c, 3)(-1, d, -1)(0, null, 0)"
				+ ", length=1, first=3, last=3}", 
				l.toString());
		
		l.addLast("e");
		assertFalse(l.isEmpty());
		assertEquals(2, l.getLength());
		assertEquals("[d, e]", printTraversal(l, false));
		assertEquals("[e, d]", printTraversal(l, true));
		assertEquals(
				"{(3, a, 1)(3, e, -1)(-1, c, 3)(-1, d, 1)(0, null, 0)"
				+ ", length=2, first=3, last=1}", 
				l.toString());
		
		assertEquals("d", l.removeFirst());
		assertEquals("e", l.removeFirst());
		assertTrue(l.isEmpty());
		assertEquals(0, l.getLength());
		assertEquals("[]", printTraversal(l, false));
		assertEquals("[]", printTraversal(l, true));
		assertEquals(
				"{(3, a, 1)(-1, e, -1)(-1, c, 3)(-1, d, 1)(0, null, 0)"
				+ ", length=0, first=-1, last=-1}", 
				l.toString());
		
		try {
			l.add(2, "a");
			fail("Index out of bounds not detected!");
		} catch (final ArrayIndexOutOfBoundsException e) {
			
		}
		
		l.addLast("f");
		assertFalse(l.isEmpty());
		assertEquals(1, l.getLength());
		assertEquals("[f]", printTraversal(l, false));
		assertEquals("[f]", printTraversal(l, true));
		assertEquals(
				"{(3, a, 1)(-1, f, -1)(-1, c, 3)(-1, d, 1)(0, null, 0)"
				+ ", length=1, first=1, last=1}", 
				l.toString());
		
		assertEquals("f", l.removeLast());
		assertTrue(l.isEmpty());
		assertEquals(0, l.getLength());
		assertEquals("[]", printTraversal(l, false));
		assertEquals("[]", printTraversal(l, true));
		assertEquals(
				"{(3, a, 1)(-1, f, -1)(-1, c, 3)(-1, d, 1)(0, null, 0)"
				+ ", length=0, first=-1, last=-1}", 
				l.toString());
		
		String[] letters = new String[] { "A", "B", "C", "D", "E" };
		for (int i = 0; i < 5; i++) {
			l.addLast(letters[i]);
		}
		assertFalse(l.isEmpty());
		assertEquals(5, l.getLength());
		assertEquals("[A, B, C, D, E]", printTraversal(l, false));
		assertEquals("[E, D, C, B, A]", printTraversal(l, true));
		assertEquals(
				"{(2, D, 4)(-1, A, 3)(3, C, 0)(1, B, 2)(0, E, -1)"
				+ ", length=5, first=1, last=4}", 
				l.toString());
		
		try {
			l.addLast("F");
			fail("Overflow not detected!");
		} catch (final OverflowException e) {
		}
		
		l.replaceAt(1, "F");
		assertFalse(l.isEmpty());
		assertEquals(5, l.getLength());
		assertEquals("[A, F, C, D, E]", printTraversal(l, false));
		assertEquals("[E, D, C, F, A]", printTraversal(l, true));
		assertEquals(
				"{(2, D, 4)(-1, A, 3)(3, C, 0)(1, F, 2)(0, E, -1)"
				+ ", length=5, first=1, last=4}", 
				l.toString());
		
	}

	private String printTraversal(DoubleChainedList l, boolean reverse) {
		return Arrays.toString(l.toArray(reverse));
	}
}
