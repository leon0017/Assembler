package me.leonrobi.opcodes;

import me.leonrobi.Label;
import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class JMP extends Opcode {

	@Override
	public String identifier() {
		return "jmp";
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		String labelName = lineContent.substring(4);
		Label label = Label.get(labelName, lineNumber);
		if (label == null)
			throw new SyntaxException("Label \"" + labelName + "\" could not be found.");

		long offset = label.byteOffset() - Parser.currentByteOffset;

		List<Byte> bytes = new ArrayList<>();

		if (offset <= 127 && offset >= -127) { // Small Jump
			byte offsetByte = (byte) (offset & 0xff);
			bytes.add((byte)0xeb);
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
