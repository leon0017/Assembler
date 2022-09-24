package me.leonrobi;

import java.util.List;

public class Parser {
	public static long currentByteOffset = 0x0;
	public static int bits = 16;

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
			return 0;
		if (writeToOutput) {
			for (byte c : bytes) {
				if (Main.doneFirstParse)
					System.out.println(" " + String.format("%08x", Parser.currentByteOffset) + "  " + String.format("%x", (long) (c & 0xff)) + "                " + modString.string());
			}
			Output.addOutput(bytes);
			currentByteOffset += bytes.size();
		}
		return bytes.size();
	}

	public static int parseInt(String intString) throws NumberFormatException {
		int parsed;
		if (intString.startsWith("0x"))
			parsed = Integer.parseInt(intString.substring(2), 16);
		else if (intString.startsWith("0b"))
			parsed = Integer.parseInt(intString.substring(2), 2);
		else
			parsed = Integer.parseInt(intString, 10);
		return parsed;
	}

	public static byte parseByte(String byteString) throws NumberFormatException {
		int parsed = parseInt(byteString);
		if (parsed > 0xFF)
			throw new NumberFormatException("Byte out of range.");
		return (byte)parsed;
	}

}
