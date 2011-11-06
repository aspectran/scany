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
 * 하나의 필터컬럼 정보를 담고 있다.
 * 
 * <p>Created: 2007. 01. 22 오전 3:07:50</p>
 * 
 * @author Gulendol
 *
 */
public class FilterColumn {
	
	private String columnName;
	
	private String keyword;
	
	/**
	 * 반드시 발생해야 하는 필수항목 여부
	 */
	private boolean isEssentialClause = true;
	
	/**
	 * FilterColumn을 생성한다.
	 * @param columnName 컬럼명
	 * @param keyword 키워드
	 * @param isEssentialClause 필수항목 여부
	 */
	public FilterColumn(String columnName, String keyword, boolean isEssentialClause) {
		this.columnName = columnName;
		this.keyword = keyword;
		this.isEssentialClause = isEssentialClause;
	}
	
	/**
	 * 컬럼명을 반환한다.
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
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
	public boolean isEssentialClause() {
		return isEssentialClause;
	}
	
}
