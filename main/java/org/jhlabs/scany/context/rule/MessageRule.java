package org.jhlabs.scany.context.rule;

public class MessageRule {

	public static final String KEYSIGN = "keysign";
	
	public static final String BODY = "body";
	
	private String keysign;

	private String encryption;
	
	private Boolean compressable;

	public String getKeysign() {
		return keysign;
	}

	public void setKeysign(String keysign) {
		this.keysign = keysign;
	}

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
	
}
