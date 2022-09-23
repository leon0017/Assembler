public class Main {
	public static void main(String[] args) {
		String inputFilePath = "C:\\Users\\leonr\\tmp\\test.asm";
		String outputFilePath = "C:\\Users\\leonr\\tmp\\test.asm.obj";
		Reader.startReading(inputFilePath);
		Output.writeOutput(outputFilePath);
	}
}