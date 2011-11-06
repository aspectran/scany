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

import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.Table;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

/**
 * 검색기.
 * 
 * + 일반 검색기능
 *     다양한 검색조건에 의한 검색 및 정렬 기능을 가지고 있다.
 * 
 * + 랜덤 검색기능
 *     검색조건을 지정한 개수만큼 랜덤하게 가지고 올 수 있으며,
 *     정렬을 통해 최신 데이터를 지정한 개수만큼 랜덤하게 검색할 수 있는 기능을 가지고 있다.
 *     
 * + 순차 탐색기능
 *     복잡한 검색질의 없이 순차적으로 레코드에 접근할 수 있다.
 *     최신 레코드를 빠르게 얻고자 할때 검색기를 쓰는 것 보다 속도면에서 월등하다.
 *     엔진(루씬)의 특성상 이전에 등록된 레코드라도 최근에 업데이트가 되었다면 최신 데이터로 분류를 한다.
 *     만약 이 것이 문제가 된다면 속도면에서 손해를 보더라고 일반 검색기능을 이용해야 한다. 
 * 
 * <p>Created: 2007. 01. 19 오전 1:29:18</p>
 * 
 * @author Gulendol
 *
 */
public class AnyExpertSearcher extends AnySearcherModel implements AnySearcher {
	
	/**
	 * 생성자
	 * 검색하기 전에 반드시 스키마를 지정해야 한다.
	 */
	public AnyExpertSearcher() {
		super();
	}

	/**
	 * 생성자
	 * @param schema 스키마
	 * @throws ScanySearchException
	 */
	public AnyExpertSearcher(Table schema) throws AnySearchException {
		super(schema);
	}
	
	/**
	 * 질의문에 의한 검색을 수행한다.
	 * 별도로 페이지 번호를 지정하지 않으면 1 페이지를 반환한다. 
	 * @param queryString
	 * @return
	 * @throws AnySearchException
	 */
	public Record[] search(String queryString) throws AnySearchException {
		return search(queryString, 1);
	}
	
	/**
	 * 질의문에 의한 검색을 수행한다.
	 * 필터 컬럼만 지정했을 경우 사용한다.
	 * @param queryString
	 * @return
	 * @throws AnySearchException
	 */
	public Record[] search(int pageNo) throws AnySearchException {
		return search(null, pageNo);
	}
	
	/**
	 * 질의문에 의한 검색을 수행한다.
	 * 빈 질의문을 지정하거나, 페이지 범위를 벗어난 페이지 번호를 지정할 경우 null을 반환한다.
	 * 검색 결과가 없으면 Record[0]을 반환한다.
	 * 색인 저장소(디렉토리)가 생성되어 있지 않으면 null을 반환하고,
	 * 빈 디렉토리 즉, 디렉토리 내에 세그먼트 파일이 존재하지 않으면 예외를 발생한다. 
	 * @param queryString 사용자가 입력한 질의문
	 * @param pageNo 페이지 번호
	 * @return hitsPerPage 개수 만큼의 Record를 반환한다.
	 * @throws AnySearchException
	 */
	public Record[] search(String queryString, int pageNo) throws AnySearchException {
		Searcher searcher = null;
		
		try {
			if(pageNo <= 0)
				return null;

			File file = new File(schema.getRepository());
			
			if(!file.exists())
				return null;
			
			try {
				searcher = new IndexSearcher(schema.getRepository());
			} catch(Exception e) {
				throw new CorruptIndexException("색인 저장소에 세그먼트 파일이 존재하지 않습니다. (Schema ID: " +
						schema.getSchemaId() + ")");
			}

			Analyzer analyzer = schema.getAnalyzer();
			
			QueryBuilder queryBuilder = new QueryBuilder(analyzer);
			queryBuilder.setFilterColumns(getFilterColumns());
			queryBuilder.setQeuryColumns(getQueryColumns());
			
			Query query = queryBuilder.getQuery(queryString);
			
			//System.out.println(query.toString());

			Hits hits = null;
			
			if(sortColumn != null)
				hits = searcher.search(query, sortColumn.getSort());
			else
				hits = searcher.search(query);			
			
			// 페이징
			totalRecords = hits.length();
			
			int totalPages = (totalRecords <= 0) ? 
								0 : (int)((totalRecords - 1) / hitsPerPage) + 1;
			
			if(totalRecords > 0) {
				if(pageNo > totalPages)
					return null;
			}
			
			int startDocNo = hitsPerPage * pageNo - hitsPerPage;
			int endDocNo = Math.min(startDocNo + hitsPerPage - 1, totalRecords - 1);

			Record[] records = transplantToRecords(hits, startDocNo, endDocNo);
			
			return records;

		} catch(Exception e) {
			throw new AnySearchException("검색 과정에서 오류가 발생했습니다.", e);

		} finally {
			try {
				if(searcher != null)
					searcher.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 최근 레코드 또는 랜덤 레코드를 검색한다.
	 * 검색 범위를 한정하기 위해서는 필터컬럼을 추가해야 한다.
	 * 검색 범위를 한정하지 않으면 모든 레코드에 대해 검색한다.
	 * @return hitsPerPage 개의 레코드
	 * @throws AnySearchException
	 */
	public Record[] random() throws AnySearchException {
		return random(null);
	}
	
	/**
	 * 최근 레코드 또는 랜덤 레코드를 검색한다.
	 * 검색 범위를 한정하기 위해서는 필터컬럼 또는 쿼리컬럼을 추가해야 한다.
	 * 검색 범위를 한정하지 않으면 모든 레코드에 대해 검색한다.
	 * 색인 저장소(디렉토리)가 생성되어 있지 않으면 null을 반환하고,
	 * 빈 디렉토리 즉, 디렉토리 내에 세그먼트 파일이 존재하지 않으면 예외를 발생한다. 
	 * @param queryString 질의문
	 * @return hitsPerPage 개의 레코드
	 * @throws AnySearchException
	 */
	public Record[] random(String queryString) throws AnySearchException {
		Searcher searcher = null;

		try {
			File file = new File(schema.getRepository());
			
			if(!file.exists())
				return null;
			
			try {
				searcher = new IndexSearcher(schema.getRepository());
			} catch(Exception e) {
				throw new CorruptIndexException("색인 저장소에 세그먼트 파일이 존재하지 않습니다. (Schema ID: " +
						schema.getSchemaId() + ")");
			}
			
			List records = new ArrayList();

			QueryBuilder queryBuilder = new QueryBuilder(schema.getAnalyzer());
			queryBuilder.setFilterColumns(getFilterColumns());
			queryBuilder.setQeuryColumns(getQueryColumns());
			
			Query query = queryBuilder.getQuery(queryString);

			Hits hits = null;
			
			if(sortColumn != null)
				hits = searcher.search(query, sortColumn.getSort());
			else
				hits = searcher.search(query);
			
			totalRecords = hits.length();

			// 랜덤
			if(hitsPerPage < totalRecords) {
				int[] docs = getRandomDocumentNo(totalRecords, hitsPerPage);

				for(int i = 0; i < docs.length; i++) {
					if(docs[i] == -1)
						continue;
					
					Record record = documentToRecord(hits.doc(docs[i]), schema);
					records.add(record);
				}
				
				//System.out.println("랜덤");
			} else {
				//System.out.println("미달");

				// SortColumn이 지정되었을 경우 0번부터 차례대로
				if(sortColumn != null) {
					for(int i = 0; i < totalRecords; i++) {							
						Record record = documentToRecord(hits.doc(i), schema);
						records.add(record);
					}
					
				// SortColumn이 지정되지 않았을 경우 document 번호를 역순으로
				// 즉 최신 데이터를 먼저 내 보낸다.
				} else {
					for(int i = totalRecords - 1; i >= 0; i--) {
						Record record = documentToRecord(hits.doc(i), schema);
						records.add(record);
					}
				}
			}
			
			return (Record[])records.toArray(new Record[records.size()]);

		} catch(Exception e) {
			throw new AnySearchException("Scany(RandomSearcher) 검색 과정에서 오류가 발생했습니다.", e);

		} finally {
			try {
				if(searcher != null)
					searcher.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 전체 Document 개수에서 랜덤하게 maxDocs개 만큼 Document 번호를 반환한다.
	 * 랜덤 Document 번호가 maxDocs 개에 못 미칠 경우 나머지는 -1로 채워진다.
	 * @param totalDocs 총 레코드 수
	 * @param maxDocs 가져올 최대 Document 번호 개수
	 * @return
	 */
	private int[] getRandomDocumentNo(int totalDocs, int maxDocs) {
		int[] docs = new int[maxDocs];
		int docNo = -1;
		int dupCnt = 0;
		
		int cnt = 0;

		while(cnt < maxDocs) {
			if(dupCnt >= maxDocs)
				break;
			
			docNo = (int)(Math.random() * totalDocs);
			
			for(int n = 0; n < cnt; n++) {
				if(docs[n] == docNo) {
					dupCnt++;					
					continue;
				}
			}
			
			docs[cnt++] = docNo;
		}
		
		for(int i = cnt; i < maxDocs; i++) {
			docs[i] = -1;
		}
		
		// 정렬
		int buf = 0;

		for(int i = 0; i < docs.length - 1; i++) {
			for(int j = 1; j < docs.length; j++) {
				if(docs[i] > docs[j]) {
					buf = docs[i];
					docs[i] = docs[j];
					docs[j] = buf;
				}
			}
		}

		return docs;
	}

	/**
	 * 레코드(Record)가 기록된 순서 또는 정렬된 순서에 따라 순차적으로 접근하여 특정 범위의 레코드를 반환한다. 
	 * 엔진(루씬)의 특성상 과거에 등록된 도큐먼트가 수정되면 가장 최근 등록시간 정보를 가진다. 
	 * 색인 저장소(디렉토리)가 생성되어 있지 않으면 null을 반환하고,
	 * 빈 디렉토리 즉, 디렉토리 내에 세그먼트 파일이 존재하지 않으면 예외를 발생한다. 
	 * @param start 시작 지점
	 * @param maxRecords 레코드 개수
	 * @param reverse 역정렬 여부
	 * @return
	 * @throws AnySearchException
	 */
	public Record[] seek(int start, int maxRecords, boolean reverse) throws AnySearchException {
		IndexReader reader = null;

		try {
			if(start < 0)
				return null;
			
			File file = new File(schema.getRepository());
			
			if(!file.exists())
				return null;
			
			try {
				reader = IndexReader.open(schema.getRepository());
			} catch(Exception e) {
				throw new CorruptIndexException("색인 저장소에 세그먼트 파일이 존재하지 않습니다. (Schema ID: " +
						schema.getSchemaId() + ")");
			}
			
			List records = new ArrayList();

			int n = 0;
			int end = start + maxRecords;
			
			if(reverse) {
				for(int i = reader.maxDoc() - 1; i >= 0; i--) {
					if(!reader.isDeleted(i)) {					
						if(n >= start && n < end) {
							records.add(AnySearcherModel.documentToRecord(reader.document(i), schema));
						}
						
						n++;
					}
				}
			} else {
				for(int i = 0; i < reader.maxDoc(); i++) {
					if(!reader.isDeleted(i)) {					
						if(n >= start && n < end) {
							records.add(AnySearcherModel.documentToRecord(reader.document(i), schema));
						}
						
						n++;
					}
				}
			}

			if(n == 0)
				return null;
			
			return (Record[])records.toArray(new Record[records.size()]);

		} catch(Exception e) {
			throw new AnySearchException("순차 검색 과정에서 오류가 발생했습니다.", e);

		} finally {
			try {
				if(reader != null)
					reader.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
