package org.jhlabs.scany.context.rule;

import org.jhlabs.scany.context.type.RemoteMode;
import org.jhlabs.scany.context.type.ServiceMode;
import org.jhlabs.scany.engine.entity.Schema;

public class ClientRule {

	private ServiceMode serviceMode;
	
	private RemoteMode remoteMode;
	
	private Object anyServiceRule;

	public ServiceMode getServiceMode() {
		return serviceMode;
	}

	public void setServiceMode(ServiceMode serviceMode) {
		this.serviceMode = serviceMode;
	}

	public RemoteMode getRemoteMode() {
		return remoteMode;
	}

	public void setRemoteMode(RemoteMode remoteMode) {
		this.remoteMode = remoteMode;
	}

	public Object getAnyServiceRule() {
		return anyServiceRule;
	}

	public void setAnyServiceRule(Object anyServiceRule) {
		this.anyServiceRule = anyServiceRule;
	}
	
	public String getSchemaConfigLocation() {
		String schemaConfigLocation = null;
		
		if(anyServiceRule != null) {
			if(serviceMode == ServiceMode.LOCAL) {
				schemaConfigLocation = ((LocalServiceRule)anyServiceRule).getSchemaConfigLocation();
			} else if(serviceMode == ServiceMode.REMOTE) {
				if(remoteMode == RemoteMode.TCP) {
					schemaConfigLocation = ((RemoteTcpServiceRule)anyServiceRule).getSchemaConfigLocation();
				} else if(remoteMode == RemoteMode.HTTP) {
					schemaConfigLocation = ((RemoteHttpServiceRule)anyServiceRule).getSchemaConfigLocation();
				}
			}
		}
		
		return schemaConfigLocation;
	}
	
	public Schema getSchema() {
		Schema schema = null;
		
		if(anyServiceRule != null) {
			if(serviceMode == ServiceMode.LOCAL) {
				schema = ((LocalServiceRule)anyServiceRule).getSchema();
			} else if(serviceMode == ServiceMode.REMOTE) {
				if(remoteMode == RemoteMode.TCP) {
					schema = ((RemoteTcpServiceRule)anyServiceRule).getSchema();
				} else if(remoteMode == RemoteMode.HTTP) {
					schema = ((RemoteHttpServiceRule)anyServiceRule).getSchema();
				}
			}
		}
		
		return schema;
	}
	
	public void setSchema(Schema schema) {
		if(anyServiceRule != null) {
			if(serviceMode == ServiceMode.LOCAL) {
				((LocalServiceRule)anyServiceRule).setSchema(schema);
			} else if(serviceMode == ServiceMode.REMOTE) {
				if(remoteMode == RemoteMode.TCP) {
					((RemoteTcpServiceRule)anyServiceRule).setSchema(schema);
				} else if(remoteMode == RemoteMode.HTTP) {
					((RemoteHttpServiceRule)anyServiceRule).setSchema(schema);
				}
			}
		}
	}
	
}
