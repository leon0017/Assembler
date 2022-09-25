package me.leonrobi;

import java.util.List;

public enum Register {
	AL(8, (byte)0xb0, (byte)0x3c),
	BL(8, (byte)0xb3, List.of((byte)0x80, (byte)0xfb)),
	CL(8, (byte)0xb1, List.of((byte)0x80, (byte)0xf9)),
	DL(8, (byte)0xb2, List.of((byte)0x80, (byte)0xfa)),
	AH(8, (byte)0xb4, List.of((byte)0x80, (byte)0xfc)),
	BH(8, (byte)0xb7, List.of((byte)0x80, (byte)0xff)),
	CH(8, (byte)0xb5, List.of((byte)0x80, (byte)0xfd)),
	DH(8, (byte)0xb6, List.of((byte)0x80, (byte)0xfe)),
	SP(16, (byte)0xbc, List.of((byte)0x83, (byte)0xfc)),
	AX(16, (byte)0xb8, (byte)0x3d),
	BX(16, (byte)0xbb, List.of((byte)0x81, (byte)0xfb)),
	CX(16, (byte)0xb9, List.of((byte)0x81, (byte)0xf9)),
	DX(16, (byte)0xba, List.of((byte)0x81, (byte)0xfa)),
	BP(16, (byte)0xbd, List.of((byte)0x81, (byte)0xfd)),
	SI(16, (byte)0xbe, List.of((byte)0x81, (byte)0xfe)),
	DI(16, (byte)0xbf, List.of((byte)0x81, (byte)0xff));

	private final int sizeBits;
	private final List<Byte> movRegFromValBytes;
	private final List<Byte> cmpRegFromValBytes;

	Register(int sizeBits, byte movRegFromValBytes, byte cmpRegFromValBytes) {
		this.sizeBits = sizeBits;
		this.movRegFromValBytes = List.of(movRegFromValBytes);
		this.cmpRegFromValBytes = List.of(cmpRegFromValBytes);
	}

	Register(int sizeBits, List<Byte> movRegFromValBytes, List<Byte> cmpRegFromValBytes) {
		this.sizeBits = sizeBits;
		this.movRegFromValBytes = movRegFromValBytes;
		this.cmpRegFromValBytes = cmpRegFromValBytes;
	}

	Register(int sizeBits, byte movRegFromValBytes, List<Byte> cmpRegFromValBytes) {
		this.sizeBits = sizeBits;
		this.movRegFromValBytes = List.of(movRegFromValBytes);
		this.cmpRegFromValBytes = cmpRegFromValBytes;
	}

	public int sizeBits() {
		return this.sizeBits;
	}

	public List<Byte> getMovRegFromValBytes() {
		return this.movRegFromValBytes;
	}

	public List<Byte> getCmpRegFromValBytes() {
		return this.cmpRegFromValBytes;
	}
}
