package me.leonrobi.opcodes;

import me.leonrobi.Opcode;

import java.util.ArrayList;
import java.util.List;

public class NOP extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[]{"nop"};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) {
		List<Byte> characters = new ArrayList<>();
		byte c = (byte) 0x90;
		characters.add(c);
		return characters;
	}
}
