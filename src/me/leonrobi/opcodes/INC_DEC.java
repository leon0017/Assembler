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

	private List<Byte> getCode(Register register, boolean dec) throws SyntaxException {
		List<Byte> bytes = new ArrayList<>();

		byte offset = (byte) register.getOffset();

		if (register.sizeBits() == 8) {
			bytes.add((byte) 0xfe);
			offset += 0xc0;
		}

		if (register.sizeBits() == 16) {
			if (Parser.bits != 16)
				bytes.add((byte) 0x66);
			if (Parser.bits == 64) {
				bytes.add((byte) 0xff);
				offset += 0xc0;
			} else {
				offset += 0x40;
			}
		}

		else if (register.sizeBits() == 32) {
			if (Parser.bits == 16)
				bytes.add((byte) 0x66);
			if (Parser.bits == 64) {
				bytes.add((byte) 0xff);
				offset += 0xc0;
			} else {
				offset += 0x40;
			}
		}

		else if (register.sizeBits() == 64) {
			if (Parser.bits != 64)
				throw new SyntaxException("You cannot use 64-bit registers in non 64-bit mode.");
			bytes.add((byte) 0x48);
			bytes.add((byte) 0xff);
			offset += 0xc0;
		}

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
