/*******************************************************************************
 * Copyright (c) 2008 Jeong Ju Ho.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Jeong Ju Ho - initial API and implementation
 ******************************************************************************/
package org.jhlabs.scany.engine.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

/**
 * 정렬 컬럼을 지정하는 역할을 한다. 
 * 
 * <p>Created: 2007. 01. 19 오전 1:18:30</p>
 * 
 * @author Gulendol
 *
 */
public class SortingAttributes {

	public final int SCORE = SortField.SCORE;
	
	public static final int DOC = SortField.DOC;
	
	public static final int AUTO = SortField.AUTO;
	
	public static final int STRING = SortField.STRING;

	public static final int INT = SortField.INT;

	public static final int FLOAT = SortField.FLOAT;
	
	private List sortFields;

	/**
	 * 정렬 컬럼을 추가한다.
	 * @param columnName 컬럼명
	 * @param type 정렬 컬럼 타입
	 * @param reverse 역정렬 여부
	 */
	public void addColumn(String columnName, int type, boolean reverse) {		
		if(sortFields == null)
			sortFields = new ArrayList();
		
		sortFields.add(new SortField(columnName, type, reverse));
	}

	/**
	 * Sort 오브젝트를 반환한다.
	 * @return
	 */
	public Sort getSort() {
		if(sortFields == null || sortFields.size() == 0)
			return new Sort();

		SortField[] sf = (SortField[])sortFields.toArray(new SortField[sortFields.size()]);
		Sort sort = new Sort(sf); 
		
		return sort;
	}

	/**
	 * 정렬 컬럼명들을 반환한다.
	 * @return
	 */
	public String[] getColumnNames() {
		String[] columnNames = new String[sortFields.size()];
		
		for(int i = 0; i < columnNames.length; i++) {
			SortField sortField = (SortField)sortFields.get(i);
			
			columnNames[i] = sortField.getField();
		}
		
		return columnNames;
	}
	
}
