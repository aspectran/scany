package org.jhlabs.scany.context.rule;

import org.jhlabs.scany.context.type.ServiceMode;

public class ClientRule {

	private ServiceMode serviceMode;
	
	private Object anyServiceRule;

	public ServiceMode getServiceMode() {
		return serviceMode;
	}

	public void setServiceMode(ServiceMode serviceMode) {
		this.serviceMode = serviceMode;
	}

	public Object getAnyServiceRule() {
		return anyServiceRule;
	}

	public void setAnyServiceRule(Object anyServiceRule) {
		this.anyServiceRule = anyServiceRule;
	}
	
}
