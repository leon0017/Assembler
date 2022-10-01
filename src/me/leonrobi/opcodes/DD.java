package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class DD extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[]{"dd"};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		String intString = lineContent.substring(3).toLowerCase();
		int i;
		try {
			i = Parser.parseInt(intString);
		} catch (NumberFormatException e) {
			throw new SyntaxException("Failed to parse int '" + intString + "' due to '" + e.getMessage() + "'");
		}
		List<Byte> bytes = new ArrayList<>();
		bytes = Parser.addIntToByteList(i, bytes);
		return bytes;
	}
}
