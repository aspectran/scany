package org.jhlabs.scany.context.rule;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jhlabs.scany.engine.entity.Schema;

public class HttpServiceRule {

	private Schema schema;
	
	private String characterEncoding;
	
	private String url;
	
	private Map<String, String> parameters = new LinkedHashMap<String, String>();
	
}
