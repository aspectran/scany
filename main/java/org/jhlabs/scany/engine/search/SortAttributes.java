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
public class SortAttributes {

	private List<SortField> sortFieldList;

	/**
	 * 정렬 컬럼을 추가한다.
	 * @param attributeName 컬럼명
	 * @param type 정렬 컬럼 타입
	 * @param reverse 역정렬 여부
	 */
	public void addAttribute(String attributeName, SortFieldType sortFieldType, boolean reverse) {		
		if(sortFieldList == null)
			sortFieldList = new ArrayList<SortField>();
		
		sortFieldList.add(new SortField(attributeName, (Integer)sortFieldType.getType(), reverse));
	}

	/**
	 * Sort 오브젝트를 반환한다.
	 * @return
	 */
	public Sort getSort() {
		if(sortFieldList == null || sortFieldList.size() == 0)
			return new Sort();

		SortField[] sf = (SortField[])sortFieldList.toArray(new SortField[sortFieldList.size()]);
		Sort sort = new Sort(sf); 
		
		return sort;
	}

	/**
	 * 정렬 컬럼명들을 반환한다.
	 * @return
	 */
	public String[] getAttributeNames() {
		String[] attributeNames = new String[sortFieldList.size()];
		
		for(int i = 0; i < attributeNames.length; i++) {
			SortField sortField = (SortField)sortFieldList.get(i);
			
			attributeNames[i] = sortField.getField();
		}
		
		return attributeNames;
	}
	
}
