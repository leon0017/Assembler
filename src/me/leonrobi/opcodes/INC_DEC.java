package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.Register;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class INC_DEC extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[] {"inc","dec"};
	}

	private List<Byte> getCode(Register register, boolean dec) {
		List<Byte> bytes = new ArrayList<>();

		if (register.sizeBits() == 8)
			bytes.add((byte)0xfe);
		if (register.sizeBits() == 32 && Parser.bits == 16)
			bytes.add((byte)0x66);
		if (register.sizeBits() == 16 && Parser.bits == 32)
			bytes.add((byte)0x66);

		if (register.getOffset() == -1)
			return null;
		byte offset = (byte)(register.getOffset());
		if (register.sizeBits() == 8)
			offset += (byte)0xc0;
		else
			offset += (byte)0x40;
		if (dec)
			offset += (byte)8;
		bytes.add(offset);
		return bytes;
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		boolean dec = lineContent.substring(0, 3).equalsIgnoreCase("dec");
		String registerString = lineContent.substring(4);
		Register register;
		try {
			register = Register.valueOf(registerString.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new SyntaxException("Invalid register '" + registerString + "' specified.");
		}

		List<Byte> bytes = getCode(register, dec);
		if (bytes == null)
			throw new SyntaxException("You cannot use INC on the register '" + register + "'.");
		return bytes;
	}
}
