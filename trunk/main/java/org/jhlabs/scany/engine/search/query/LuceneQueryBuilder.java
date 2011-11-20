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

import java.util.Iterator;
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
import org.jhlabs.scany.context.ScanyContext;
import org.jhlabs.scany.engine.entity.RecordKey;
import org.jhlabs.scany.engine.search.FilterAttribute;

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

	private List<Query> queryList;
	
	public LuceneQueryBuilder() {
	}

	/**
	 * 질의를 분석한다.
	 * 
	 * @param queryString
	 * @return
	 * @throws MultipartRequestzException
	 */
	public Query build() throws QueryBuilderException {
		try {
			if(queryList.size() == 0)
				throw new IllegalArgumentException("no Query.");
			
			if(queryList.size() == 1)
				return queryList.get(0);
			
			Iterator<Query> iter = queryList.iterator();
			BooleanQuery booleanQuery = new BooleanQuery();
			
			while(iter.hasNext()) {
				Query query = iter.next();
				
				booleanQuery.add(query, BooleanClause.Occur.MUST);
			}

			return booleanQuery;
		} catch(Exception e) {
			throw new QueryBuilderException("Cannot build query.", e);
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
	public void addQuery(List<FilterAttribute> filterAttributeList) {
		if(filterAttributeList == null || filterAttributeList.size() == 0)
			return;
		
		if(filterAttributeList.size() == 1) {
			queryList.add(makeQuery((FilterAttribute)filterAttributeList.get(0)));
			return;
		}
		
		Iterator<FilterAttribute> iter = filterAttributeList.iterator();
		BooleanQuery booleanQuery = new BooleanQuery();
		
		while(iter.hasNext()) {
			FilterAttribute filterAttribute = iter.next();
			Query query = makeQuery(filterAttribute);

			if(filterAttribute.isEssential())
				booleanQuery.add(query, BooleanClause.Occur.MUST);
			else
				booleanQuery.add(query, BooleanClause.Occur.SHOULD);
		}
		
		queryList.add(booleanQuery);
	}

	private Query makeQuery(FilterAttribute filterAttribute) {
		String attributeName = filterAttribute.getAttributeName();
		String keyword = filterAttribute.getKeyword();

		Query query = null;
		Term term = new Term(attributeName, keyword);

		if(keyword.indexOf('?') != -1 || keyword.indexOf('*') != -1) {
			query = new WildcardQuery(term);
		} else {
			query = new TermQuery(term);
		}
		
		return query;
	}
	
	/**
	 * 토큰화된 컬럼을 검색하기 위한 질의를 만든다.
	 * 
	 * @param queryString
	 * @return
	 * @throws QueryBuilderException 
	 * @throws MultipartRequestzException
	 */
	public void addQuery(String queryString, List<String> queryAttributeList, Analyzer analyzer) throws QueryBuilderException {
		if(queryString == null || queryString.length() == 0)
			return;
		
		String typicalColumnName;
		
		if(queryAttributeList == null || queryAttributeList.size() == 0)
			typicalColumnName = RecordKey.RECORD_KEY;
		else
			typicalColumnName = (String)queryAttributeList.get(0);
		
		try {
			QueryParser queryParser = new QueryParser(ScanyContext.LUCENE_VERSION, typicalColumnName, analyzer);
			queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);

			Query query = queryParser.parse(queryString);
			queryList.add(query);
		} catch(ParseException e) {
			throw new QueryBuilderException("Cannot add a Query.", e);
		}
	}
	
}
