package org.jhlabs.scany;

import org.jhlabs.scany.service.AnyService;
import org.jhlabs.scany.service.ScanyServiceProvider;

public class ScanyServiceProviderFactory {
	
	private static final ScanyServiceProvider scanyServiceProvider;

	static {
		try{
			final String configLocation = "/c:/ADE_JHLabs/workspace/scany-test/conf/scany.xml";
			scanyServiceProvider = new ScanyServiceProvider(configLocation);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error initializing ScanyServiceProvider class", e);
		}
	}

	public static AnyService getService() {
		return scanyServiceProvider.getAnyService();
	}
}
