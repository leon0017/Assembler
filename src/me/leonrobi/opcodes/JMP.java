package me.leonrobi.opcodes;

import me.leonrobi.Label;
import me.leonrobi.Opcode;
import me.leonrobi.Parser;
import me.leonrobi.SyntaxException;

import java.util.ArrayList;
import java.util.List;

public class JMP extends Opcode {

	private enum JMPType {
		JMP((byte)0xeb),
		JO((byte)0x70),
		JNO((byte)0x71),
		JS((byte)0x78),
		JNS((byte)0x79),
		JE((byte)0x74),
		JZ((byte)0x74),
		JNE((byte)0x75),
		JNZ((byte)0x75),
		JB((byte)0x72),
		JNAE((byte)0x72),
		JC((byte)0x72),
		JNB((byte)0x73),
		JAE((byte)0x73),
		JNC((byte)0x73),
		JBE((byte)0x76),
		JNA((byte)0x76),
		JA((byte)0x77),
		JNBE((byte)0x77),
		JL((byte)0x7c),
		JNGE((byte)0x7c),
		JGE((byte)0x7d),
		JNL((byte)0x7d),
		JLE((byte)0x7e),
		JNG((byte)0x7e),
		JG((byte)0x7f),
		JNLE((byte)0x7f),
		JP((byte)0x7a),
		JPE((byte)0x7a),
		JNP((byte)0x7b),
		JPO((byte)0x7b),
		JCXZ((byte)0xe3);

		private final byte shortJumpOpcode;

		JMPType(byte shortJumpOpcode) {
			this.shortJumpOpcode = shortJumpOpcode;
		}

		public byte getShortJumpOpcode() {
			return this.shortJumpOpcode;
		}
	}

	@Override
	public String[] identifiers() {
		JMPType[] values = JMPType.values();
		String[] identifiers = new String[values.length];
		for (int i = 0; i < identifiers.length; i++)
			identifiers[i] = values[i].toString().toLowerCase();
		return identifiers;
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

		if (offset <= 127 && offset >= -127) { // Short Jump
			byte offsetByte = (byte) (offset & 0xff);
			bytes.add(jmpType.getShortJumpOpcode());
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
