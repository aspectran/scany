package org.jhlabs.scany.service.remote.http;

import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.transaction.AbstractTransaction;

public class RemoteHttpTransaction extends AbstractTransaction {

	public RemoteHttpTransaction(Relation relation) {
		super(relation);
	}
	
	public void commit() {
		
	}

	public void rollback() {
		
	}
}

