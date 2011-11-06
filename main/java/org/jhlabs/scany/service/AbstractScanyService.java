/**
 * 
 */
package org.jhlabs.scany.service;

import org.jhlabs.scany.context.rule.ServiceRule;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 6. 오후 4:46:36</p>
 *
 */
public abstract class AbstractScanyService {
	
	private ServiceRule serviceRule;

	public AbstractScanyService(ServiceRule serviceRule) {
		this.serviceRule = serviceRule;
	}
	
}
