package me.leonrobi.opcodes;

import me.leonrobi.Label;
import me.leonrobi.Opcode;
import me.leonrobi.SyntaxException;

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
		System.out.println(labelName + " : " + label.byteOffset());
		return null;
	}
}
