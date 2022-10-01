package me.leonrobi;

import java.util.ArrayList;
import java.util.List;

public class DestSrcInstructionHelper {

	public enum Type {
		ADD,
		CMP,
		MOV,
		SUB
	}

	private final String lineContent;
	private final boolean shorterByteOperation;
	private final Type type;
	private final int startingLine;

	public DestSrcInstructionHelper(String lineContent, boolean shorterByteOperation, Type type, int startingLine) {
		this.lineContent = lineContent;
		this.shorterByteOperation = shorterByteOperation;
		this.type = type;
		this.startingLine = startingLine;
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
		Label label;
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
				label = Label.get(source, startingLine);
				if (label == null)
					throw new SyntaxException("Failed to parse register/value/label for '" + source + "' - " + nfe.getMessage());
				if (type != Type.MOV)
					throw new SyntaxException("You cannot do this operation on a label.");
				/* TODO: add ORG offset as well. */
				long labelOffset = label.byteOffset() + Parser.orgOffset;
				System.out.printf("%x%n", labelOffset);
				int bitsRequired = Parser.valueToBits(labelOffset);
				if (bitsRequired > register.sizeBits())
					throw new SyntaxException("Cannot fit label address '" + labelOffset + "' into register '" + register + "'.");
				if (register.sizeBits() == 8)
					b = (byte) labelOffset;
				else if (register.sizeBits() == 16)
					s = (short) labelOffset;
				else if (register.sizeBits() == 32)
					i = (int) labelOffset;
				else if (register.sizeBits() == 64)
					l = labelOffset;
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
				case ADD -> _bytes = register.getMathRegFromValBytes(doSpecialByteOperation, register,
					(byte) 0xc0, (byte) 0x04, register.sizeBits() == 8 ? (byte) 0x80 : (byte) 0x81);
				case SUB -> _bytes = register.getMathRegFromValBytes(doSpecialByteOperation, register,
					(byte) 0xe8, (byte) 0x2c, register.sizeBits() == 8 ? (byte) 0x80 : (byte) 0x81);
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
				case CMP -> bytes.add(register.sizeBits() == 8 ? (byte) 0x38 : (byte) 0x39);
				case ADD -> bytes.add(register.sizeBits() == 8 ? (byte) 0x00 : (byte) 0x01);
				case SUB -> bytes.add(register.sizeBits() == 8 ? (byte) 0x28 : (byte) 0x29);
			}

			bytes.add(Parser.encodeModRm(sourceRegister, register));
		}

		return bytes;
	}

}
