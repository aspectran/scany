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

/**
 * 스키마 정보를 담고 있다.
 * 
 * @author Gulendol
 *
 */
public class Relation {

	private String id;
	
	private RecordKeyPattern recordKeyPattern;
	
	private String directory;
	
	private String description;

	private AttributeMap attributeMap;
	
	private Analyzer analyzer;
	
	private int mergeFactor;
	
	private int maxMergeDocs;

	public Relation() {
		attributeMap = new AttributeMap();
	}
	
	public RecordKey newRecordKey() {
		return new RecordKey(recordKeyPattern);
	}
	
	public RecordKey newRecordKey(String recordKeyString) throws RecordKeyException {
		RecordKey recordKey = new RecordKey(recordKeyPattern);
		recordKey.setRecordKeyString(recordKeyString);
		return recordKey;
	}
	
	/**
	 * 해당하는 이름을 가진 컬럼을 반환한다.
	 * @param attributeName 컬럼명
	 * @return
	 */
	public Attribute getAttribute(String attributeName) {
		return attributeMap.get(attributeName);
	}
	
	/**
	 * 컬럼을 반환한다.
	 * @return the columns
	 */
	public AttributeMap getAttributeMap() {
		return attributeMap;
	}

	/**
	 * 컬럼을 지정한다.
	 * @param columns the columns to set
	 */
	public void setAttributeMap(AttributeMap attributeMap) {
		this.attributeMap = attributeMap;
	}

	/**
	 * PrimaryKey의 형식을 반환한다.
	 * @return the keyFormat
	 */
	public RecordKeyPattern getRecordKeyPattern() {
		return recordKeyPattern;
	}

	/**
	 * PrimaryKey의 형식을 지정한다.
	 * @param keyFormat the keyFormat to set
	 */
	public void setRecordKeyPattern(RecordKeyPattern recordKeyPattern) {
		this.recordKeyPattern = recordKeyPattern;
	}

	/**
	 * 스키마 ID를 반환한다.
	 * @return the schemaId
	 */
	public String getRelationId() {
		return id;
	}

	/**
	 * 스키마 ID를 지정한다.
	 * @param id the schemaId to set
	 */
	public void setRelationId(String id) {
		this.id = id;
	}

	/**
	 * @return the repository
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * @param directory the repository to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
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
