package me.leonrobi;

import java.util.ArrayList;
import java.util.List;

public enum Register {
	AL(8, 0, (byte)0xb0, (byte)0x3c, false),
	BL(8, 3, (byte)0xb3, List.of((byte)0x80, (byte)0xfb), false),
	CL(8, 1, (byte)0xb1, List.of((byte)0x80, (byte)0xf9), false),
	DL(8, 2, (byte)0xb2, List.of((byte)0x80, (byte)0xfa), false),
	AH(8, 4, (byte)0xb4, List.of((byte)0x80, (byte)0xfc), false),
	BH(8, 7, (byte)0xb7, List.of((byte)0x80, (byte)0xff), false),
	CH(8, 5, (byte)0xb5, List.of((byte)0x80, (byte)0xfd), false),
	DH(8, 6, (byte)0xb6, List.of((byte)0x80, (byte)0xfe), false),
	SP(16, 4, (byte)0xbc, List.of((byte)0x81, (byte)0xfc), false),
	AX(16, 0, (byte)0xb8, (byte)0x3d, false),
	BX(16, 3, (byte)0xbb, List.of((byte)0x81, (byte)0xfb), false),
	CX(16, 1, (byte)0xb9, List.of((byte)0x81, (byte)0xf9), false),
	DX(16, 2, (byte)0xba, List.of((byte)0x81, (byte)0xfa), false),
	BP(16, 5, (byte)0xbd, List.of((byte)0x81, (byte)0xfd), false),
	SI(16, 6, (byte)0xbe, List.of((byte)0x81, (byte)0xfe), false),
	DI(16, 7, (byte)0xbf, List.of((byte)0x81, (byte)0xff), false),
	EAX(32, 0, (byte)0xb8, (byte)0x3d, false),
	EBX(32, 3, (byte)0xbb, List.of((byte)0x81, (byte)0xfb), false),
	ECX(32, 1, (byte)0xb9, List.of((byte)0x81, (byte)0xf9), false),
	EDX(32, 2, (byte)0xba, List.of((byte)0x81, (byte)0xfa), false),
	EBP(32, 5, (byte)0xbd, List.of((byte)0x81, (byte)0xfd), false),
	ESI(32, 6, (byte)0xbe, List.of((byte)0x81, (byte)0xfe), false),
	EDI(32, 7, (byte)0xbf, List.of((byte)0x81, (byte)0xff), false),
	ESP(32, 4, (byte)0xbc, List.of((byte)0x81, (byte)0xfc), false),
	RAX(64, 0, (byte)0xb8, (byte)0x3d, false),
	RBX(64, 3, (byte)0xbb, List.of((byte)0x81, (byte)0xfb), false),
	RCX(64, 1, (byte)0xb9, List.of((byte)0x81, (byte)0xf9), false),
	RDX(64, 2, (byte)0xba, List.of((byte)0x81, (byte)0xfa), false),
	RBP(64, 5, (byte)0xbd, List.of((byte)0x81, (byte)0xfd), false),
	RSI(64, 6, (byte)0xbe, List.of((byte)0x81, (byte)0xfe), false),
	RDI(64, 7, (byte)0xbf, List.of((byte)0x81, (byte)0xff), false),
	RSP(64, 4, (byte)0xbc, List.of((byte)0x81, (byte)0xfc), false),
	R8B(8, 0, (byte)0xb0, List.of((byte)0x80, (byte)0xf8), true),
	R9B(8, 1, (byte)0xb1, List.of((byte)0x80, (byte)0xf9), true),
	R10B(8, 2, (byte)0xb2, List.of((byte)0x80, (byte)0xfa), true),
	R11B(8, 3, (byte)0xb3, List.of((byte)0x80, (byte)0xfb), true),
	R12B(8, 4, (byte)0xb4, List.of((byte)0x80, (byte)0xfc), true),
	R13B(8, 5, (byte)0xb5, List.of((byte)0x80, (byte)0xfd), true),
	R14B(8, 6, (byte)0xb6, List.of((byte)0x80, (byte)0xfe), true),
	R15B(8, 7, (byte)0xb7, List.of((byte)0x80, (byte)0xff), true),
	R8W(16, 0, (byte)0xb8, List.of((byte)0x80, (byte)0xf8), true),
	R9W(16, 1, (byte)0xb9, List.of((byte)0x80, (byte)0xf9), true),
	R10W(16, 2, (byte)0xba, List.of((byte)0x80, (byte)0xfa), true),
	R11W(16, 3, (byte)0xbb, List.of((byte)0x80, (byte)0xfb), true),
	R12W(16, 4, (byte)0xbc, List.of((byte)0x80, (byte)0xfc), true),
	R13W(16, 5, (byte)0xbd, List.of((byte)0x80, (byte)0xfd), true),
	R14W(16, 6, (byte)0xbe, List.of((byte)0x80, (byte)0xfe), true),
	R15W(16, 7, (byte)0xbf, List.of((byte)0x80, (byte)0xff), true),
	R8D(32, 0, (byte)0xb8, List.of((byte)0x80, (byte)0xf8), true),
	R9D(32, 1, (byte)0xb9, List.of((byte)0x80, (byte)0xf9), true),
	R10D(32, 2, (byte)0xba, List.of((byte)0x80, (byte)0xfa), true),
	R11D(32, 3, (byte)0xbb, List.of((byte)0x80, (byte)0xfb), true),
	R12D(32, 4, (byte)0xbc, List.of((byte)0x80, (byte)0xfc), true),
	R13D(32, 5, (byte)0xbd, List.of((byte)0x80, (byte)0xfd), true),
	R14D(32, 6, (byte)0xbe, List.of((byte)0x80, (byte)0xfe), true),
	R15D(32, 7, (byte)0xbf, List.of((byte)0x80, (byte)0xff), true),;

	private final int sizeBits;
	private final List<Byte> movRegFromValBytes;
	private final List<Byte> cmpRegFromValBytes;
	private final int offset;
	private final boolean isRRegister;

	Register(int sizeBits, int offset, byte movRegFromValBytes, byte cmpRegFromValBytes, boolean isRRegister) {
		this.sizeBits = sizeBits;
		this.offset = offset;
		this.movRegFromValBytes = List.of(movRegFromValBytes);
		this.cmpRegFromValBytes = List.of(cmpRegFromValBytes);
		this.isRRegister = isRRegister;
	}

	Register(int sizeBits, int offset, List<Byte> movRegFromValBytes, List<Byte> cmpRegFromValBytes, boolean isRRegister) {
		this.sizeBits = sizeBits;
		this.offset = offset;
		this.movRegFromValBytes = movRegFromValBytes;
		this.cmpRegFromValBytes = cmpRegFromValBytes;
		this.isRRegister = isRRegister;
	}

	Register(int sizeBits, int offset, byte movRegFromValBytes, List<Byte> cmpRegFromValBytes, boolean isRRegister) {
		this.sizeBits = sizeBits;
		this.offset = offset;
		this.movRegFromValBytes = List.of(movRegFromValBytes);
		this.cmpRegFromValBytes = cmpRegFromValBytes;
		this.isRRegister = isRRegister;
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
		if (register == Register.AL || register == Register.AX || register == Register.EAX || register == Register.RAX) {
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

	public boolean isRRegister() {
		return this.isRRegister;
	}

	public int getOffset() {
		return this.offset;
	}

	public static byte[] getBytePrefix(Register register) throws SyntaxException {
		if (register.sizeBits() == 16 && Parser.bits == 32)
			return new byte[]{0x66};
		if (register.sizeBits() == 32 && Parser.bits == 16)
			return new byte[]{0x66};
		if (register.sizeBits() == 64 && Parser.bits != 64)
			throw new SyntaxException("64-bit registers cannot be used on 16-bit or 32-bit mode.");
		if (register.isRRegister() && Parser.bits != 64)
			throw new SyntaxException("Cannot use register '" + register + "' on 16-bit or 32-bit mode.");
		if (register.sizeBits() == 64 && !register.isRRegister())
			return new byte[]{0x48};
		if (register.sizeBits() != 64 && register.sizeBits() != 16 && register.isRRegister())
			return new byte[]{0x41};
		if (register.sizeBits() == 64)
			return new byte[]{0x49};
		if (register.sizeBits() == 16 && register.isRRegister())
			return new byte[]{0x66, 0x41};
		if (register.sizeBits() == 16 && Parser.bits == 64)
			return new byte[]{0x66};
		return null;
	}

	public static Register get32Bit(Register register) {
		if (register.sizeBits() != 64)
			return null;
		if (register.isRRegister())
			return Register.valueOf(register + "D");
		else
			return Register.valueOf("E" + register.toString().substring(1));
	}
}
