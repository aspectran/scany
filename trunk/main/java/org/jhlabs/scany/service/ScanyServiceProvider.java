package org.jhlabs.scany.service;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jhlabs.scany.context.ScanyContext;
import org.jhlabs.scany.context.ScanyContextException;
import org.jhlabs.scany.context.builder.ScanyContextBuilder;
import org.jhlabs.scany.context.rule.ClientRule;
import org.jhlabs.scany.context.rule.LocalServiceRule;
import org.jhlabs.scany.context.type.ServiceMode;
import org.jhlabs.scany.service.local.LocalService;

public class ScanyServiceProvider {
	
	private final ScanyContext scanyContext;
	
	public ScanyServiceProvider(String configLocation) throws FileNotFoundException {
		this(ScanyContextBuilder.build(configLocation));
	}

	public ScanyServiceProvider(InputStream is) {
		this(ScanyContextBuilder.build(is));
	}
	
	public ScanyServiceProvider(ScanyContext scanyContext) {
		this.scanyContext = scanyContext;
	}

	public AnyService getAnyService() {
		if(scanyContext.getLocalServiceRule() == null)
			throw new ScanyContextException("Scany's local service is not defined.");

		AnyService anyService = null;
		
		if(scanyContext.getClientRule() == null) {
			anyService = new LocalService(scanyContext.getLocalServiceRule());
		} else {
			ClientRule clientRule = scanyContext.getClientRule();
			ServiceMode serviceMode = clientRule.getServiceMode();
			
			if(serviceMode == ServiceMode.LOCAL) {
				anyService = new LocalService((LocalServiceRule)clientRule.getAnyServiceRule());
			} else {
				throw new ScanyContextException("Sorry! Scany " + serviceMode + " service is not support.");
			}
		}
		
		return anyService;
	}

}
