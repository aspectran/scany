package org.jhlabs.scany.service.remote;

import org.jhlabs.scany.engine.entity.Table;
import org.jhlabs.scany.engine.transaction.AbstractAnyTransaction;

public class RemoteAnyTransaction extends AbstractAnyTransaction {

	private Table table;

	public RemoteAnyTransaction(Table table) {
		this.table = table;
	}
	
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	public void rollback() {
		// TODO Auto-generated method stub
		
	}
}

