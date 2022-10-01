package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class SETPOS extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[] {
			"setpos"
		};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		String newPosString = lineContent.substring(7);
		int newPos;
		try {
			newPos = Parser.parseInt(newPosString);
		} catch (NumberFormatException e) {
			throw new SyntaxException(e.getMessage());
		}
		List<Byte> bytes = new ArrayList<>();
		for (long i = 0; i < newPos - Parser.currentByteOffset; i++)
			bytes.add((byte) 0x00);
		return bytes;
	}
}
