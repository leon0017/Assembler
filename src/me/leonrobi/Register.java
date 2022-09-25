package me.leonrobi;

import java.util.ArrayList;
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
	ESP(32, 4, (byte)0xbc, List.of((byte)0x81, (byte)0xfc)),
	RAX(64, 0, (byte)0xb8, (byte)0x3d),
	RBX(64, 3, (byte)0xbb, List.of((byte)0x81, (byte)0xfb)),
	RCX(64, 1, (byte)0xb9, List.of((byte)0x81, (byte)0xf9)),
	RDX(64, 2, (byte)0xba, List.of((byte)0x81, (byte)0xfa)),
	RBP(64, 5, (byte)0xbd, List.of((byte)0x81, (byte)0xfd)),
	RSI(64, 6, (byte)0xbe, List.of((byte)0x81, (byte)0xfe)),
	RDI(64, 7, (byte)0xbf, List.of((byte)0x81, (byte)0xff)),
	RSP(64, 4, (byte)0xbc, List.of((byte)0x81, (byte)0xfc));

	private final int sizeBits;
	private final List<Byte> movRegFromValBytes;
	private final List<Byte> cmpRegFromValBytes;
	private final int offset;

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

	public List<Byte> getCmpRegFromValBytes(boolean doSpecialByteOperation) {
		if (!doSpecialByteOperation)
			return this.cmpRegFromValBytes;
		List<Byte> bytes = new ArrayList<>();
		bytes.add((byte)0x83);
		bytes.add((byte)(0xf8 + offset));
		return bytes;
	}

	public List<Byte> getAddRegFromValBytes(boolean doSpecialByteOperation, Register register) {
		List<Byte> bytes = new ArrayList<>();
		if (doSpecialByteOperation) {
			bytes.add((byte)0x83);
			bytes.add((byte)(0xc0 + offset));
			return bytes;
		}
		if (register == Register.AL || register == Register.AX || register == Register.EAX) {
			if (register == Register.AL)
				bytes.add((byte)0x04);
			else
				bytes.add((byte)0x05);
			return bytes;
		}
		if (sizeBits == 8)
			bytes.add((byte)0x80);
		else
			bytes.add((byte)0x81);
		bytes.add((byte)(0xc0 + offset));
		return bytes;
	}

	public int getOffset() {
		return this.offset;
	}
}
