package me.leonrobi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Output {
	private static final List<Byte> bytes = new ArrayList<>();

	public static void addOutput(List<Byte> toAdd) {
		bytes.addAll(toAdd);
	}

	public static void writeOutput(String outputFile) {
		File outputFileObj = new File(outputFile);
		if (!outputFileObj.exists()) {
			try {
				boolean couldCreate = outputFileObj.createNewFile();
				if (!couldCreate) {
					System.out.println("Failed to create output file.");
					System.exit(1);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			byte[] data = new byte[bytes.size()];
			for (int i = 0; i < bytes.size(); i++)
				data[i] = bytes.get(i);
			FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
			fileOutputStream.write(data, 0, data.length);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			System.out.println("Failed to write to output file: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
