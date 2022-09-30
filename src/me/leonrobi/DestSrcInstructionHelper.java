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
					if (type == Type.MOV) {
						l = Parser.parseLong(source);
						if (l <= 0xFFFFFFFFL) {
							i = Parser.parseInt(source);
							register = Register.get32Bit(register);
						}
					} else {
						i = Parser.parseInt(source);
					}
				}
			} catch (NumberFormatException nfe) {
				throw new SyntaxException("Failed to parse register/value for '" + source + "' - " + nfe.getMessage());
			}
		}

		if (register == null)
			throw new RuntimeException("64-bit register --> 32-bit register is NULL.");

		List<Byte> bytes = new ArrayList<>();

		if (sourceRegister == null) {
			List<Byte> _bytes = null;
			boolean doSpecialByteOperation = false;
			if (shorterByteOperation) {
				if (register.sizeBits() == 16)
					doSpecialByteOperation = s <= 127 && s >= -127;
				else if (register.sizeBits() == 32 || register.sizeBits() == 64)
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

			List<Byte> __bytes = new ArrayList<>();

			byte[] bytesPrefix = Register.getBytePrefix(register);

			if (bytesPrefix != null) {
				for (byte bi : bytesPrefix)
					__bytes.add(bi);
			}

			__bytes.addAll(bytes);

			if (register.sizeBits() == 8) {
				__bytes.add(b);
				bytes = __bytes;
			} else if (register.sizeBits() == 16) {
				if (doSpecialByteOperation) {
					__bytes.add((byte) s);
					bytes = __bytes;
				} else {
					bytes = Parser.addShortToByteList(s, __bytes);
				}
			} else if (register.sizeBits() == 32) {
				if (doSpecialByteOperation) {
					__bytes.add((byte) i);
					bytes = __bytes;
				} else {
					bytes = Parser.addIntToByteList(i, __bytes);
				}
			} else if (register.sizeBits() == 64) {
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
			byte[] bytesPrefix = Register.getBytePrefix(register);

			if (bytesPrefix != null) {
				for (byte bi : bytesPrefix)
					bytes.add(bi);
			}

			switch (type) {
				case MOV -> bytes.add(register.sizeBits() == 8 ? (byte) 0x88 : (byte) 0x89);
				/*case CMP -> _bytes = register.getCmpRegFromValBytes(doSpecialByteOperation);
				case ADD -> _bytes = register.getAddRegFromValBytes(doSpecialByteOperation, register);*/
			}

			bytes.add(Parser.encodeModRm(sourceRegister, register));
		}

		return bytes;
	}

}
