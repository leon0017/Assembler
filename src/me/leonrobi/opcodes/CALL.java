package me.leonrobi.opcodes;

import me.leonrobi.Label;
import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class CALL extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[] {
			"call"
		};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		String[] split = lineContent.split(" ");
		String labelName = split[1];
		Label label = Label.get(labelName, lineNumber);
		if (label == null)
			throw new SyntaxException("Label \"" + labelName + "\" could not be found.");
		long offset = (label.byteOffset() - Parser.currentByteOffset);
		List<Byte> bytes = new ArrayList<>();
		if (Parser.bits == 16) {
			if (offset < 32771 && offset > -32766) {
				bytes.add((byte) 0xe8);
				bytes = Parser.addShortToByteList((short) (offset - 3), bytes);
			} else {
				throw new SyntaxException("Offset too distant. Offset=" + offset);
			}
		}
		return bytes;
	}
}
