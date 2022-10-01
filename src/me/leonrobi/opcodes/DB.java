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
		String byteString = lineContent.substring(3);
		byte b = 0;
		boolean doString = false;
		List<Byte> bytes = new ArrayList<>();
		try {
			b = Parser.parseByte(byteString.toLowerCase());
		} catch (NumberFormatException e) {
			if (byteString.startsWith("\"") && byteString.endsWith("\"")) {
				doString = true;
				String stringString = byteString.substring(1, byteString.length()-1);
				char[] chars = stringString.toCharArray();
				for (char c : chars) {
					bytes.add((byte) c);
				}
			} else {
				throw new SyntaxException("Failed to parse byte/string '" + byteString + "' due to '" + e.getMessage() + "'");
			}
		}
		if (!doString)
			bytes.add(b);
		return bytes;
	}
}
