package me.leonrobi;

import java.util.List;

public enum Register {
	AL(8, 0, (byte)0xb0, (byte)0x3c),
	BL(8, 3, (byte)0xb3, List.of((byte)0x80, (byte)0xfb)),
	CL(8, 1, (byte)0xb1, List.of((byte)0x80, (byte)0xf9)),
	DL(8, 2, (byte)0xb2, List.of((byte)0x80, (byte)0xfa)),
	AH(8, 4, (byte)0xb4, List.of((byte)0x80, (byte)0xfc)),
	BH(8, 7, (byte)0xb7, List.of((byte)0x80, (byte)0xff)),
	CH(8, 5, (byte)0xb5, List.of((byte)0x80, (byte)0xfd)),
	DH(8, 6, (byte)0xb6, List.of((byte)0x80, (byte)0xfe)),
	SP(16, 4, (byte)0xbc, List.of((byte)0x81, (byte)0xfc)),
	AX(16, 0, (byte)0xb8, (byte)0x3d),
	BX(16, 3, (byte)0xbb, List.of((byte)0x81, (byte)0xfb)),
	CX(16, 1, (byte)0xb9, List.of((byte)0x81, (byte)0xf9)),
	DX(16, 2, (byte)0xba, List.of((byte)0x81, (byte)0xfa)),
	BP(16, 5, (byte)0xbd, List.of((byte)0x81, (byte)0xfd)),
	SI(16, 6, (byte)0xbe, List.of((byte)0x81, (byte)0xfe)),
	DI(16, 7, (byte)0xbf, List.of((byte)0x81, (byte)0xff)),
	EAX(32, 0, (byte)0xb8, (byte)0x3d),
	EBX(32, 3, (byte)0xbb, List.of((byte)0x81, (byte)0xfb)),
	ECX(32, 1, (byte)0xb9, List.of((byte)0x81, (byte)0xf9)),
	EDX(32, 2, (byte)0xba, List.of((byte)0x81, (byte)0xfa)),
	EBP(32, 5, (byte)0xbd, List.of((byte)0x81, (byte)0xfd)),
	ESI(32, 6, (byte)0xbe, List.of((byte)0x81, (byte)0xfe)),
	EDI(32, 7, (byte)0xbf, List.of((byte)0x81, (byte)0xff)),
	ESP(32, 4, (byte)0xbc, List.of((byte)0x81, (byte)0xfc));

	private final int sizeBits;
	private final List<Byte> movRegFromValBytes;
	private final List<Byte> cmpRegFromValBytes;
	private int offset = 0;

	Register(int sizeBits, int offset, byte movRegFromValBytes, byte cmpRegFromValBytes) {
		this.sizeBits = sizeBits;
		this.offset = offset;
		this.movRegFromValBytes = List.of(movRegFromValBytes);
		this.cmpRegFromValBytes = List.of(cmpRegFromValBytes);
	}

	Register(int sizeBits, int offset, List<Byte> movRegFromValBytes, List<Byte> cmpRegFromValBytes) {
		this.sizeBits = sizeBits;
		this.offset = offset;
		this.movRegFromValBytes = movRegFromValBytes;
		this.cmpRegFromValBytes = cmpRegFromValBytes;
	}

	Register(int sizeBits, int offset, byte movRegFromValBytes, List<Byte> cmpRegFromValBytes) {
		this.sizeBits = sizeBits;
		this.offset = offset;
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

	public int getOffset() {
		return this.offset;
	}
}
