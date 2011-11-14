package org.jhlabs.scany.context.rule;

import org.jhlabs.scany.engine.entity.Schema;

public class LocalServiceRule {

	private Schema schema;
	
	private String schemaConfigLocation;
	
	private String directory;
	
	private String characterEncoding;
	
	private SpoolingRule spoolingRule;

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

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	public SpoolingRule getSpoolingRule() {
		return spoolingRule;
	}

	public void setSpoolingRule(SpoolingRule spoolingRule) {
		this.spoolingRule = spoolingRule;
	}
	
}
