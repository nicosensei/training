package fr.nikokode.dsexo;

import org.junit.Test;

import junit.framework.TestCase;

public class StackTest extends TestCase {

	public StackTest(String name) {
		super(name);
	}

	/**
	 * @param args
	 */
	@Test
	public final void testOperations() {
		Stack s = new Stack(10);
		assertEquals("{[null, null, null, null, null, null, null, null, null, null], top=-1}",
				s.toString());
		assertTrue(s.isEmpty());
		assertEquals(0, s.getHeight());

		try {
			s.pop();
			fail("Underflow not detected!");
		} catch (final UnderflowException e) {
		}

		s.push(0);
		assertEquals("{[0, null, null, null, null, null, null, null, null, null], top=0}",
				s.toString());
		assertFalse(s.isEmpty());
		assertEquals(1, s.getHeight());

		assertEquals(0, s.pop());
		assertEquals("{[0, null, null, null, null, null, null, null, null, null], top=-1}",
				s.toString());
		assertTrue(s.isEmpty());
		assertEquals(0, s.getHeight());

		for (int i = 1; i < 6; i++) {
			s.push(i);
		}
		assertEquals("{[1, 2, 3, 4, 5, null, null, null, null, null], top=4}",
				s.toString());
		assertFalse(s.isEmpty());
		assertEquals(5, s.getHeight());

		for (int i = 6; i < 11; i++) {
			s.push(i);
		}
		assertEquals("{[1, 2, 3, 4, 5, 6, 7, 8, 9, 10], top=9}", s.toString());
		assertFalse(s.isEmpty());
		assertEquals(10, s.getHeight());

		try {
			s.push(11);
			fail("Overflow not detected!");
		} catch (final OverflowException e) {
		}

		for (int i = 10; i > 0; i--) {
			assertEquals(i, s.pop());
		}
		assertEquals("{[1, 2, 3, 4, 5, 6, 7, 8, 9, 10], top=-1}", s.toString());
		assertTrue(s.isEmpty());
		assertEquals(0, s.getHeight());
	}

}
