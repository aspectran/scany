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


import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.jhlabs.scany.context.type.DirectoryType;
import org.jhlabs.scany.engine.index.RecordKeyException;

/**
 * 스키마 정보를 담고 있다.
 * 
 * @author Gulendol
 *
 */
public class Relation {

	private Schema schema;

	private String id;
	
	private RecordKeyPattern recordKeyPattern;
	
	private DirectoryType directoryType;
	
	private String directory;
	
	private String description;

	private AttributeMap attributeMap;
	
	private String analyzerId;
	
	private Analyzer analyzer;
	
	private Analyzer perFieldAnalyzer;
	
	private int mergeFactor;
	
	private int maxMergeDocs;

	public Relation() {
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
	public String getId() {
		return id;
	}

	/**
	 * 스키마 ID를 지정한다.
	 * @param id the schemaId to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public DirectoryType getDirectoryType() {
		return directoryType;
	}

	public void setDirectoryType(DirectoryType directoryType) {
		this.directoryType = directoryType;
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

	public String getAnalyzerId() {
		return analyzerId;
	}

	public void setAnalyzerId(String analyzerId) {
		this.analyzerId = analyzerId;
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

	public Analyzer getPerFieldAnalyzer() {
		return perFieldAnalyzer;
	}

	public void setPerFieldAnalyzer(Analyzer perFieldAnalyzer) {
		this.perFieldAnalyzer = perFieldAnalyzer;
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

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	/**
	 * @param maxMergeDocs the maxMergeDocs to set
	 */
	public void setMaxMergeDocs(int maxMergeDocs) {
		this.maxMergeDocs = maxMergeDocs;
	}
	
	public Directory openDirectory() throws IOException {
		Directory directory = null;
		
		if(directoryType == DirectoryType.FS) {
			directory = FSDirectory.open(new File(this.directory));
		} else if(directoryType == DirectoryType.FS) {
			if(this.directory == null)
				directory = new RAMDirectory();
			else {
				directory = new RAMDirectory(FSDirectory.open(new File(this.directory)));
			}
		}
		
		return directory;
	}

}
