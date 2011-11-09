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
package org.jhlabs.scany.engine.entity;


import org.apache.lucene.analysis.Analyzer;

import org.jhlabs.scany.engine.entity.Attribute;

/**
 * 스키마 정보를 담고 있다.
 * 
 * @author Gulendol
 *
 */
public class Relation {

	private String schemaId;
	
	private String keyFormat;
	
	private String repository;
	
	private String description;

	private Attribute[] columns;
	
	private Analyzer analyzer;
	
	private boolean expertQueryMode;

	private int mergeFactor;
	
	private int maxMergeDocs;
	
	
	/**
	 * 해당하는 이름을 가진 컬럼을 반환한다.
	 * @param columnName 컬럼명
	 * @return
	 */
	public Attribute getColumn(String columnName) {
		if(columns == null)
			return null;
		
		for(int i = 0; i < columns.length; i++) {
			if(columnName.equalsIgnoreCase(columns[i].getName()))
				return columns[i];
		}
		
		return null;
	}
	
	/**
	 * 컬럼을 반환한다.
	 * @return the columns
	 */
	public Attribute[] getColumns() {
		return columns;
	}

	/**
	 * 컬럼을 지정한다.
	 * @param columns the columns to set
	 */
	public void setColumns(Attribute[] columns) {
		this.columns = columns;
	}

	/**
	 * PrimaryKey의 형식을 반환한다.
	 * @return the keyFormat
	 */
	public String getKeyPattern() {
		return keyFormat;
	}

	/**
	 * PrimaryKey의 형식을 지정한다.
	 * @param keyFormat the keyFormat to set
	 */
	public void setKeyPattern(String keyFormat) {
		this.keyFormat = keyFormat;
	}

	/**
	 * 스키마 ID를 반환한다.
	 * @return the schemaId
	 */
	public String getSchemaId() {
		return schemaId;
	}

	/**
	 * 스키마 ID를 지정한다.
	 * @param schemaId the schemaId to set
	 */
	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	/**
	 * @return the repository
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(String repository) {
		this.repository = repository;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the analyzer
	 */
	public Analyzer getAnalyzer() {
		return analyzer;
	}

	/**
	 * @param analyzer the analyzer to set
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * @return the expertQueryMode
	 */
	public boolean isExpertQueryMode() {
		return expertQueryMode;
	}

	/**
	 * @param expertQueryMode the expertQueryMode to set
	 */
	public void setExpertQueryMode(boolean expertQueryMode) {
		this.expertQueryMode = expertQueryMode;
	}

	/**
	 * @return the mergeFactor
	 */
	public int getMergeFactor() {
		return mergeFactor;
	}

	/**
	 * @param mergeFactor the mergeFactor to set
	 */
	public void setMergeFactor(int mergeFactor) {
		this.mergeFactor = mergeFactor;
	}

	/**
	 * @return the maxMergeDocs
	 */
	public int getMaxMergeDocs() {
		return maxMergeDocs;
	}

	/**
	 * @param maxMergeDocs the maxMergeDocs to set
	 */
	public void setMaxMergeDocs(int maxMergeDocs) {
		this.maxMergeDocs = maxMergeDocs;
	}
	
}
