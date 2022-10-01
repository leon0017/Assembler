package me.leonrobi.opcodes;

import me.leonrobi.DestSrcInstructionHelper;
import me.leonrobi.Opcode;
import me.leonrobi.SyntaxException;

import java.util.List;

public class SUB extends Opcode {

	@Override
	public String[] identifiers() {
		return new String[] {
			"sub"
		};
	}

	@Override
	public List<Byte> handler(String lineContent, int lineNumber) throws SyntaxException {
		DestSrcInstructionHelper destSrcInstructionHelper = new DestSrcInstructionHelper(
			lineContent,
			true,
			DestSrcInstructionHelper.Type.SUB
		);
		return destSrcInstructionHelper.handle();
	}
}
