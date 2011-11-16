package org.jhlabs.scany.context.rule;

public class MessageRule {

	public static final String KEYSIGN = "keysign";
	
	public static final String BODY = "body";
	
	private String encryption;
	
	private Boolean compressable;
	
	private String text;

	public String getEncryption() {
		return encryption;
	}

	public void setEncryption(String encryption) {
		this.encryption = encryption;
	}

	public Boolean getCompressable() {
		return compressable;
	}

	public void setCompressable(Boolean compressable) {
		this.compressable = compressable;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
