package org.jhlabs.scany.context.rule;

import org.jhlabs.scany.context.type.MessageFormat;
import org.jhlabs.scany.engine.entity.Schema;

public class HttpServiceRule {

	private Schema schema;

	private String schemaConfigLocation;

	private String characterEncoding;
	
	private String url;
	
	private MessageFormat messageFormat;
	
	private MessageRule keysignMessageRule;

	private MessageRule bodyMessageRule;
	
	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public String getSchemaConfigLocation() {
		return schemaConfigLocation;
	}

	public void setSchemaConfigLocation(String schemaConfigLocation) {
		this.schemaConfigLocation = schemaConfigLocation;
	}
	
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MessageFormat getMessageFormat() {
		return messageFormat;
	}

	public void setMessageFormat(MessageFormat messageFormat) {
		this.messageFormat = messageFormat;
	}

	public MessageRule getKeysignMessageRule() {
		return keysignMessageRule;
	}

	public void setKeysignMessageRule(MessageRule keysignMessageRule) {
		this.keysignMessageRule = keysignMessageRule;
	}

	public MessageRule getBodyMessageRule() {
		return bodyMessageRule;
	}

	public void setBodyMessageRule(MessageRule bodyMessageRule) {
		this.bodyMessageRule = bodyMessageRule;
	}
	
}
