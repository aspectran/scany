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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanClause;
import org.jhlabs.scany.engine.entity.Attribute;


/**
 * 정렬 컬럼을 지정하는 역할을 한다. 
 * 
 * <p>Created: 2007. 01. 19 오전 1:18:30</p>
 * 
 * @author Gulendol
 *
 */
public class QueryAttribute {

	private String attributeName;
	
	private String keyword;
	
	private BooleanClause.Occur booleanClauseOccur;
	
	private String analyzerId;
	
	private Analyzer analyzer;
	
	private Attribute attribute;

	public QueryAttribute() {
	}
	
	public QueryAttribute(String attributeName, String analyzerId) {
		this.attributeName = attributeName;
		this.analyzerId = analyzerId;
	}

	public QueryAttribute(String attributeName, String analyzerId, String keyword) {
		this.attributeName = attributeName;
		this.analyzerId = analyzerId;
		this.keyword = keyword;
	}
	
	public QueryAttribute(String attributeName, String analyzerId, String keyword, BooleanClause.Occur booleanClauseOccur) {
		this.attributeName = attributeName;
		this.analyzerId = analyzerId;
		this.keyword = keyword;
		this.booleanClauseOccur = booleanClauseOccur;
	}
	
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public BooleanClause.Occur getBooleanClauseOccur() {
		return booleanClauseOccur;
	}

	public void setBooleanClauseOccur(BooleanClause.Occur booleanClauseOccur) {
		this.booleanClauseOccur = booleanClauseOccur;
	}

	public String getAnalyzerId() {
		return analyzerId;
	}

	public void setAnalyzerId(String analyzerId) {
		this.analyzerId = analyzerId;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	protected void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	protected Attribute getAttribute() {
		return attribute;
	}

	protected void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	
}
