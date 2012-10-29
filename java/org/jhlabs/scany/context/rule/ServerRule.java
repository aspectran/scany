package org.jhlabs.scany.context.rule;


public class ServerRule {

	private RemoteTcpServiceRule remoteTcpServiceRule;
	
	private OptimizingRule optimizingRule;

	public RemoteTcpServiceRule getRemoteTcpServiceRule() {
		return remoteTcpServiceRule;
	}

	public void setRemoteTcpServiceRule(RemoteTcpServiceRule remoteServiceRule) {
		this.remoteTcpServiceRule = remoteServiceRule;
	}

	public OptimizingRule getOptimizingRule() {
		return optimizingRule;
	}

	public void setOptimizingRule(OptimizingRule optimizingRule) {
		this.optimizingRule = optimizingRule;
	}
	
}
