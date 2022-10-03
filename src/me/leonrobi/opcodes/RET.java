package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class RET extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[] {
			"ret"
		};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		List<Byte> bytes = new ArrayList<>();
		bytes.add((byte) 0xc3);
		return bytes;
	}
}
