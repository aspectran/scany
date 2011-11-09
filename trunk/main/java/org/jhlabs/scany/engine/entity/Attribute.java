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
	private boolean isStorable;
	
	/**
	 * 원본 내용의 압축해서 저장할지 여부.
	 * 긴 문서 내용이나 바이너리 데이터에 유용. 
	 */
	private boolean isCompressable;
	
	/**
	 * 색인 생성 여부.
	 * 색인이 생성되어야 질의도 가능하다.
	 */
	private boolean isIndexable;

	/**
	 * 색인 데이터의 토큰 분리 여부
	 */
	private boolean isTokenizable;
	
	/**
	 * 통합 질의에 포함할 컬럼인지 여부
	 */
	private boolean isQueryable;
	
	/**
	 * PrefixQuery 를 사용해야 하는지 여부
	 * <pre>
	 * 다음에 해당하는 경우 PrefixQuery가 적용된다.
	 *   1. 한글: 1자일 경우
	 *   2. 영문: 4자 이상인 경우
	 * </pre>
	 */
	private boolean isPrefixQueryable;
	
	private String description;
	
	private Analyzer analyzer;
	
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
		return isIndexable;
	}

	/**
	 * @param isIndexable the isIndexable to set
	 */
	public void setIndexable(boolean isIndexable) {
		this.isIndexable = isIndexable;
	}

	/**
	 * @return the isQueryable
	 */
	public boolean isQueryable() {
		return isQueryable;
	}

	/**
	 * @param isQueryable the isQueryable to set
	 */
	public void setQueryable(boolean isQueryable) {
		this.isQueryable = isQueryable;
	}

	/**
	 * @return the isPrefixQueryable
	 */
	public boolean isPrefixQueryable() {
		return isPrefixQueryable;
	}

	/**
	 * @param isPrefixQueryable the isPrefixQueryable to set
	 */
	public void setPrefixQueryable(boolean isPrefixQueryable) {
		this.isPrefixQueryable = isPrefixQueryable;
	}

	/**
	 * @return the isStorable
	 */
	public boolean isStorable() {
		return isStorable;
	}

	/**
	 * @return the isCompressable
	 */
	public boolean isCompressable() {
		return isCompressable;
	}

	/**
	 * @param isCompressable the isCompressable to set
	 */
	public void setCompressable(boolean isCompressable) {
		this.isCompressable = isCompressable;
	}

	/**
	 * @param isStorable the isStorable to set
	 */
	public void setStorable(boolean isStorable) {
		this.isStorable = isStorable;
	}

	/**
	 * @return the isTokenizable
	 */
	public boolean isTokenizable() {
		return isTokenizable;
	}

	/**
	 * @param isTokenizable the isTokenizable to set
	 */
	public void setTokenizable(boolean isTokenizable) {
		this.isTokenizable = isTokenizable;
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
	
	/**
	 * 컬럼의 내용을 분석기에 분석된 토큰스트림을 반환한다. 
	 * @param value Text
	 * @return
	 */
	public TokenStream makeTokenStream(String value) {
		return analyzer.tokenStream(name, new StringReader(value));
	}
	
}
