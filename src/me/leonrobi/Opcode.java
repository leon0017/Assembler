package me.leonrobi;

import java.util.ArrayList;
import java.util.List;

public abstract class Opcode {

	private static final List<Opcode> opcodes = new ArrayList<>();

	public abstract String[] identifiers();

	public abstract List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException;

	public static void add(Opcode opcode) {
		opcodes.add(opcode);
	}

	public static Opcode searchForOpcode(String opcodeToRead) {
		for (Opcode opcode : opcodes) {
			for (String identifier : opcode.identifiers()) {
				if (identifier.equals(opcodeToRead)) {
					return opcode;
				}
			}
		}
		return null;
	}

}
