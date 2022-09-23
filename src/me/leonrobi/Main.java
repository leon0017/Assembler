package me.leonrobi;

import me.leonrobi.opcodes.NOP;

import java.io.File;

public class Main {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Invalid syntax! Correct arguments: [inputFilePath] [outputFilePath]");
			System.exit(1);
			return;
		}

		String inputFilePath = args[0];

		if (!new File(inputFilePath).exists()) {
			System.out.println("The input file specified does not exist.");
			System.exit(1);
			return;
		}

		Opcode.add(new NOP());

		String outputFilePath = args[1];

		Reader.startReading(inputFilePath);
		Output.writeOutput(outputFilePath);
	}
}