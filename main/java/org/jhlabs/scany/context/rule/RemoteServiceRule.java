package org.jhlabs.scany.context.rule;

import org.jhlabs.scany.engine.entity.Schema;

public class RemoteServiceRule {

	private Schema schema;
	
	private String schemaConfigLocation;
	
	private String host;
	
	private int timeout;
	
	private String authentificationKey;
	
	private String messageEncryption;
	
	private Boolean messageCompressable;

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
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getAuthentificationKey() {
		return authentificationKey;
	}

	public void setAuthentificationKey(String authentificationKey) {
		this.authentificationKey = authentificationKey;
	}

	public String getMessageEncryption() {
		return messageEncryption;
	}

	public void setMessageEncryption(String messageEncryption) {
		this.messageEncryption = messageEncryption;
	}

	public Boolean getMessageCompressable() {
		return messageCompressable;
	}

	public void setMessageCompressable(Boolean messageCompressable) {
		this.messageCompressable = messageCompressable;
	}
	
}
