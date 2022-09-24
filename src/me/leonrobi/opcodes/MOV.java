package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.Register;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class MOV extends Opcode {

	@Override
	public String identifier() {
		return "mov";
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
		Register outRegister = null;
		byte b = 0;
		try {
			outRegister = Register.valueOf(source.toUpperCase());
		} catch (IllegalArgumentException e) {
			try {
				b = Parser.parseByte(source);
			} catch (NumberFormatException nfe) {
				throw new SyntaxException("Failed to parse register/byte for '" + source + "'");
			}
		}

		List<Byte> bytes = new ArrayList<>();

		if (outRegister == null) {
			if (register.sizeBits() != 8)
				throw new SyntaxException("Moving to registers larger than 8 bits is not yet supported.");
			if (register == Register.AL)
				bytes.add((byte)0xb0);
			else if (register == Register.BL)
				bytes.add((byte)0xb3);
			else if (register == Register.CL)
				bytes.add((byte)0xb1);
			else if (register == Register.DL)
				bytes.add((byte)0xb2);
			else if (register == Register.AH)
				bytes.add((byte)0xb4);
			else if (register == Register.BH)
				bytes.add((byte)0xb7);
			else if (register == Register.CH)
				bytes.add((byte)0xb5);
			else if (register == Register.DH)
				bytes.add((byte)0xb6);
			else
				throw new SyntaxException("Register '" + register + "' does not support MOV.");
			bytes.add(b);
		} else {
			throw new SyntaxException("Moving to registers from registers is not yet supported.");
		}

		return bytes;
	}
}
