package org.jhlabs.scany.context.rule;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jhlabs.scany.engine.entity.Schema;

public class HttpServiceRule {

	private Schema schema;

	private String schemaConfigLocation;

	private String characterEncoding;
	
	private String url;
	
	private Map<String, String> parameters;

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

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public String getParamter(String name) {
		return parameters.get(name);
	}
	
	public String setParameter(String name, String value) {
		return parameters.put(name, value);
	}
	
}
