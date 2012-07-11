package fr.nikokode.dsexo.hash;

public class ModuloHash extends HashFunction<Integer> {

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
