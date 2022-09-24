package me.leonrobi;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	public static long currentByteOffset = 0x0;
	public static int bits = 16;
	private static final List<Byte> emptyBytes = new ArrayList<>();

	static {
		emptyBytes.add((byte)0x00);
	}

	private enum HandleOpcodeResultEnum {
		FAILED,
		SUCCESS,
		LABEL
	}

	private record HandleOpcodeResult(HandleOpcodeResultEnum result, List<Byte> bytes) {}

	private static HandleOpcodeResult handleOpcode(ModifiableString modString, int lineNumber) {
		String originalString = modString.string();
		String opcodeToCompare = originalString.split(" ")[0];
		Opcode opcode = Opcode.searchForOpcode(opcodeToCompare.toLowerCase());

		boolean opcodeNull = opcode == null;

		if (opcodeToCompare.endsWith(":") && opcodeNull) {
			StringBuilder labelNameBuilder = new StringBuilder(opcodeToCompare);
			labelNameBuilder.setLength(opcodeToCompare.length()-1);
			String labelName = labelNameBuilder.toString();
			Label.add(new Label(labelName, currentByteOffset));
			return new HandleOpcodeResult(HandleOpcodeResultEnum.LABEL, null);
		}

		if (opcodeNull) {
			System.out.println("ERROR on line " + lineNumber + " - Unknown opcode \"" + opcodeToCompare + "\"");
			System.exit(1);
			return new HandleOpcodeResult(HandleOpcodeResultEnum.FAILED, null);
		}

		List<Byte> bytes;

		try {
			bytes = opcode.handler(originalString, lineNumber);
		} catch (SyntaxException e) {
			System.out.println("ERROR on line " + lineNumber + " - \"" + e.getMessage() + "\"");
			System.exit(1);
			return new HandleOpcodeResult(HandleOpcodeResultEnum.FAILED, null);
		}

		return new HandleOpcodeResult(HandleOpcodeResultEnum.SUCCESS, bytes);
	}

	public static long parseLine(String lineContent, int lineNumber, boolean writeToOutput) {
		if (lineContent.equals(""))
			return 0;

		ModifiableString modString = new ModifiableString(lineContent);

		HandleOpcodeResult result = handleOpcode(modString, lineNumber);
		if (result.result == HandleOpcodeResultEnum.FAILED)
			return 0;
		List<Byte> bytes = result.bytes;
		if (result.result == HandleOpcodeResultEnum.LABEL || bytes == null)
			bytes = emptyBytes;
		if (writeToOutput) {
			for (byte c : bytes)
				System.out.println(" " + String.format("%08x", Parser.currentByteOffset) + "  " + String.format("%x", (long) (c & 0xff)) + "                " + modString.string());
		}
		if (!bytes.equals(emptyBytes) && writeToOutput) {
			Output.addOutput(bytes);
			currentByteOffset += bytes.size();
		}
		return bytes.size();
	}

	public static byte parseByte(String byteString) throws NumberFormatException {
		int parsed;
		if (byteString.startsWith("0x"))
			parsed = Integer.parseInt(byteString.substring(2), 16);
		else if (byteString.startsWith("0b"))
			parsed = Integer.parseInt(byteString.substring(2), 2);
		else
			parsed = Integer.parseInt(byteString, 10);
		if (parsed > 0xFF)
			throw new NumberFormatException("Byte out of range.");
		return (byte)parsed;
	}

}
