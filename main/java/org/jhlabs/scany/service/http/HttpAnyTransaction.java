package org.jhlabs.scany.service.http;

import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.transaction.AbstractAnyTransaction;

public class HttpAnyTransaction extends AbstractAnyTransaction {

	private Relation table;

	public HttpAnyTransaction(Relation table) {
		this.table = table;
	}
	
	public void commit() {
		
	}

	public void rollback() {
		
	}
}

