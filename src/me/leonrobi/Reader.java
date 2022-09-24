package me.leonrobi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {
	public static final List<Line> lines = new ArrayList<>();
	public record Line(String lineContent, int lineNumber) {}

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

	public static void startReading(String inputFilePath) {
		File inputFile = new File(inputFilePath);

		if (!inputFile.exists()) {
			System.out.println("The input file specified does not exist.");
			System.exit(1);
			return;
		}

		try {
			Scanner scanner = new Scanner(inputFile);
			int currentLineNumber = 0;
			while (scanner.hasNextLine()) {
				String lineContent = scanner.nextLine();
				ModifiableString modString = new ModifiableString(lineContent);
				removeComments(modString);
				if (!isEmptyLine(modString))
					removeStartSpaces(modString);
				lines.add(new Line(modString.string(), ++currentLineNumber));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (Line line : lines)
			Parser.parseLine(line.lineContent, line.lineNumber, true);
	}
}
