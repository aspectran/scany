package org.jhlabs.scany.service.remote.http;

import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.transaction.AbstractTransaction;

public class RemoteHttpTransaction extends AbstractTransaction {

	private Relation table;

	public RemoteHttpTransaction(Relation table) {
		this.table = table;
	}
	
	public void commit() {
		
	}

	public void rollback() {
		
	}
}

