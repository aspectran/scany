package org.jhlabs.scany.service.remote.tcp;

import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.transaction.AbstractTransaction;

public class RemoteTcpTransaction extends AbstractTransaction {

	private Relation table;

	public RemoteTcpTransaction(Relation table) {
		this.table = table;
	}
	
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	public void rollback() {
		// TODO Auto-generated method stub
		
	}
}

