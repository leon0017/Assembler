package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class DB extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[]{"db"};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		String byteString = lineContent.substring(3).toLowerCase();
		byte b;
		try {
			b = Parser.parseByte(byteString);
		} catch (NumberFormatException e) {
			throw new SyntaxException("Failed to parse byte '" + byteString + "' due to '" + e.getMessage() + "'");
		}
		List<Byte> bytes = new ArrayList<>();
		bytes.add(b);
		return bytes;
	}
}
