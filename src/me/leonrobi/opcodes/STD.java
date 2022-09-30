package me.leonrobi.opcodes;

import me.leonrobi.Opcode;

import java.util.ArrayList;
import java.util.List;

public class STD extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[]{"sti"};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) {
		List<Byte> bytes = new ArrayList<>();
		bytes.add((byte) 0xFD);
		return bytes;
	}
}
