import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Reader {
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
			while (scanner.hasNextLine())
				Parser.parseLine(scanner.nextLine(), ++currentLineNumber);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
