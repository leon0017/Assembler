package me.leonrobi.opcodes;

import me.leonrobi.Opcode;
import me.leonrobi.SyntaxException;

import java.util.List;

public class CMP extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[] {"cmp"};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		return null;
	}
}
