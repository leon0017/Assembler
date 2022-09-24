package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.List;

public class BITS extends Opcode {

	@Override
	public String identifier() {
		return "bits";
	}

	@Override
	public List<Byte> handler(String lineContent) throws SyntaxException {
		String bits = lineContent.substring(5);
		switch (bits) {
			case "16" -> Parser.bits = 16;
			case "32" -> Parser.bits = 32;
			case "64" -> Parser.bits = 64;
			default -> throw new SyntaxException("Invalid bits specified: '" + bits + "'");
		}
		return null;
	}
}
