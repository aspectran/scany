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
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.RecordKeyException;
import org.jhlabs.scany.engine.entity.RecordList;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.search.extract.PagingRecordExtractor;
import org.jhlabs.scany.engine.search.extract.RandomRecordExtractor;
import org.jhlabs.scany.engine.search.extract.RecordExtractor;
import org.jhlabs.scany.engine.search.extract.SequentialRecordExtractor;
import org.jhlabs.scany.engine.search.paging.IterablePaging;
import org.jhlabs.scany.engine.search.query.LuceneQueryBuilder;
import org.jhlabs.scany.engine.search.query.QueryBuilderException;

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
public class LuceneSearcher extends SearchModel implements AnySearcher {
	
	/**
	 * 생성자
	 * @param relation 스키마
	 * @throws ScanySearchException
	 */
	public LuceneSearcher(Relation relation) throws AnySearcherException {
		super(relation);
	}
	
	/**
	 * 질의문에 의한 검색을 수행한다.
	 * 별도로 페이지 번호를 지정하지 않으면 1 페이지를 반환한다. 
	 * @param queryString
	 * @return
	 * @throws AnySearcherException
	 */
	public RecordList search(String queryString) throws AnySearcherException {
		return search(queryString, 1);
	}
	
	/**
	 * 질의문에 의한 검색을 수행한다.
	 * 필터 컬럼만 지정했을 경우 사용한다.
	 * @param queryString
	 * @return
	 * @throws AnySearcherException
	 */
	public RecordList search(int pageNo) throws AnySearcherException {
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
	 * @throws AnySearcherException
	 */
	public RecordList search(String queryString, int page) throws AnySearcherException {
		if(page <= 0)
			return null;

		try {
			setQueryString(queryString);
			setPage(page);

			RecordExtractor recordExtractor = new PagingRecordExtractor((SearchModel)this);
			
			search((SearchModel)this, recordExtractor);			

			return recordExtractor.getRecordList();

		} catch(Exception e) {
			throw new AnySearcherException("Search failed.", e);
		}
	}
	
	/**
	 * 최근 레코드 또는 랜덤 레코드를 검색한다.
	 * 검색 범위를 한정하기 위해서는 필터컬럼을 추가해야 한다.
	 * 검색 범위를 한정하지 않으면 모든 레코드에 대해 검색한다.
	 * @return hitsPerPage 개의 레코드
	 * @throws AnySearcherException
	 */
	public RecordList random() throws AnySearcherException {
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
	 * @throws AnySearcherException
	 */
	public RecordList random(String queryString) throws AnySearcherException {
		try {
			setQueryString(queryString);
			
			RecordExtractor recordExtractor = new RandomRecordExtractor((SearchModel)this);
			
			search((SearchModel)this, recordExtractor);			

			return recordExtractor.getRecordList();

		} catch(Exception e) {
			throw new AnySearcherException("Random search failed.", e);
		}
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
	 * @throws AnySearcherException
	 */
	public RecordList seek(int start, int maxRecords, boolean reverse) throws AnySearcherException {
		if(start < 0)
			return null;

		try {
			setPage(1);
			setStartRecord(start);
			setHitsPerPage(maxRecords);
			setReverse(reverse);
			
			RecordExtractor recordExtractor = new SequentialRecordExtractor((SearchModel)this);
			
			search((SearchModel)this, recordExtractor);			

			return recordExtractor.getRecordList();

		} catch(Exception e) {
			throw new AnySearcherException("Sequential search failed.", e);
		}
	}
	
	public Iterator<Record> interator(int numHitsToCollect) throws AnySearcherException {
		return interator(null, numHitsToCollect);
	}
	
	public Iterator<Record> interator(String queryString, int numHitsToCollect) throws AnySearcherException {
		try {
			setQueryString(queryString);
			
			LuceneQueryBuilder queryBuilder = new LuceneQueryBuilder();
			queryBuilder.addQuery(getFilterAttributeList());
			
			if(getQueryAttributeList() == null || getQueryAttributeList().size() == 0)
				queryBuilder.addQuery(getParsedQueryString(), getRelation().getAnalyzer());
			else
				queryBuilder.addQuery(getParsedQueryString(), getQueryAttributeList().get(0), getRelation().getAnalyzer());

			Query query = queryBuilder.build();
			
			IterablePaging iter = new IterablePaging((SearchModel)this, query, numHitsToCollect);
			iter.skipTo(getStartRecord());
			iter.gather(getHitsPerPage());
			
			return iter.iterator();
		} catch(Exception e) {
			throw new AnySearcherException("search failed.", e);
		}
	}
	
	public static RecordList search(SearchModel searchModel, RecordExtractor recordExtractor) throws QueryBuilderException, RecordKeyException, IOException, ParseException {
		IndexSearcher indexSearcher = null;
		
		try {
			Directory directory = searchModel.getRelation().openDirectory();
			indexSearcher = new IndexSearcher(directory);
			
			LuceneQueryBuilder queryBuilder = new LuceneQueryBuilder();
			queryBuilder.addQuery(searchModel.getFilterAttributeList());
			
			if(searchModel.getQueryAttributeList() == null || searchModel.getQueryAttributeList().size() == 0)
				queryBuilder.addQuery(searchModel.getParsedQueryString(), searchModel.getRelation().getAnalyzer());
			else
				queryBuilder.addQuery(searchModel.getParsedQueryString(), searchModel.getQueryAttributeList().get(0), searchModel.getRelation().getAnalyzer());
			
			Query query = queryBuilder.build();
			query = indexSearcher.rewrite(query);
			
			List<SortAttribute> sortAttributeList = searchModel.getSortAttributeList();
			Sort sort = null;
			
			if(sortAttributeList != null && sortAttributeList.size() > 0)
				sort = SearchModelUtils.makeSort(searchModel.getSortAttributeList());

			ScoreDoc[] docs = null;
			
			if(sort == null) {
				TopDocs topDocs = indexSearcher.search(query, searchModel.getHitsPerPage());
				docs = topDocs.scoreDocs;
				searchModel.setTotalRecords(topDocs.totalHits);
			} else {
				TopFieldDocs topFieldDocs = indexSearcher.search(query, searchModel.getHitsPerPage(), sort);
				docs = topFieldDocs.scoreDocs;
				searchModel.setTotalRecords(topFieldDocs.totalHits);
			}

			recordExtractor.extract(indexSearcher.getIndexReader(), docs);
			
			return recordExtractor.getRecordList();

		} finally {
			try {
				if(indexSearcher != null)
					indexSearcher.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
