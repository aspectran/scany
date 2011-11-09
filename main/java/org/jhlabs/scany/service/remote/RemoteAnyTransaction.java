package org.jhlabs.scany.service.remote;

import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.transaction.AbstractAnyTransaction;

public class RemoteAnyTransaction extends AbstractAnyTransaction {

	private Relation table;

	public RemoteAnyTransaction(Relation table) {
		this.table = table;
	}
	
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	public void rollback() {
		// TODO Auto-generated method stub
		
	}
}

