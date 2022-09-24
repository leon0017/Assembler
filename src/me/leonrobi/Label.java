package me.leonrobi;

import java.util.ArrayList;
import java.util.List;

public record Label(String name, long byteOffset) {
	public static final List<Label> labels = new ArrayList<>();

	public static void add(Label label) {
		labels.add(label);
	}
}
