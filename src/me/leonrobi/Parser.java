package me.leonrobi;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	public static long currentByteOffset = 0x0;
	public static int bits = 16;

	private enum HandleOpcodeResultEnum {
		FAILED,
		SUCCESS,
		LABEL
	}

	private record HandleOpcodeResult(HandleOpcodeResultEnum result, List<Byte> bytes) {}

	private static HandleOpcodeResult handleOpcode(ModifiableString modString, int lineNumber) {
		String originalString = modString.string();
		String opcodeToCompare = originalString.split(" ")[0];
		Opcode opcode = Opcode.searchForOpcode(opcodeToCompare.toLowerCase());

		boolean opcodeNull = opcode == null;

		if (opcodeToCompare.endsWith(":") && opcodeNull) {
			StringBuilder labelNameBuilder = new StringBuilder(opcodeToCompare);
			labelNameBuilder.setLength(opcodeToCompare.length()-1);
			String labelName = labelNameBuilder.toString();
			Label.add(new Label(labelName, currentByteOffset));
			return new HandleOpcodeResult(HandleOpcodeResultEnum.LABEL, null);
		}

		if (opcodeNull) {
			System.out.println("ERROR on line " + lineNumber + " - Unknown opcode \"" + opcodeToCompare + "\"");
			System.exit(1);
			return new HandleOpcodeResult(HandleOpcodeResultEnum.FAILED, null);
		}

		List<Byte> bytes;

		try {
			bytes = opcode.handler(originalString, lineNumber);
		} catch (SyntaxException e) {
			System.out.println("ERROR on line " + lineNumber + " - \"" + e.getMessage() + "\"");
			System.exit(1);
			return new HandleOpcodeResult(HandleOpcodeResultEnum.FAILED, null);
		}

		return new HandleOpcodeResult(HandleOpcodeResultEnum.SUCCESS, bytes);
	}

	public static long parseLine(String lineContent, int lineNumber, boolean writeToOutput) {
		if (lineContent.equals(""))
			return 0;

		ModifiableString modString = new ModifiableString(lineContent);

		HandleOpcodeResult result = handleOpcode(modString, lineNumber);
		if (result.result == HandleOpcodeResultEnum.FAILED)
			return 0;
		List<Byte> bytes = result.bytes;
		if (result.result == HandleOpcodeResultEnum.LABEL || bytes == null)
			return 0;
		if (writeToOutput) {
			for (byte c : bytes) {
				if (Main.doneFirstParse)
					System.out.println(" " + String.format("%08x", Parser.currentByteOffset) + "  " + String.format("%x", (long) (c & 0xff)) + "                " + modString.string());
			}
			Output.addOutput(bytes);
			currentByteOffset += bytes.size();
		}
		return bytes.size();
	}

	public static long parseLong(String longString) throws NumberFormatException {
		long parsed;
		if (longString.startsWith("0x"))
			parsed = Long.parseLong(longString.substring(2), 16);
		else if (longString.startsWith("0b"))
			parsed = Long.parseLong(longString.substring(2), 2);
		else
			parsed = Long.parseLong(longString, 10);
		return parsed;
	}

	public static int parseInt(String intString) throws NumberFormatException {
		long parsed = parseLong(intString);
		if (parsed > 0xFFFFFFFFL)
			throw new NumberFormatException("Integer out of range.");
		return (int)parsed;
	}

	public static byte parseByte(String byteString) throws NumberFormatException {
		int parsed = parseInt(byteString);
		if (parsed > 0xFF)
			throw new NumberFormatException("Byte out of range.");
		return (byte)parsed;
	}

	public static short parseShort(String shortString) throws NumberFormatException {
		int parsed = parseInt(shortString);
		if (parsed > 0xFFFF)
			throw new NumberFormatException("Short out of range.");
		return (short)parsed;
	}

	public static List<Byte> addShortToByteList(short s, List<Byte> bytes) {
		List<Byte> newBytes = new ArrayList<>(bytes);
		byte first = (byte)(s & 0xFF);
		byte second = (byte)((s >> 8) & 0xFF);
		newBytes.add(first);
		newBytes.add(second);
		return newBytes;
	}

	public static List<Byte> addIntToByteList(int i, List<Byte> bytes) {
		List<Byte> newBytes = new ArrayList<>(bytes);
		byte a = (byte)(i & 0xFF);
		byte b = (byte)((i >> 8) & 0xFF);
		byte c = (byte)((i >> 16) & 0xFF);
		byte d = (byte)((i >> 24) & 0xFF);
		newBytes.add(a);
		newBytes.add(b);
		newBytes.add(c);
		newBytes.add(d);
		return newBytes;
	}

	public static List<Byte> addLongToByteList(long l, List<Byte> bytes) {
		List<Byte> newBytes = new ArrayList<>(bytes);
		byte a = (byte)(l & 0xFF);
		byte b = (byte)((l >> 8) & 0xFF);
		byte c = (byte)((l >> 16) & 0xFF);
		byte d = (byte)((l >> 24) & 0xFF);
		byte e = (byte)((l >> 32) & 0xFF);
		byte f = (byte)((l >> 40) & 0xFF);
		byte g = (byte)((l >> 48) & 0xFF);
		byte h = (byte)((l >> 56) & 0xFF);
		newBytes.add(a);
		newBytes.add(b);
		newBytes.add(c);
		newBytes.add(d);
		newBytes.add(e);
		newBytes.add(f);
		newBytes.add(g);
		newBytes.add(h);
		return newBytes;
	}

}
