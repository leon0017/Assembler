package me.leonrobi;

public class ModifiableString {
	private String string;

	public ModifiableString(String string) {
		this.string = string;
	}

	public String string() {
		return this.string;
	}

	public String string(String string) {
		this.string = string;
		return string;
	}
}
