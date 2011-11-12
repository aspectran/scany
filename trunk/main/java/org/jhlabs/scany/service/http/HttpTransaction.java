package org.jhlabs.scany.service.http;

import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.transaction.AbstractTransaction;

public class HttpTransaction extends AbstractTransaction {

	private Relation table;

	public HttpTransaction(Relation table) {
		this.table = table;
	}
	
	public void commit() {
		
	}

	public void rollback() {
		
	}
}

