package me.leonrobi;

import me.leonrobi.opcodes.*;

import java.io.File;

public class Main {
	public static boolean doneFirstParse = false;
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
		Opcode.add(new BITS());
		Opcode.add(new CLI());
		Opcode.add(new CLD());
		Opcode.add(new STD());
		Opcode.add(new STI());
		Opcode.add(new HLT());
		Opcode.add(new AAA());
		Opcode.add(new JMP());
		Opcode.add(new MOV());
		Opcode.add(new INT());
		Opcode.add(new CMP());
		Opcode.add(new DB());
		Opcode.add(new DW());
		Opcode.add(new DD());
		Opcode.add(new DQ());
		Opcode.add(new INC_DEC());
		Opcode.add(new ADD());
		Opcode.add(new SUB());
		Opcode.add(new ORG());
		Opcode.add(new SETPOS());
		Opcode.add(new XOR());
		Opcode.add(new CALL());
		Opcode.add(new RET());

		String outputFilePath = args[1];

		Reader.startReading(inputFilePath);
		doneFirstParse = true;
		Parser.currentByteOffset = 0;
		Output.bytes.clear();
		Reader.lines.clear();

		Reader.startReading(inputFilePath);
		Output.writeOutput(outputFilePath);
	}
}