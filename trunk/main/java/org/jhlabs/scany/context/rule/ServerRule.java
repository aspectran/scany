package org.jhlabs.scany.context.rule;

public class ServerRule {

	private RemoteServiceRule remoteServiceRule;
	
	private OptimizingRule optimizingRule;

	public RemoteServiceRule getRemoteServiceRule() {
		return remoteServiceRule;
	}

	public void setRemoteServiceRule(RemoteServiceRule remoteServiceRule) {
		this.remoteServiceRule = remoteServiceRule;
	}

	public OptimizingRule getOptimizingRule() {
		return optimizingRule;
	}

	public void setOptimizingRule(OptimizingRule optimizingRule) {
		this.optimizingRule = optimizingRule;
	}
	
}
