package org.jhlabs.scany.service.http;

import org.jhlabs.scany.engine.entity.Table;
import org.jhlabs.scany.engine.transaction.AbstractAnyTransaction;

public class HttpAnyTransaction extends AbstractAnyTransaction {

	private Table table;

	public HttpAnyTransaction(Table table) {
		this.table = table;
	}
	
	public void commit() {
		
	}

	public void rollback() {
		
	}
}

