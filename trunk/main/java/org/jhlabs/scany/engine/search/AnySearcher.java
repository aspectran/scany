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

import java.util.List;
import java.util.Map;

import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.RecordKey;
import org.jhlabs.scany.engine.entity.RecordList;
import org.jhlabs.scany.engine.search.summarize.Summarizer;

/**
 * 
 * <p>Created: 2008. 01. 07 오전 7:18:00</p>
 * 
 * @author Gulendol
 *
 */
public interface AnySearcher {
	
	/**
	 * 레코드(Document)의 총 개수를 반환한다.
	 * 
	 * @return
	 */
	public int getTotalRecords();

	/**
	 * 한 페이지 당 출력하는 레코드의 개수를 반환한다.
	 * 
	 * @return the hitsPerPage
	 */
	public int getHitsPerPage();

	/**
	 * 한 페이지 당 출력하는 레코드의 개수를 설정한다.
	 * 
	 * @param hitsPerPage 레코드의 개수
	 */
	public void setHitsPerPage(int hitsPerPage);

	public List<String> getQueryAttributeList();

	public void setQueryAttributeList(List<String> queryAttributeList);

	/**
	 * 질의 가능 대상 컬럼을 수동으로 추가한다.
	 * 기본 질의 대상 컬럼을 관계없이 별도의 컬럼을 지정하여 검색하기 위함이다. 
	 * 이 메쏘드를 이용해서 별도로 컬럼을 지정하지 않으면 기본 질의 대상 컬럼으로 검색한다.
	 * 
	 * @param columnName 컬럼명
	 * @throws AnySearcherException
	 */
	public void addQueryAttribute(String attributeName);
	
	public List<FilterAttribute> getFilterAttributeList();

	public void setFilterAttributeList(List<FilterAttribute> filterAttributeList);

	public void addFilterAttribute(String attributeName, String keyword, boolean essential);

	public void addFilterAttribute(RecordKey recordKey);

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
	public void addFilterAttribute(FilterAttribute filterAttribute);
	
	public List<SortAttribute> getSortAttributeList();

	public void setSortAttributeList(List<SortAttribute> sortAttributeList);
	
	public void addSortAttribute(String attributeName, SortFieldType sortFieldType, boolean reverse);

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
	public void addSortAttribute(SortAttribute sortAttribute);

	public Map<String, Summarizer> getSummarizerMap();

	public void setSummarizerMap(Map<String, Summarizer> summarizerMap);

	public void setSummarizer(String attributeName, Summarizer summarizer);
	
	public void setSummarizer(String attributeName, String summarizerId);
	
	public void clearQueryAttribute();
	
	public void clearFilterAttribute();
	
	public void clearSortAttribute();
	


	/**
	 * 질의문을 의한 검색을 수행한다.
	 * 별도로 페이지 번호를 지정하지 않으면 1 페이지를 반환한다. 
	 * @param queryString
	 * @return
	 * @throws AnySearcherException
	 */
	 public RecordList search(String queryString) throws AnySearcherException;
	
	/**
	 * 질의문을 의한 검색을 수행한다.
	 * 필터 컬럼만 지정했을 경우 사용한다.
	 * @param queryString
	 * @return
	 * @throws AnySearcherException
	 */
	public RecordList search(int pageNo) throws AnySearcherException;
	
	/**
	 * 질의문을 의한 검색을 수행한다.
	 * 빈 질의문을 지정하거나, 페이지 범위를 벗어난 페이지 번호를 지정할 경우 null을 반환한다.
	 * 검색 결과가 없으면 Record[0]을 반환한다.
	 * 색인 저장소(디렉토리)가 생성되어 있지 않으면 null을 반환하고,
	 * 빈 디렉토리 즉, 디렉토리 내에 세그먼트 파일이 존재하지 않으면 예외를 발생한다. 
	 * @param queryString 사용자가 입력한 질의문
	 * @param pageNo 페이지 번호
	 * @return hitsPerPage 개수 만큼의 Record를 반환한다.
	 * @throws AnySearcherException
	 */
	public RecordList search(String queryString, int pageNo) throws AnySearcherException;
	
	/**
	 * 최근 레코드 또는 랜덤 레코드를 검색한다.
	 * 검색 범위를 한정하기 위해서는 필터컬럼을 추가해야 한다.
	 * 검색 범위를 한정하지 않으면 모든 레코드에 대해 검색한다.
	 * @return hitsPerPage 개의 레코드
	 * @throws AnySearcherException
	 */
	public RecordList random() throws AnySearcherException;
	
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
	public RecordList random(String queryString) throws AnySearcherException;

	/**
	 * 등록된 순서에 따라 도큐먼트를 페이징 한다.
	 * 엔진(루씬)의 특성상 과거에 등록된 도큐먼트가 수정되면 가장 최근 등록시간 정보를 가진다. 
	 * 색인 저장소(디렉토리)가 생성되어 있지 않으면 null을 반환하고,
	 * 빈 디렉토리 즉, 디렉토리 내에 세그먼트 파일이 존재하지 않으면 예외를 발생한다. 
	 * @param start 시작 지점
	 * @param maxRecords 레코드 개수
	 * @param reverse 역정렬 여부
	 * @return
	 * @throws AnySearcherException
	 */
	public RecordList seek(int start, int maxRecords, boolean reverse) throws AnySearcherException;
	
}
