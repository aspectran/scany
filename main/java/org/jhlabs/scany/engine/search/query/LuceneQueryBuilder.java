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
package org.jhlabs.scany.engine.search.query;

import org.jhlabs.scany.context.builder.ScanyContextBuilder;
import org.jhlabs.scany.engine.entity.Attribute;
import org.jhlabs.scany.engine.search.FilteringAttributes;
import org.jhlabs.scany.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

/**
 * 검색 조항을 모두 조합한 후 하나의 Query를 반환한다.
 * 
 * <pre>
 * 현재 Scany에서 지정할 수 있는 검색 조항
 * QueryColumn - 질의문 분석 후 해당 컬럼을 검색
 * FilterColumn - 질의문 분석 없이 특정 값을 가진 컬럼을 가려냄 
 * </pre>
 * 
 * <p>
 * Created: 2007. 01. 31 오전 7:09:01
 * </p>
 * 
 * @author Gulendol
 */
public class LuceneQueryBuilder {

	private Analyzer analyzer;
	
	/**
	 * 필터 대상 컬럼
	 */
	private FilteringAttributes[] filterColumns;

	/**
	 * 질의 대상 컬럼
	 */
	private Attribute[] queryColumns;

	//private String queryString;

	/**
	 * Constructs
	 * @param analyzer 분석기
	 * @param isExpertQueryMode 전문가용 질의문법 사용여부
	 */
	public LuceneQueryBuilder(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	/**
	 * 필터컬럼을 지정한다.
	 * 
	 * @param filterColumns
	 */
	public void setFilterColumns(FilteringAttributes[] filterColumns) {
		this.filterColumns = filterColumns;
	}

	/**
	 * 쿼리컬럼과 질의문을 지정한다.
	 * 
	 * @param queryColumns 질의 대상 컬럼
	 */
	public void setQeuryColumns(Attribute[] queryColumns) {
		this.queryColumns = queryColumns;
	}

	/**
	 * 질의를 분석한다.
	 * 
	 * @param queryString
	 * @return
	 * @throws MultipartRequestzException
	 */
	public Query getQuery(String queryString) throws QueryBuilderException {

		try {
			List queries = new ArrayList();

			// 필터쿼리
			Query filterQuery = getFilterQuery();

			if(filterQuery != null)
				queries.add(filterQuery);

			// 컬럼쿼리
			if(!StringUtils.isEmpty(queryString)) {
				Query parsedQuery = stringToQuery(queryString);

				if(parsedQuery != null)
					queries.add(parsedQuery);
			}

			if(queries.size() == 0)
				throw new IllegalArgumentException("지정된 검색 조건이 없습니다. 검색 조건을 지정하세요.");

			if(queries.size() == 1) {
				// System.out.println(queries.toString());
				return (Query)queries.get(0);
			}

			BooleanQuery booleanQuery = new BooleanQuery();

			for(int i = 0; i < queries.size(); i++) {
				booleanQuery.add((Query)queries.get(i), BooleanClause.Occur.MUST);
			}

			//System.out.println(booleanQuery.toString());

			return booleanQuery;

		} catch(Exception e) {
			throw new QueryBuilderException("질의 조합에 실패했습니다.", e);
		}
	}

	/**
	 * 필터 쿼리를 만든다.
	 * 필터링에 기준이 되는 값(키워드)은 파싱을 하지 않는 것이 특징이다.
	 * 내부적으로 TermQuery 또는 WildcardQuery 를 이용한다.
	 * 
	 * @param queryString
	 * @return
	 */
	private Query getFilterQuery() {
		if(filterColumns == null)
			return null;

		List queries = new ArrayList();

		Query query = null;
		Term term = null;

		for(int i = 0; i < filterColumns.length; i++) {
			String columnName = filterColumns[i].getColumnName();
			String keyword = filterColumns[i].getKeyword();

			term = new Term(columnName, keyword);

			if(keyword.indexOf('?') != -1 || keyword.indexOf('*') != -1) {
				query = new WildcardQuery(term);
			} else {
				query = new TermQuery(term);
			}

			queries.add(query);
		}
		
		if(queries.size() == 1)
			return (Query)queries.get(0);

		if(queries.size() > 1) {
			BooleanQuery booleanQuery = new BooleanQuery();

			BooleanClause.Occur occur = null;
			
			for(int i = 0; i < queries.size(); i++) {
				if(filterColumns[i].isEssentialClause())
					occur = BooleanClause.Occur.MUST;
				else
					occur = BooleanClause.Occur.SHOULD;
				
				booleanQuery.add((Query)queries.get(i), occur);
			}

			return booleanQuery;
		}

		return null;
	}

	/**
	 * 토큰화된 컬럼을 검색하기 위한 질의를 만든다.
	 * 
	 * @param queryString
	 * @return
	 * @throws MultipartRequestzException
	 */
	private Query stringToQuery(String queryString) throws QueryBuilderException {
		String typicalColumnName;
		
		if(queryColumns == null || queryColumns.length == 0)
			typicalColumnName = ScanyContextBuilder.PRIMARY_KEY;
		else
			typicalColumnName = queryColumns[0].getName();
		
		Query query = null;

		try {
			QueryParser queryParser = new QueryParser(typicalColumnName, analyzer);
			queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);

			query = queryParser.parse(queryString);
			
			System.out.println(query);
		} catch(ParseException e) {
			throw new QueryBuilderException("질의문을 분석할 수 없습니다.", e);
		}

		return query;
	}
	
}
