package me.leonrobi;

public enum Register {
	AL(8),
	BL(8),
	CL(8),
	DL(8),
	AH(8),
	BH(8),
	CH(8),
	DH(8);

	private final int sizeBits;

	Register(int sizeBits) {
		this.sizeBits = sizeBits;
	}

	public int sizeBits() {
		return this.sizeBits;
	}
}
