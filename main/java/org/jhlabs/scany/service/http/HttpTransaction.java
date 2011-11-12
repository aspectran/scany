package org.jhlabs.scany.service.http;

import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.transaction.AbstractAnyTransaction;

public class HttpTransaction extends AbstractAnyTransaction {

	private Relation table;

	public HttpTransaction(Relation table) {
		this.table = table;
	}
	
	public void commit() {
		
	}

	public void rollback() {
		
	}
}

