package me.leonrobi;

import java.util.ArrayList;
import java.util.List;

public class DestSrcInstructionHelper {

	public enum Type {
		ADD,
		CMP,
		MOV
	}

	private final String lineContent;
	private final boolean shorterByteOperation;
	private final Type type;

	public DestSrcInstructionHelper(String lineContent, boolean shorterByteOperation, Type type) {
		this.lineContent = lineContent;
		this.shorterByteOperation = shorterByteOperation;
		this.type = type;
	}

	public List<Byte> handle() throws SyntaxException {
		String[] split = lineContent.substring(4).split(",");
		String registerName = split[0];
		Register register;
		try {
			register = Register.valueOf(registerName.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new SyntaxException("Unknown register specified: '" + registerName + "'.");
		}

		String source = split[1].replaceAll(" ", "").replaceAll("\t", "");
		Register sourceRegister = null;
		byte b = 0;
		short s = 0;
		int i = 0;
		long l = 0;
		try {
			sourceRegister = Register.valueOf(source.toUpperCase());
			if (sourceRegister.sizeBits() != register.sizeBits())
				throw new SyntaxException("You cannot operate on registers with different sizes.");
		} catch (IllegalArgumentException e) {
			try {
				if (register.sizeBits() == 8)
					b = Parser.parseByte(source);
				else if (register.sizeBits() == 16)
					s = Parser.parseShort(source);
				else if (register.sizeBits() == 32)
					i = Parser.parseInt(source);
				else if (register.sizeBits() == 64) {
					if (type == Type.MOV)
						l = Parser.parseLong(source);
					else
						i = Parser.parseInt(source);
				}
			} catch (NumberFormatException nfe) {
				throw new SyntaxException("Failed to parse register/value for '" + source + "' - " + nfe.getMessage());
			}
		}

		List<Byte> bytes = new ArrayList<>();

		if (sourceRegister == null) {
			List<Byte> _bytes = null;
			boolean doSpecialByteOperation = false;
			if (shorterByteOperation) {
				if (register.sizeBits() == 16)
					doSpecialByteOperation = s <= 127 && s >= -127;
				else if (register.sizeBits() == 32)
					doSpecialByteOperation = i <= 127 && i >= -127;
			}
			switch (type) {
				case MOV -> _bytes = register.getMovRegFromValBytes();
				case CMP -> _bytes = register.getCmpRegFromValBytes(doSpecialByteOperation);
				case ADD -> _bytes = register.getAddRegFromValBytes(doSpecialByteOperation, register);
			}
			if (_bytes == null)
				throw new SyntaxException("Register '" + register + "' does not support this operation.");
			bytes.addAll(_bytes);
			if (register.sizeBits() == 8) {
				List<Byte> __bytes = new ArrayList<>(bytes);
				__bytes.add(b);
				bytes = __bytes;
			} else if (register.sizeBits() == 16) {
				List<Byte> __bytes = new ArrayList<>();
				if (Parser.bits == 32 || Parser.bits == 64)
					__bytes.add((byte)0x66);
				__bytes.addAll(bytes);
				if (doSpecialByteOperation) {
					__bytes.add((byte) s);
					bytes = __bytes;
				} else {
					bytes = Parser.addShortToByteList(s, __bytes);
				}
			} else if (register.sizeBits() == 32) {
				List<Byte> __bytes = new ArrayList<>();
				if (Parser.bits == 16)
					__bytes.add((byte)0x66);
				__bytes.addAll(bytes);
				if (doSpecialByteOperation) {
					__bytes.add((byte) i);
					bytes = __bytes;
				} else {
					bytes = Parser.addIntToByteList(i, __bytes);
				}
			} else if (register.sizeBits() == 64) {
				List<Byte> __bytes = new ArrayList<>();
				if (Parser.bits != 64)
					throw new SyntaxException("You cannot use 64-bit registers in non 64-bit mode.");
				__bytes.add((byte)0x48);
				__bytes.addAll(bytes);
				if (doSpecialByteOperation) {
					__bytes.add((byte) i);
					bytes = __bytes;
				} else {
					if (type == Type.MOV)
						bytes = Parser.addLongToByteList(l, __bytes);
					else
						bytes = Parser.addIntToByteList(i, __bytes);
				}
			}
		} else {
			throw new SyntaxException("Having a register as a source operand is not yet supported.");
		}

		return bytes;
	}

}
