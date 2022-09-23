import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Output {
	private static final StringBuilder stringBuilder = new StringBuilder();

	public static void addOutput(String toAdd) {
		stringBuilder.append(toAdd);
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
			Files.write(Paths.get(outputFile), stringBuilder.toString().getBytes());
		} catch (IOException e) {
			System.out.println("Failed to write to output file: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
