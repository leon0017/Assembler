package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.List;

public class UNTIL extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[]{"until"};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		String[] split = lineContent.split(" ");
		int until;
		try {
			until = Parser.parseInt(split[1]);
		} catch (NumberFormatException e) {
			throw new SyntaxException("Failed to parse integer '" + split[1] + "' due to '" + e.getMessage() + "'");
		}
		String instruction = lineContent.substring(split[0].length() + split[1].length() + 2);
		for (long i = Parser.currentByteOffset; i < until; i++)
			Parser.parseLine(instruction, lineNumber, true);
		return null;
	}
}
