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
import java.util.Iterator;
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
public class SortAttribute {

	private String attributeName;
	
	private SortFieldType sortFieldType;
	
	private boolean reverse;

	public SortAttribute(String attributeName, SortFieldType sortFieldType, boolean reverse) {
		this.attributeName = attributeName;
		this.sortFieldType = sortFieldType;
		this.reverse = reverse;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public SortFieldType getSortFieldType() {
		return sortFieldType;
	}

	public boolean isReverse() {
		return reverse;
	}

	public static Sort makeSort(List<SortAttribute> sortAttributeList) {
		List<SortField> sortFieldList = new ArrayList<SortField>();
		
		Iterator<SortAttribute> iter = sortAttributeList.iterator();
		
		while(iter.hasNext()) {
			SortAttribute sortAttribute = iter.next();
			
			SortField sortFiled = new SortField(sortAttribute.getAttributeName(), (Integer)sortAttribute.getSortFieldType().getType(), sortAttribute.isReverse());
			
			sortFieldList.add(sortFiled);
		}
		
		SortField[] sortFileds = (SortField[])sortFieldList.toArray(new SortField[sortFieldList.size()]);
		Sort sort = new Sort(sortFileds);

		return sort;
	}
	
}
