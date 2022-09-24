package me.leonrobi;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	public static long currentByteOffset = 0x0;

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

	private static List<Byte> handleOpcode(ModifiableString modString, int lineNumber) {
		String originalString = modString.string();
		String opcodeToCompare = originalString.split(" ")[0].toLowerCase();
		Opcode opcode = Opcode.searchForOpcode(opcodeToCompare);

		if (opcode == null) {
			System.out.println("ERROR on line " + lineNumber + " - Unknown opcode \"" + opcodeToCompare + "\"");
			System.exit(1);
			return null;
		}

		List<Byte> bytes;

		try {
			bytes = new ArrayList<>(opcode.handler(originalString));
		} catch (SyntaxException e) {
			System.out.println("ERROR on line " + lineNumber + " - \"" + e.getMessage() + "\"");
			System.exit(1);
			return null;
		}

		return bytes;
	}

	public static void parseLine(String lineContent, int lineNumber) {
		ModifiableString modString = new ModifiableString(lineContent);

		removeComments(modString);
		if (isEmptyLine(modString))
			return;
		removeStartSpaces(modString);

		List<Byte> bytes = handleOpcode(modString, lineNumber);
		Output.addOutput(bytes, modString.string());
		currentByteOffset += bytes.size();
	}
}
