package org.jhlabs.scany.engine.entity;

import java.util.ArrayList;

public class RecordList extends ArrayList<Record> implements java.io.Serializable {

	private static final long serialVersionUID = -8647558139113525196L;
	
	public RecordList() {
		super();
	}

	public RecordList(int initialCapacity) {
		super(initialCapacity);
	}
	
}
