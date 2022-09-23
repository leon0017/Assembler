package me.leonrobi;

import java.util.ArrayList;
import java.util.List;

public abstract class Opcode {

	private static final List<Opcode> opcodes = new ArrayList<>();

	public abstract String identifier();

	public abstract List<Byte> handler(String lineContent) throws SyntaxException;

	public static void add(Opcode opcode) {
		opcodes.add(opcode);
	}

	public static Opcode searchForOpcode(String opcodeToRead) {
		for (Opcode opcode : opcodes) {
			if (opcode.identifier().equals(opcodeToRead))
				return opcode;
		}
		return null;
	}

}
