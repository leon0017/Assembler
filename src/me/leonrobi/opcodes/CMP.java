package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.Register;
import me.leonrobi.SyntaxException;

import java.util.List;

public class CMP extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[] {"cmp"};
	}

	private List<Byte> getCompareRegToValOpcode(Register register) {
		return register.getCmpRegFromValBytes();
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
		Register otherRegister = null;
		byte b = 0;
		short s = 0;
		try {
			otherRegister = Register.valueOf(source.toUpperCase());
			if (otherRegister.sizeBits() != register.sizeBits())
				throw new SyntaxException("You cannot compare registers with different sizes.");
		} catch (IllegalArgumentException e) {
			try {
				if (register.sizeBits() == 8)
					b = Parser.parseByte(source);
				else if (register.sizeBits() == 16)
					s = Parser.parseShort(source);
			} catch (NumberFormatException nfe) {
				throw new SyntaxException("Failed to parse register/value for '" + source + "'");
			}
		}
		List<Byte> bytes;

		if (otherRegister == null) {
			bytes = getCompareRegToValOpcode(register);
			if (register.sizeBits() == 8)
				bytes.add(b);
			else if (register.sizeBits() == 16)
				bytes = Parser.addShortToByteList(s, bytes);
		} else {
			throw new SyntaxException("Comparing registers with other registers is not yet supported.");
		}

		return bytes;
	}
}
