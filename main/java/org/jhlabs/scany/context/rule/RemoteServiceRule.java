package org.jhlabs.scany.context.rule;

import org.jhlabs.scany.engine.entity.Schema;

public class RemoteServiceRule {

	private Schema schema;
	
	private String host;
	
	private int timeout;
	
	private String authentificationKey;
	
	private String messageEncryption;
	
	private Boolean messageCompressable;
}
