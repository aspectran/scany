package org.jhlabs.scany.context.rule;

import org.jhlabs.scany.context.type.SpoolingMode;

public class SpoolingRule {

	private SpoolingMode spoolingMode;
	
	private Object spoolTransactionRule;

	public SpoolingMode getSpoolingMode() {
		return spoolingMode;
	}

	public void setSpoolingMode(SpoolingMode spoolingMode) {
		this.spoolingMode = spoolingMode;
	}

	public Object getAnySpoolingRule() {
		return spoolTransactionRule;
	}

	public void setSpoolTransactionRule(Object anySpoolingRule) {
		this.spoolTransactionRule = anySpoolingRule;
	}
	
}
