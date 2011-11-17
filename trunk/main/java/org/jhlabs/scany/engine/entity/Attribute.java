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

import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.jhlabs.scany.engine.search.summarize.Summarizer;

/**
 * 컬럼의 속성
 * 
 * @author Gulendol
 *
 */
public class Attribute {

	private String name;
	
	/**
	 * 원본 내용의 저장 여부
	 */
	private boolean storable;
	
	/**
	 * 원본 내용의 압축해서 저장할지 여부.
	 * 긴 문서 내용이나 바이너리 데이터에 유용. 
	 */
	private boolean compressable;
	
	/**
	 * 색인 생성 여부.
	 * 색인이 생성되어야 질의도 가능하다.
	 */
	private boolean indexable;

	/**
	 * 색인 데이터의 토큰 분리 여부
	 */
	private boolean tokenizable;
	
	/**
	 * 통합 질의에 포함할 컬럼인지 여부
	 */
	private boolean queryable;
	
	/**
	 * PrefixQuery 를 사용해야 하는지 여부
	 * <pre>
	 * 다음에 해당하는 경우 PrefixQuery가 적용된다.
	 *   1. 한글: 1자일 경우
	 *   2. 영문: 4자 이상인 경우
	 * </pre>
	 */
	private boolean prefixQueryable;
	
	private String description;
	
	private Analyzer analyzer;
	
	private Summarizer summarizer;
	
	private float boost;

	/**
	 * @return the boost
	 */
	public float getBoost() {
		return boost;
	}

	/**
	 * @param boost the boost to set
	 */
	public void setBoost(float boost) {
		this.boost = boost;
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
	 * @return the isIndexed
	 */
	public boolean isIndexable() {
		return indexable;
	}

	/**
	 * @param indexable the isIndexable to set
	 */
	public void setIndexable(boolean indexable) {
		this.indexable = indexable;
	}

	/**
	 * @return the isQueryable
	 */
	public boolean isQueryable() {
		return queryable;
	}

	/**
	 * @param queryable the isQueryable to set
	 */
	public void setQueryable(boolean queryable) {
		this.queryable = queryable;
	}

	/**
	 * @return the isPrefixQueryable
	 */
	public boolean isPrefixQueryable() {
		return prefixQueryable;
	}

	/**
	 * @param prefixQueryable the isPrefixQueryable to set
	 */
	public void setPrefixQueryable(boolean prefixQueryable) {
		this.prefixQueryable = prefixQueryable;
	}

	/**
	 * @return the isStorable
	 */
	public boolean isStorable() {
		return storable;
	}

	/**
	 * @param storable the isStorable to set
	 */
	public void setStorable(boolean storable) {
		this.storable = storable;
	}
	
	/**
	 * @return the isCompressable
	 */
	public boolean isCompressable() {
		return compressable;
	}

	/**
	 * @param compressable the isCompressable to set
	 */
	public void setCompressable(boolean compressable) {
		this.compressable = compressable;
	}

	/**
	 * @return the isTokenizable
	 */
	public boolean isTokenizable() {
		return tokenizable;
	}

	/**
	 * @param tokenizable the isTokenizable to set
	 */
	public void setTokenizable(boolean tokenizable) {
		this.tokenizable = tokenizable;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * 컬럼의 내용을 분석기에 분석된 토큰스트림을 반환한다. 
	 * @param reader
	 * @return
	 */
	public TokenStream makeTokenStream(Reader reader) {
		return analyzer.tokenStream(name, reader);
	}
	
	public Summarizer getSummarizer() {
		return summarizer;
	}

	public void setSummarizer(Summarizer summarizer) {
		this.summarizer = summarizer;
	}

	/**
	 * 컬럼의 내용을 분석기에 분석된 토큰스트림을 반환한다. 
	 * @param value Text
	 * @return
	 */
	public TokenStream makeTokenStream(String value) {
		return analyzer.tokenStream(name, new StringReader(value));
	}
	
}
