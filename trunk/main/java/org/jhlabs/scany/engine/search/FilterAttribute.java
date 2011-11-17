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


/**
 * 하나의 필터컬럼 정보를 담는다.
 * 
 * <p>Created: 2007. 01. 22 오전 3:07:50</p>
 * 
 * @author Gulendol
 *
 */
public class FilterAttribute {
	
	private String attributeName;
	
	private String keyword;
	
	/**
	 * 반드시 발생해야 하는 필수항목 여부
	 */
	private boolean essential = true;
	
	/**
	 * FilterColumn을 생성한다.
	 * @param columnName 컬럼명
	 * @param keyword 키워드
	 * @param essential 필수항목 여부
	 */
	public FilterAttribute(String attributeName, String keyword, boolean essential) {
		this.attributeName = attributeName;
		this.keyword = keyword;
		this.essential = essential;
	}
	
	/**
	 * 컬럼명을 반환한다.
	 * @return the columnName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * 키워드를 반환한다.
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * 필수항목인지 여부를 반환한다.
	 * @return the isEssentialClause
	 */
	public boolean isEssential() {
		return essential;
	}
	
}
