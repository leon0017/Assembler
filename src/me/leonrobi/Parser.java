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

	private static void removeComments(ModifiableString modString) {
		String string = modString.string();

		StringBuilder builder = new StringBuilder(string);
		char[] chars = string.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			char chr = chars[i];
			if (chr == '#') {
				builder.setLength(i);
				break;
			}
		}

		modString.string(builder.toString());
	}

	private static boolean isEmptyLine(ModifiableString modString) {
		String string = modString.string();

		if (string.isEmpty())
			return true;
		char[] chars = string.toCharArray();
		for (char chr : chars) {
			if (chr != ' ' && chr != '\t')
				return false;
		}

		return true;
	}

	private static void removeStartSpaces(ModifiableString modString) {
		String string = modString.string();
		StringBuilder builder = new StringBuilder();

		char[] chars = string.toCharArray();
		int i = 0;

		while (chars[i] == ' ' || chars[i] == '\t')
			++i;
		for (int j = i; j < chars.length; j++)
			builder.append(chars[j]);

		modString.string(builder.toString());
	}

	private enum HandleOpcodeResultEnum {
		FAILED,
		SUCCESS,
		LABEL;
	}

	private record HandleOpcodeResult(HandleOpcodeResultEnum result, List<Byte> bytes) {}

	private static HandleOpcodeResult handleOpcode(ModifiableString modString, int lineNumber) {
		String originalString = modString.string();
		String opcodeToCompare = originalString.split(" ")[0];
		Opcode opcode = Opcode.searchForOpcode(opcodeToCompare.toLowerCase());

		boolean opcodeNull = opcode == null;

		if (opcodeToCompare.endsWith(":") && opcodeNull) {
			Label.add(new Label(opcodeToCompare.substring(opcodeToCompare.length()-1), currentByteOffset));
			return new HandleOpcodeResult(HandleOpcodeResultEnum.LABEL, null);
		}

		if (opcodeNull) {
			System.out.println("ERROR on line " + lineNumber + " - Unknown opcode \"" + opcodeToCompare + "\"");
			System.exit(1);
			return new HandleOpcodeResult(HandleOpcodeResultEnum.FAILED, null);
		}

		List<Byte> bytes;

		try {
			bytes = opcode.handler(originalString);
		} catch (SyntaxException e) {
			System.out.println("ERROR on line " + lineNumber + " - \"" + e.getMessage() + "\"");
			System.exit(1);
			return new HandleOpcodeResult(HandleOpcodeResultEnum.FAILED, null);
		}

		return new HandleOpcodeResult(HandleOpcodeResultEnum.SUCCESS, bytes);
	}

	public static void parseLine(String lineContent, int lineNumber) {
		ModifiableString modString = new ModifiableString(lineContent);

		removeComments(modString);
		if (isEmptyLine(modString))
			return;
		removeStartSpaces(modString);

		HandleOpcodeResult result = handleOpcode(modString, lineNumber);
		if (result.result == HandleOpcodeResultEnum.FAILED)
			return;
		List<Byte> bytes = result.bytes;
		if (result.result == HandleOpcodeResultEnum.LABEL || bytes == null)
			bytes = emptyBytes;
		for (byte c : bytes)
			System.out.println(" " + String.format("%08x", Parser.currentByteOffset) + "  " + String.format("%x", (long)(c & 0xff)) + "                " + modString.string());
		if (!bytes.equals(emptyBytes)) {
			Output.addOutput(bytes);
			currentByteOffset += bytes.size();
		}
	}
}
