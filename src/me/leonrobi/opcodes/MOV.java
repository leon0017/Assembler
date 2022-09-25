package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.Register;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class MOV extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[]{"mov"};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		String[] split = lineContent.substring(4).split(",");
		String registerName = split[0];
		Register register;
		try {
			register = Register.valueOf(registerName.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new SyntaxException("Unknown register specified: '" + registerName + "'");
		}

		String source = split[1].replaceAll(" ", "").replaceAll("\t", "");
		Register inRegister = null;
		byte b = 0;
		short s = 0;
		try {
			inRegister = Register.valueOf(source.toUpperCase());
			if (inRegister.sizeBits() != register.sizeBits())
				throw new SyntaxException("You cannot move registers with different sizes.");
		} catch (IllegalArgumentException e) {
			try {
				if (register.sizeBits() == 8)
					b = Parser.parseByte(source);
				if (register.sizeBits() == 16)
					s = Parser.parseShort(source);
			} catch (NumberFormatException nfe) {
				throw new SyntaxException("Failed to parse register/value for '" + source + "'");
			}
		}

		List<Byte> bytes = new ArrayList<>();

		if (inRegister == null) {
			List<Byte> movRegFromValBytes = register.getMovRegFromValBytes();
			if (movRegFromValBytes == null)
				throw new SyntaxException("Register '" + register + "' does not support MOV.");
			bytes.addAll(movRegFromValBytes);
			if (register.sizeBits() == 8) {
				List<Byte> newBytes = new ArrayList<>(bytes);
				newBytes.add(b);
				bytes = newBytes;
			} else if (register.sizeBits() == 16) {
				List<Byte> newBytes = new ArrayList<>();
				if (Parser.bits == 32 || Parser.bits == 64)
					newBytes.add((byte)0x66);
				newBytes.addAll(bytes);
				bytes = Parser.addShortToByteList(s, newBytes);
			}
		} else {
			throw new SyntaxException("Moving to registers from registers is not yet supported.");
		}

		return bytes;
	}
}
