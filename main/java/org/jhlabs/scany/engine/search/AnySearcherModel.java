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
import org.apache.lucene.search.Hits;
import org.jhlabs.scany.context.builder.ScanyContextBuilder;
import org.jhlabs.scany.engine.entity.Attribute;
import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.RecordKey;
import org.jhlabs.scany.engine.entity.RecordKeyException;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.search.summarize.SimpleFragmentSummarizer;
import org.jhlabs.scany.engine.search.summarize.Summarizer;
import org.jhlabs.scany.util.StringUtils;

/**
 * <p>
 * Created: 2008. 01. 07 오전 7:18:00
 * </p>
 * 
 * @author Gulendol
 */
public class AnySearcherModel {

	protected Relation schema;

	protected RecordKey primaryKey;

	private List queryColumns;

	private List filterColumns;

	protected SortingAttributes sortColumn;

	protected int totalRecords = 0;

	protected int hitsPerPage = 10;

	protected int summaryLength = 200;
	
	protected Map summarizers;
	
	/**
	 * 생성자 검색하기 전에 반드시 Schema를 지정해야 한다.
	 */
	public AnySearcherModel() {
	}

	/**
	 * 생성자
	 * 
	 * @param schema Schema
	 * @throws ScanySearchException
	 */
	public AnySearcherModel(Relation schema) throws AnySearchException {
		setSchema(schema);
	}

	/**
	 * 현재 스키마를 반환한다.
	 * 
	 * @return Schema
	 */
	public Relation getSchema() {
		return schema;
	}

	/**
	 * 스키마를 지정한다.
	 * 
	 * @param schema Schema Schema
	 * @throws AnySearchException
	 */
	public void setSchema(Relation schema) throws AnySearchException {
		this.schema = schema;

		this.primaryKey = null;
		this.queryColumns = null;
		this.sortColumn = null;

		asureSchema();
	}

	/**
	 * 레코드(Document)의 총 개수를 반환한다.
	 * 
	 * @return
	 */
	public int getTotalRecords() {
		return this.totalRecords;
	}

	/**
	 * 요약문의 최대 길이를 반환한다.
	 * 
	 * @return the summaryLength 요약문 최대 길이
	 */
	public int getSummaryLength() {
		return summaryLength;
	}

	/**
	 * 요약문의 최대 길이를 설정한다.
	 * 
	 * @param summaryLength 요약문 최대 길이
	 */
	public void setSummaryLength(int summaryLength) {
		this.summaryLength = summaryLength;
	}

	/**
	 * 한 페이지 당 출력하는 레코드의 개수를 반환한다.
	 * 
	 * @return the hitsPerPage
	 */
	public int getHitsPerPage() {
		return hitsPerPage;
	}

	/**
	 * 한 페이지 당 출력하는 레코드의 개수를 설정한다.
	 * 
	 * @param hitsPerPage 레코드의 개수
	 */
	public void setHitsPerPage(int hitsPerPage) {
		this.hitsPerPage = hitsPerPage;
	}

	/**
	 * PrimaryKey를 반환한다.
	 * 와일드카드('*', '?')가 사용된 PrimaryKey를 사용할 수 있다.
	 * 
	 * @return the primaryKey
	 */
	public RecordKey getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * PrimaryKey를 지정하여 검색범위를 줄인다.
	 * 
	 * <pre>
	 * 와일드카드('*', '?')가 사용된 PrimaryKey를 사용할 수 있다.
	 * KeyPattern이 &quot;groupId:boardId:articleNo&quot; 일 경우
	 *     PrimaryKey 값으로 &quot;site:notice:*&quot; 지정하면
	 *     &quot;site&quot; 그룹의 &quot;notice&quot; 게시판의 모든 글에서 검색할 것이다.
	 * </pre>
	 * @param primaryKey the primaryKey to set
	 * 
	 */
	public void setPrimaryKey(RecordKey primaryKey) {
		this.primaryKey = primaryKey;
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
	 * @throws AnySearchException
	 */
	public void setSortColumn(SortingAttributes sortColumn) throws AnySearchException {
		try {
			String[] columnNames = sortColumn.getColumnNames();
			Attribute[] columns = schema.getAttributes();

			for(int i = 0; i < columnNames.length; i++) {
				boolean isOk = false;

				if(columnNames[i].equals(ScanyContextBuilder.PRIMARY_KEY)) {
					isOk = true;
					break;
				}

				for(int j = 0; j < columns.length; j++) {
					if(columns[j].isIndexable() && !columns[j].isTokenizable()) {
						isOk = true;
						break;
					}
				}

				if(!isOk) {
					throw new IllegalArgumentException("정렬 가능한 컬럼이 아닙니다. (Column: " + columnNames[i] + ")");
				}
			}

			this.sortColumn = sortColumn;

		} catch(Exception e) {
			throw new AnySearchException("정렬컬럼을 지정할 수 없습니다.", e);
		}
	}

	/**
	 * 질의 컬럼을 반환한다. 수동으로 지정했을 경우 기본 질의 컬럼은 포함되지 않는다.
	 * 
	 * @return
	 */
	protected Attribute[] getQueryColumns() {
		List list = null;

		// 질의가능 컬럼을 수동으로 지정했을 경우
		if(queryColumns != null && queryColumns.size() > 0) {
			list = queryColumns;

		// 기본 질의 컬럼
		} else {
			list = new ArrayList();

			Attribute[] columns = schema.getAttributes();

			for(int i = 0; i < columns.length; i++) {
				if(columns[i].isQueryable()) {
					list.add(columns[i]);
				}
			}
		}

		if(list == null || list.size() == 0)
			return null;

		return (Attribute[])list.toArray(new Attribute[list.size()]);
	}

	/**
	 * 필터 컬럼을 반환한다.
	 * 
	 * @return
	 */
	protected FilteringAttributes[] getFilterColumns() {
		if(filterColumns == null || filterColumns.size() == 0)
			return null;

		return (FilteringAttributes[])filterColumns.toArray(new FilteringAttributes[filterColumns.size()]);
	}

	/**
	 * Summarizers를 반환한다.
	 * 
	 * @return
	 */
	protected Map getSummarizers() {
		if(summarizers == null || summarizers.size() == 0)
			return null;
		
		return summarizers;
	}

	/**
	 * 질의 가능 대상 컬럼을 수동으로 추가한다.
	 * 기본 질의 대상 컬럼을 관계없이 별도의 컬럼을 지정하여 검색하기 위함이다. 
	 * 이 메쏘드를 이용해서 별도로 컬럼을 지정하지 않으면 기본 질의 대상 컬럼으로 검색한다.
	 * 
	 * @param columnName 컬럼명
	 * @throws AnySearchException
	 */
	public void addQueryColumn(String columnName) throws AnySearchException {
		try {
			asureSchema();

			Attribute column = schema.getAttribute(columnName);

			if(column == null)
				throw new IllegalArgumentException("유효한 컬럼명이 아닙니다. (Column: " + columnName + ")");

			if(!column.isIndexable())
				throw new IllegalArgumentException("색인화된 컬럼이 아니므로, 질의 컬럼으로 지정할 수 없습니다. (Column: " + columnName + ")");

			if(!column.isTokenizable())
				throw new IllegalArgumentException("토큰화되지 않은 컬럼이므로, 필터 컬럼으로 지정해야 합니다. (Column: " + columnName + ")");

			if(queryColumns == null)
				queryColumns = new ArrayList();

			queryColumns.add(column);

		} catch(Exception e) {
			throw new AnySearchException("쿼리컬럼을 추가할 수 없습니다.", e);
		}
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
	 * @throws AnySearchException
	 * 
	 */
	public void addFilterColumn(String columnName, String keyword, boolean isEssentialClause)
			throws AnySearchException {
		try {
			asureSchema();

			Attribute column = schema.getAttribute(columnName);

			if(column == null)
				throw new IllegalArgumentException("유효한 컬럼명이 아닙니다. (Column: " + columnName + ")");

			if(!column.isIndexable() || !column.isTokenizable())
				throw new IllegalArgumentException("필터 가능 컬럼이 아닙니다. (Column: " + columnName + ")");

			if(filterColumns == null)
				filterColumns = new ArrayList();

			FilteringAttributes filterColumn = new FilteringAttributes(columnName, keyword, isEssentialClause);

			filterColumns.add(filterColumn);

		} catch(Exception e) {
			throw new AnySearchException("필터컬럼을 추가할 수 없습니다.", e);
		}
	}

	/**
	 * PrimaryKey를 필터 컬럼으로 추가한다.
	 * PrimaryKey는 필수조건이 된다.
	 * 
	 * @param recordKey PrimaryKey
	 * @throws AnySearchException
	 */
	public void addFilterColumn(RecordKey recordKey) throws AnySearchException {
		try {
			asureSchema();

			String rkey = recordKey.getRecordKeyString();

			FilteringAttributes fc = new FilteringAttributes(RecordKey.RECORD_KEY, rkey, true);

			if(filterColumns == null)
				filterColumns = new ArrayList();

			filterColumns.add(fc);

		} catch(Exception e) {
			throw new AnySearchException("필터컬럼을 추가할 수 없습니다.", e);
		}
	}

	/**
	 * 컬럼별 Summarizer 지정한다.
	 * @param columnName 컬럼명
	 * @param summarizer
	 * @throws AnySearchException
	 */
	public void addSummarizer(String columnName, Summarizer summarizer) throws AnySearchException {
		try {
			if(!isColumnName(columnName))
				throw new IllegalArgumentException("유효한 컬럼명이 아닙니다. (Column: " + columnName + ")");
			
			if(summarizers == null)
				summarizers = new HashMap();
			
			summarizers.put(columnName, summarizer);

		} catch(Exception e) {
			throw new AnySearchException("Summarizer를 추가할 수 없습니다.", e);
		}
	}

	/**
	 * 질의 컬럼을 모두 해제한다. 이후부터 기본 질의 컬럼으로 검색된다.
	 */
	public void clearQueryColumns() {
		queryColumns = null;
	}

	/**
	 * 필터 컬럼을 모두 해제한다.
	 */
	public void clearFilterColumns() {
		filterColumns = null;
	}

	/**
	 * 컬럼별 Summarizer 지정을 초기화한다.
	 */
	public void clearSummarizers() {
		summarizers = null;
	}

	/**
	 * 컬럼명이 맞는지 조회한다.
	 * 
	 * @param columnName 컬럼명
	 * @return true or false
	 */
	public boolean isColumnName(String columnName) {
		Attribute[] columns = schema.getAttributes();

		for(int i = 0; i < columns.length; i++) {
			if(columnName.equals(columns[i].getName())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 필터컬럼과 쿼리컬럼으로 지정된 컬럼의 총 개수를 반환한다.
	 * 
	 * @param isIncludeDefaultQueryColumn 기본 질의컬럼을 포함할 지 여부
	 * @return
	 */
	protected int getClauseCount(boolean isIncludeDefaultQueryColumn) {
		int count = 0;

		if(filterColumns != null)
			count += filterColumns.size();

		// 질의가능 컬럼을 수동으로 지정했을 경우
		if(queryColumns != null && queryColumns.size() > 0) {
			count += queryColumns.size();

			// 기본 질의 컬럼
		} else if(isIncludeDefaultQueryColumn) {
			Attribute[] columns = schema.getAttributes();

			for(int i = 0; i < columns.length; i++) {
				if(columns[i].isQueryable()) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * 검색결과에서 지정한 범위 Document를 Column 리스트로 반환
	 * @param hits
	 * @param startDocNo
	 * @param endDocNo
	 * @return Record 배열
	 * @throws RecordKeyException 
	 */
	protected Record[] transplantToRecords(Hits hits, int startDocNo, int endDocNo) throws RecordKeyException {
		List records = new ArrayList(endDocNo - startDocNo + 1);
		transplantToRecords(records, hits, startDocNo, endDocNo);
		
		return (Record[])records.toArray(new Record[records.size()]);
	}
	
	/**
	 * 검색결과에서 지정한 범위 Document를 Column 리스트로 반환
	 * 
	 * @param fields
	 * @param hits
	 * @param startDocNo
	 * @param endDocNo
	 * @param summarizer
	 * @return List
	 * @throws RecordKeyException 
	 */
	protected List transplantToRecords(List records, Hits hits, int startDocNo, int endDocNo) throws RecordKeyException {
		try {
			for(int i = startDocNo; i <= endDocNo; i++) {
				Record record = documentToRecord(hits.doc(i), schema);
				records.add(record);
			}
			
		} catch(IOException e) {
			// e.printStackTrace();
		}

		return records;
	}

	/**
	 * 스키마가 지정되어 있는지를 검증한다.
	 * 
	 * @throws AnySearchException
	 */
	private void asureSchema() throws AnySearchException {
		if(schema == null)
			throw new AnySearchException("스키마를 지정하세요.(Schema is null)");
	}

	/**
	 * Document를 Record로 반환
	 * 
	 * @param document
	 * @param columns
	 * @return Record
	 * @throws RecordKeyException 
	 */
	protected static Record documentToRecord(Document document, Relation relation) throws RecordKeyException {
		RecordKey recordKey = relation.newRecordKey();
		recordKey.setRecordKeyString(document.get(RecordKey.RECORD_KEY));
		
		Record record = new Record();
		record.setRecordKey(recordKey);
		
		Attribute[] columns = relation.getAttributes();

		for(int i = 0; i < columns.length; i++) {
			record.setValue(columns[i].getName(), document.get(columns[i].getName()));
		}

		return record;
	}
	
	/**
	 * Summarize.
	 *
	 * @param keywords the keywords
	 * @param records the records
	 * @return the record[]
	 */
	protected Record[] summarize(String[] keywords, Record[] records) {
		Iterator it = (Iterator)summarizers.keySet().iterator();
		
		while(it.hasNext()) {
			String attributenName = (String)it.next();
			SimpleFragmentSummarizer summarizer = (SimpleFragmentSummarizer)summarizers.get(attributenName);
			summarizer.setKeywords(keywords);
			
			for(int i = 0; i < records.length; i++) {
				String content = records[i].getValue(attributenName);
				
				if(!StringUtils.isEmpty(content)) {
					records[i].setValue(attributenName, summarizer.summarize(content));
				}
			}
		}
		
		return records;
	}
}
