package fr.nikokode.dsexo.hash;

public class MultiplicationHash extends HashFunction<Integer> {
	
	public static final double A = (Math.sqrt(5) - 1) / 2;
	
	private final int bucketCount;
	
	public MultiplicationHash(int bucketCount) {
		super();
		this.bucketCount = bucketCount;
	}

	@Override
	public int hash(Integer key) {
		return new Double(
				Math.rint(bucketCount * (key.intValue() * A % 1))).intValue();
	}
	
}
