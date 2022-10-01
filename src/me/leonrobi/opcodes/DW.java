package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class DW extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[]{"dw"};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		String shortString = lineContent.substring(3).toLowerCase();
		short s;
		try {
			s = Parser.parseShort(shortString);
		} catch (NumberFormatException e) {
			throw new SyntaxException("Failed to parse short '" + shortString + "' due to '" + e.getMessage() + "'");
		}
		List<Byte> bytes = new ArrayList<>();
		bytes = Parser.addShortToByteList(s, bytes);
		return bytes;
	}
}
