package fr.nikokode.dsexo;




/**
 * Double chained list implementation using arrays.
 * 
 * We use 3 arrays to store the data and the next/previous pointers. 
 * Additionally we use a stack to keep track of the next free slot. This has 
 * the drawback of increasing memory usage in O(n) but allows to guarantee the
 * O(1) seek time for a free slot.
 * 
 * We store the actual list length instead of using a sentinel, and the list is 
 * not circular (e.g. the last element has no successor).
 * 
 * Also the list length cannot increase past a defined bound.
 * 
 * @author nicolas
 *
 */
public class DoubleChainedList {
	
	public static final int NIL = -1;
	
	private int maxSize;
	
	private Object[] values;
	private int[] previous;
	private int[] next;
	
	private Stack freeSlots;	
	
	private int length;
	
	/**
	 * First element offset
	 */
	private int first;
	
	/**
	 * Last element offset
	 */
	private int last;

	public DoubleChainedList(int maxSize) {
		values = new Object[maxSize];
		previous = new int[maxSize];
		next = new int[maxSize];
		
		freeSlots = new Stack(maxSize);
		for (int i = maxSize - 1; i >= 0; i--) {
			freeSlots.push(i);
		}
		
		this.length = 0;
		this.maxSize = maxSize;
		first = NIL;
		last = NIL;
	}
	
	// Running time is O(1)
	public void addLast(Object value) {
		checkOverflow();
		
		if (length == 0) {
			insertFirstElement(value);
			return;
		}
		
		int index = ((Integer) freeSlots.pop()).intValue();
		values[index] = value;
		previous[index] = last; // predecessor
		next[index] =  NIL; // no successor
		
		// Update predecessor element
		next[last] = index;
		
		last = index;
		length++;
	}
	
	// Running time is O(1)
	public void addFirst(Object value) {
		
		checkOverflow();
		
		if (length == 0) {
			insertFirstElement(value);
			return;
		}

		int index = seekFreeOffset();
		values[index] = value;
		previous[index] = NIL; // no predecessor
		next[index] =  first; // successor
		
		previous[first] = index;
		
		first = index;		
		length++;
	}
	
	// The element at the current index is shifted to the right
	// Running time is O(n)
	public void add(int index, Object value) {
		checkOverflow();
		
		if (index > length -1) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		
		int indexOffset = seekOffset(index);
		
		if (indexOffset == first) {
			addFirst(value);
			return;
		}
		
		if (indexOffset == last) {
			addLast(value);
			return;
		}
		
		if (length == 0) {
			insertFirstElement(value);
			return;
		}

		int insertOffset = seekFreeOffset();
		
		// Update the chaining (shift current to the right)
		int indexPredecessorOffset = previous[indexOffset];
		next[indexPredecessorOffset] = insertOffset;
		previous[indexOffset] = insertOffset;				
		
		// Insert new element with correct chaining
		previous[insertOffset] = indexPredecessorOffset; // predecessor
		values[insertOffset] = value;
		next[insertOffset] =  indexOffset; // successor
		
		length++;
	}
	
	private void checkOverflow() {
		if (length == maxSize) {
			throw new OverflowException();
		}
	}
	
	public int getLength() {
		return length;
	}
	
	public boolean isEmpty() {
		return length == 0;
	}
	
	public Object removeFirst() {
		return removeAtOffset(first);		
	}
	
	public Object removeLast() {
		return removeAtOffset(last);
	}
	
	public Object remove(int index) {
		assert index >= 0;
		return removeAtOffset(seekOffset(index));
	}
	
	private Object removeAtOffset(int offset) {
		
		if (length == 0) {
			throw new RuntimeException("Empty!");
		}

		Object value = values[offset];
		
		if (length == 1) {
			// removal of last element
			first = NIL;
			last = NIL;
		} else if (offset == first) {
			first = next[first];
			previous[first] = NIL;
		} else if (offset == last) {
			last = previous[last];
			if (last != NIL) { // happens when only 1 element remains
				next[last] = NIL;
			}
		} else {
			int predecessorIndex = previous[offset];
			int successorIndex = next[offset];
			next[predecessorIndex] = successorIndex;
			previous[successorIndex] = predecessorIndex;
		}
		
		freeSlots.push(offset); // free slot
		
		length--;
		return value;
	}

	private void insertFirstElement(Object value) {
		assert length == 0;
		
		int index = seekFreeOffset();
		values[index] = value;
		previous[index] = NIL; // no predecessor
		next[index] =  NIL; // no successor
		length = 1;
		first = index;
		last = index;
	}

	@Override
	public String toString() {
		String str = "{";
		for (int i = 0; i < maxSize; i++) {
			str += "(" + previous[i] + ", "
					+ values[i] + ", "
					+ next[i] + ")";
			 
		}
				
		return str + ", length=" + length + ", first=" + first 
				+ ", last=" + last + "}";
	}
	
	public Object[] toArray(boolean reverse) {
		if (length == 0) {
			return new Object[0];
		}
		Object[] array = new Object[length];
		int offset = reverse ? last : first;
		int arrayIndex = 0;
		
		while (offset != NIL) {
			array[arrayIndex] = values[offset];
			arrayIndex++;
			offset = reverse ? previous[offset] : next[offset];
		}
		
		return array;
	}
	
	protected int seekOffset(int index) {
		// Seek the element at index (this is N/2 worst-case)
		int indexOffset;
		if (index <= length /2) {
			indexOffset = first;
			for (int i = 0; i < index; i++) {
				indexOffset = next[indexOffset];
			}
		} else {
			indexOffset = last;
			for (int i = length - 1; i > index; i--) {
				indexOffset = previous[indexOffset];
			}
		}
		
		return indexOffset;
	}
	
	public void replaceAt(int index, Object value) {
		values[seekOffset(index)] = value;
	}
	
	private int seekFreeOffset() {
		return ((Integer) freeSlots.pop()).intValue();
	}
	
}
