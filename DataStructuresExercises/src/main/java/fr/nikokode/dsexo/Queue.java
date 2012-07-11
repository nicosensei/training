package fr.nikokode.dsexo;

import java.util.Arrays;

/**
 * A fixed-size queue of positive integers.
 * 
 * @author nicolas
 * 
 */
public class Queue {

	/**
	 * Circular array of values.
	 */
	private final int[] queue;

	/**
	 * The queue size.
	 */
	private int length;

	/**
	 * Head index (first enqueud).
	 */
	private int head;

	public Queue(int maxSize) {
		queue = new int[maxSize];
		head = 0;
		length = 0;
		for (int i = 0; i < maxSize; i++) {
			queue[i] = -1;
		}
	}

	public boolean isEmpty() {
		return length == 0;
	}

	public int getLength() {
		return length;
	}

	@Override
	public String toString() {
		return "{" + Arrays.toString(queue)
				+ ", head=" + head 
				+ ", length=" + length + "}";
	}

	public void enqueue(int value) {
		assert value >= 0;
		if (length == queue.length) {
			throw new OverflowException();
		}
		
		int insertIndex = (head + length) % queue.length;
		queue[insertIndex] = value;
		length++;
	}

	public int dequeue() {
		if (length == 0) {
			throw new UnderflowException();
		}
		int value = queue[head];
		head = (head + 1)  % queue.length;
		
		length--;
		return value;
	}

}
