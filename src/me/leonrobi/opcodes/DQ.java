package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class DQ extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[]{"dq"};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		String longString = lineContent.substring(3).toLowerCase();
		long l;
		try {
			l = Parser.parseLong(longString);
		} catch (NumberFormatException e) {
			throw new SyntaxException("Failed to parse int '" + longString + "' due to '" + e.getMessage() + "'");
		}
		List<Byte> bytes = new ArrayList<>();
		bytes = Parser.addLongToByteList(l, bytes);
		return bytes;
	}
}
