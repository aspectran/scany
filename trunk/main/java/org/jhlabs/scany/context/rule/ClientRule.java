package org.jhlabs.scany.context.rule;

import org.jhlabs.scany.context.type.RemoteMode;
import org.jhlabs.scany.context.type.ServiceMode;

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
	
}
