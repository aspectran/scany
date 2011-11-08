package org.jhlabs.scany.service.local;

import org.jhlabs.scany.engine.entity.Table;
import org.jhlabs.scany.service.AbstractAnyTransaction;

public class LocalAnyTransaction extends AbstractAnyTransaction {

	public LocalAnyTransaction(Table table) {
		super(table);
	}

	public void commit() {
		// TODO Auto-generated method stub
		
	}

	public void rollback() {
		// TODO Auto-generated method stub
		
	}
}

