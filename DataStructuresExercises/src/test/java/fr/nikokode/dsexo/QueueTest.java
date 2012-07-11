package fr.nikokode.dsexo;

import junit.framework.TestCase;

public class QueueTest extends TestCase {

	public QueueTest(String name) {
		super(name);
	}
	
	public final void testOperations() {
		Queue q = new Queue(10);		
		assertEquals(
				"{[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1], head=0, length=0}", 
				q.toString());
		assertTrue(q.isEmpty());
		assertEquals(0, q.getLength());
		
		try {
			q.dequeue();
			fail("Underflow not detected!");
		} catch (final UnderflowException e) {
		}
		
		q.enqueue(0);
		assertEquals(
				"{[0, -1, -1, -1, -1, -1, -1, -1, -1, -1], head=0, length=1}", 
				q.toString());
		assertFalse(q.isEmpty());
		assertEquals(1, q.getLength());
		
		assertEquals(0, q.dequeue());
		assertEquals(
				"{[0, -1, -1, -1, -1, -1, -1, -1, -1, -1], head=1, length=0}", 
				q.toString());
		assertTrue(q.isEmpty());
		assertEquals(0, q.getLength());
		
		for (int i = 1; i < 6; i++) {
			q.enqueue(i);
		}
		assertEquals(
				"{[0, 1, 2, 3, 4, 5, -1, -1, -1, -1], head=1, length=5}", 
				q.toString());
		assertFalse(q.isEmpty());
		assertEquals(5, q.getLength());
		
		for (int i = 6; i < 11; i++) {
			q.enqueue(i);
		}
		assertEquals(
				"{[10, 1, 2, 3, 4, 5, 6, 7, 8, 9], head=1, length=10}", 
				q.toString());
		assertFalse(q.isEmpty());
		assertEquals(10, q.getLength());
		
		try {
			q.enqueue(11);
			fail("Overflow not detected!");
		} catch (final OverflowException e) {
		}
		
		for (int i = 1; i < 11; i++) {
			assertEquals(i, q.dequeue());
		}		
		assertEquals(
				"{[10, 1, 2, 3, 4, 5, 6, 7, 8, 9], head=1, length=0}", 
				q.toString());
		assertTrue(q.isEmpty());
		assertEquals(0, q.getLength());
		
		for (int i = 5; i > 0; i--) {
			q.enqueue(i);
		}
		assertEquals(
				"{[10, 5, 4, 3, 2, 1, 6, 7, 8, 9], head=1, length=5}", 
				q.toString());
		assertFalse(q.isEmpty());
		assertEquals(5, q.getLength());
		
	}

}
