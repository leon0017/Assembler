package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.List;

public class ORG extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[] {
			"org"
		};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		String offsetString = lineContent.substring(4);
		long offset;
		try {
			offset = Parser.parseLong(offsetString);
		} catch (NumberFormatException e) {
			throw new SyntaxException(e.getMessage());
		}
		Parser.orgOffset = offset;
		return null;
	}
}
