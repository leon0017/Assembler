package me.leonrobi;

import java.util.ArrayList;
import java.util.List;

public record Label(String name, long byteOffset) {
	public static final List<Label> labels = new ArrayList<>();

	public static void add(Label label) {
		for (Label l : labels)
			if (l.name.equals(label.name))
				return;
		labels.add(label);
	}

	public static Label get(String name, int startingLine) {
		for (Label label : labels)
			if (label.name.equals(name))
				return label;
		// Label is not yet processed OR label does not exist.
		int lineNumber = -1;
		for (int i = startingLine; i < Reader.lines.size(); i++) {
			Reader.Line line = Reader.lines.get(i);
			if (line.lineContent().split(" ")[0].equals(name + ":")) {
				lineNumber = line.lineNumber();
			}
		}
		if (lineNumber == -1)
			return null;
		// Found label. Next process the instructions before it to calculate the label byte offset.
		long byteOffset = 0;
		for (int i = startingLine; i < Reader.lines.size(); i++) {
			Reader.Line line = Reader.lines.get(i);
			if (line.lineNumber() == lineNumber)
				break;
			byteOffset += Parser.parseLine(line.lineContent(), line.lineNumber(), false);
		}
		Label label = new Label(name, byteOffset + Parser.currentByteOffset);
		Label.add(label);
		return label;
	}
}
