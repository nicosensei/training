package fr.nikokode.dsexo;

import java.util.Arrays;

/**
 * A stack of non-null objects.
 * 
 * @author nicolas
 * 
 */
public class Stack {

	/**
	 * A fixed-size array storing values.
	 */
	private final Object[] stack;

	/**
	 * Stack top index.
	 */
	private int top;

	public Stack(int maxSize) {
		stack = new Object[maxSize];
		top = -1;
	}

	public void push(Object value) {
		assert value != null;
		if (top == stack.length - 1) {
			throw new OverflowException();
		}
		top++;
		stack[top] = value;
	}

	public Object pop() {
		if (top < 0) {
			throw new UnderflowException();
		}
		Object topValue = stack[top];
		top--;
		return topValue;
	}

	public boolean isEmpty() {
		return top == -1;
	}

	public int getHeight() {
		return top + 1;
	}

	@Override
	public String toString() {
		return "{" + Arrays.toString(stack) + ", top=" + top + "}";
	}

}
