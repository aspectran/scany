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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.jhlabs.scany.engine.entity.Attribute;
import org.jhlabs.scany.engine.entity.AttributeMap;
import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.RecordKey;
import org.jhlabs.scany.engine.entity.RecordKeyException;
import org.jhlabs.scany.engine.entity.RecordList;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.search.query.QueryTextParser;
import org.jhlabs.scany.engine.search.query.SimpleQueryTextParser;
import org.jhlabs.scany.engine.search.summarize.Summarizer;

/**
 * <p>
 * Created: 2008. 01. 07 오전 7:18:00
 * </p>
 * 
 * @author Gulendol
 */
public class SearchModel {

	private Relation relation;

	private List<QueryAttribute> queryAttributeList;

	private List<FilterAttribute> filterAttributeList;

	private List<SortAttribute> sortAttributeList;
	
	private Map<String, Summarizer> summarizerMap;

	private String queryText;

	private String parsedQueryText;
	
	private String[] queryKeywords;

	protected int totalRecords = 0;

	protected int hitsPerPage = 10;
	
	protected int page = 0;

	protected int startRecord = 0;
	
	protected boolean reverse;

	/**
	 * 생성자
	 * 
	 * @param relation Schema
	 * @throws ScanySearchException
	 */
	public SearchModel(Relation relation) throws AnySearcherException {
		this.relation = relation;
	}

	/**
	 * 현재 스키마를 반환한다.
	 * 
	 * @return Schema
	 */
	protected Relation getRelation() {
		return relation;
	}

	public String getDirectory() {
		return relation.getDirectory();
	}
	
	/**
	 * 레코드(Document)의 총 개수를 반환한다.
	 * 
	 * @return
	 */
	public int getTotalRecords() {
		return this.totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	/**
	 * 한 페이지 당 출력하는 레코드의 개수를 반환한다.
	 * 
	 * @return the hitsPerPage
	 */
	public int getHitsPerPage() {
		return hitsPerPage;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getStartRecord() {
		return startRecord;
	}

	public void setStartRecord(int startRecord) {
		this.startRecord = startRecord;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	/**
	 * 한 페이지 당 출력하는 레코드의 개수를 설정한다.
	 * 
	 * @param hitsPerPage 레코드의 개수
	 */
	public void setHitsPerPage(int hitsPerPage) {
		this.hitsPerPage = hitsPerPage;
	}

	public String getQueryText() {
		return queryText;
	}

	public String setQueryText(String queryText) {
		QueryTextParser parser = new SimpleQueryTextParser();
		parsedQueryText = parser.parse(queryText);
		queryKeywords = parser.getKeywords();

		return parsedQueryText;
	}
	
	public String getParsedQueryText() {
		return parsedQueryText;
	}

	public String[] getQueryKeywords() {
		return queryKeywords;
	}

	public List<QueryAttribute> getQueryAttributeList() {
		if(queryAttributeList == null)
			return SearchModelUtils.extractDefaultQueryAttributeList(relation.getAttributeMap());

		return queryAttributeList;
	}

	public void setQueryAttributeList(List<QueryAttribute> queryAttributeList) {
		Iterator<QueryAttribute> iter = queryAttributeList.iterator();
		
		while(iter.hasNext()) {
			QueryAttribute queryAttribute = iter.next();
			addQueryAttribute(queryAttribute);
		}
	}

	/**
	 * 질의 가능 대상 컬럼을 수동으로 추가한다.
	 * 기본 질의 대상 컬럼을 관계없이 별도의 컬럼을 지정하여 검색하기 위함이다. 
	 * 이 메쏘드를 이용해서 별도로 컬럼을 지정하지 않으면 기본 질의 대상 컬럼으로 검색한다.
	 * 
	 * @param columnName 컬럼명
	 * @throws AnySearcherException
	 */
	public void addQueryAttribute(QueryAttribute queryAttribute) {
		Attribute attribute = relation.getAttributeMap().get(queryAttribute.getAttributeName());

		if(attribute == null)
			throw new InvalidAttributeException(queryAttribute.getAttributeName());
		
		if(!RecordKey.RECORD_KEY.equals(attribute.getName())) {
			if(!attribute.isIndexable() || !attribute.isAnalyzable()) {
				throw new InvalidAttributeException(queryAttribute.getAttributeName(), "색인화 또는 토큰화된 속성이어야 합니다.");
			}
		}
		
		queryAttribute.setAttribute(attribute);
		queryAttribute.setAnalyzer(attribute.getAnalyzer());
		
		if(queryAttributeList == null)
			queryAttributeList = new ArrayList<QueryAttribute>();
		
		queryAttributeList.add(queryAttribute);
	}
	
	public List<FilterAttribute> getFilterAttributeList() {
		return filterAttributeList;
	}

	public void setFilterAttributeList(List<FilterAttribute> filterAttributeList) {
		Iterator<FilterAttribute> iter = filterAttributeList.iterator();
		
		while(iter.hasNext()) {
			FilterAttribute filterAttribute = iter.next();
			addFilterAttribute(filterAttribute);
		}
	}

	public void addFilterAttribute(String attributeName, String keyword, boolean essential) {
		addFilterAttribute(new FilterAttribute(attributeName, keyword, essential));
	}

	public void addFilterAttribute(RecordKey recordKey) {
		addFilterAttribute(RecordKey.RECORD_KEY, recordKey.toString(), true);
	}
	
	/**
	 * 필터 컬럼을 추가한다.
	 * 
	 * <pre>
	 * 필터 컬럼으로 지정할 수 있는 컬럼은 다음 조건을 충족해야 한다.
	 * - 색인컬럼(Indexed)
	 * - 토큰분리컬럼(Tokenized)
	 * </pre>
	 * @param columnName 컬럼명
	 * @throws AnySearcherException
	 * 
	 */
	public void addFilterAttribute(FilterAttribute filterAttribute) {
		Attribute attribute = relation.getAttributeMap().get(filterAttribute.getAttributeName());

		if(attribute == null)
			throw new InvalidAttributeException(filterAttribute.getAttributeName());
		
		if(!RecordKey.RECORD_KEY.equals(attribute.getName())) {
			if(!attribute.isIndexable() || !attribute.isAnalyzable()) {
				throw new InvalidAttributeException(attribute.getName(), "색인화 또는 토큰화된 속성이어야 합니다.");
			}
		}
		
		filterAttribute.setAttribute(attribute);
		
		if(filterAttributeList == null)
			filterAttributeList = new ArrayList<FilterAttribute>();
		
		filterAttributeList.add(filterAttribute);
	}
	
	public List<SortAttribute> getSortAttributeList() {
		return sortAttributeList;
	}

	public void setSortAttributeList(List<SortAttribute> sortAttributeList) {
		Iterator<SortAttribute> iter = sortAttributeList.iterator();
		
		while(iter.hasNext()) {
			SortAttribute sortAttribute = iter.next();
			addSortAttribute(sortAttribute);
		}
	}
	
	public void addSortAttribute(String attributeName, SortFieldType sortFieldType, boolean reverse) {
		addSortAttribute(new SortAttribute(attributeName, sortFieldType, reverse));
	}
	
	/**
	 * 정렬 컬럼을 지정한다. 해제할때는 null을 입력하자.
	 * 
	 * <pre>
	 * 주의사항
	 *     컬럼의 속성에서 IsTokenized 옵션이 true 인 경우
	 *     즉, tokenized fields인 경우는 정렬이 될 수 없음을 기억하자.
	 *     IsIndexed 옵션이 false 인 경우도 정렬이 될 수 없다.
	 * </pre>
	 * 
	 * @param columnName 컬럼명
	 * @param reverse 역순 정렬 여부
	 * @throws AnySearcherException
	 */
	public void addSortAttribute(SortAttribute sortAttribute) {
		Attribute attribute = relation.getAttributeMap().get(sortAttribute.getAttributeName());

		if(attribute == null)
			throw new InvalidAttributeException(sortAttribute.getAttributeName());
		
		if(!RecordKey.RECORD_KEY.equals(attribute.getName())) {
			if(!attribute.isIndexable() || !attribute.isAnalyzable()) {
				throw new InvalidAttributeException(attribute.getName(), "색인화 또는 토큰화된 속성이어야 합니다.");
			}
		}
		
		sortAttribute.setAttribute(attribute);

		if(sortAttributeList == null)
			sortAttributeList = new ArrayList<SortAttribute>();
		
		sortAttributeList.add(sortAttribute);
	}

	public Map<String, Summarizer> getSummarizerMap() {
		if(summarizerMap == null)
			return SearchModelUtils.extractDefaultSummarizerMap(relation.getAttributeMap());

		return summarizerMap;
	}

	public void setSummarizerMap(Map<String, Summarizer> summarizerMap) {
		this.summarizerMap = summarizerMap;
	}

	public void setSummarizer(String attributeName, Summarizer summarizer) {
		if(relation.getAttributeMap().get(attributeName) == null)
			throw new InvalidAttributeException(attributeName);
		
		if(summarizerMap == null)
			summarizerMap = new HashMap<String, Summarizer>();
		
		summarizerMap.put(attributeName, summarizer);
	}
	
	public void setSummarizer(String attributeName, String summarizerId) {
		Summarizer summarizer = relation.getSchema().getSummarizer(summarizerId);
		
		if(summarizer == null)
			throw new InvalidSummarizerException(summarizerId);

		setSummarizer(attributeName, summarizer);
	}
	
	public void clearQueryAttribute() {
		queryAttributeList = null;
	}
	
	public void clearFilterAttribute() {
		filterAttributeList = null;
	}
	
	public void clearSortAttribute() {
		sortAttributeList = null;
	}
	
	/**
	 * 검색결과에서 지정한 범위 Document를 Column 리스트로 반환.
	 *
	 * @param reader the reader
	 * @param docs the docs
	 * @param start the start
	 * @param end the end
	 * @return List
	 * @throws RecordKeyException the record key exception
	 * @throws CorruptIndexException the corrupt index exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public RecordList populateRecordList(IndexReader reader, ScoreDoc[] docs, int start, int end) throws RecordKeyException, CorruptIndexException, IOException {
		RecordList recordList = new RecordList(end - start + 1);
		
		for(int i = start; i <= end; i++) {
			Document document = reader.document(docs[i].doc);
			Record record = createRecord(document);
			recordList.add(record);
			
			if(queryKeywords != null && summarizerMap != null && summarizerMap.size() > 0) {
				for(Map.Entry<String, Summarizer> entry : summarizerMap.entrySet()) {
					String key = entry.getKey();
					String value = record.getValue(key);
					
					if(value != null && value.length() > 0) {
						Summarizer summarizer = entry.getValue();
						value = summarizer.summarize(queryKeywords, value);
						record.setValue(key, value);
						
					}
				}
			}
		}
		
		return recordList;
	}

	/**
	 * Document를 Record로 반환
	 * 
	 * @param document
	 * @param columns
	 * @return Record
	 * @throws RecordKeyException 
	 */
	public Record createRecord(Document document) throws RecordKeyException {
		RecordKey recordKey = relation.newRecordKey();
		recordKey.setRecordKeyString(document.get(RecordKey.RECORD_KEY));
		
		Record record = new Record();
		record.setRecordKey(recordKey);
		
		AttributeMap attributeMap = relation.getAttributeMap();
		
		if(attributeMap != null) {
			String[] names = attributeMap.getAttributeNames();
			
			for(int i = 0; i < names.length; i++) {
				record.setValue(names[i], document.get(names[i]));
			}
		}
		
		return record;
	}

}
