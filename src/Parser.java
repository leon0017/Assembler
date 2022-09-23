public class Parser {
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

	public static void removeStartSpaces(ModifiableString modString) {
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

	public static void parseLine(String lineContent, int lineNumber) {
		ModifiableString modString = new ModifiableString(lineContent);

		removeComments(modString);
		boolean isEmptyLine = isEmptyLine(modString);
		if (!isEmptyLine) {
			removeStartSpaces(modString);
		}

		String finalString;

		if (isEmptyLine)
			finalString = "EMPTY";
		else
			finalString = lineNumber + ": " + modString.string();

		System.out.println(finalString);
		Output.addOutput(finalString);
	}
}
