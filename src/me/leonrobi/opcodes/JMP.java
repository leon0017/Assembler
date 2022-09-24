package me.leonrobi.opcodes;

import me.leonrobi.Label;
import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class JMP extends Opcode {

	private enum JMPType {
		JMP,
		JNE,
		JE
	}

	private byte getShortJmpByte(JMPType jmpType) {
		switch (jmpType) {
			case JMP -> {
				return (byte)0xfe;
			}
			case JNE -> {
				return (byte)0x75;
			}
			case JE -> {
				return (byte)0x74;
			}
		}
		return (byte)0x0;
	}

	@Override
	public String[] identifiers() {
		return new String[]{"jmp","jne","je"};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		String[] split = lineContent.split(" ");
		String identifier = split[0].toUpperCase();
		JMPType jmpType;
		try {
			jmpType = JMPType.valueOf(identifier);
		} catch (IllegalArgumentException e) {
			throw new SyntaxException("Internal exception: could not get valueOf JMPType.");
		}
		String labelName = split[1];
		Label label = Label.get(labelName, lineNumber);
		if (label == null)
			throw new SyntaxException("Label \"" + labelName + "\" could not be found.");

		long offset = label.byteOffset() - Parser.currentByteOffset;

		List<Byte> bytes = new ArrayList<>();

		if (offset <= 127 && offset >= -127) { // Small Jump
			byte offsetByte = (byte) (offset & 0xff);
			bytes.add(getShortJmpByte(jmpType));
			if (offset <= 0) // Backwards OR same location
				bytes.add((byte)((0xff + offsetByte) - 0x1));
			else
				bytes.add(offsetByte);
			return bytes;
		} else {
			throw new SyntaxException("Further jumps are not supported yet.");
		}
	}
}
