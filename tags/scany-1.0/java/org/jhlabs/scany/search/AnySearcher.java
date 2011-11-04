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
package org.jhlabs.scany.search;

import org.jhlabs.scany.entity.PrimaryKey;
import org.jhlabs.scany.entity.Record;
import org.jhlabs.scany.entity.Schema;

/**
 * 
 * <p>Created: 2008. 01. 07 오전 7:18:00</p>
 * 
 * @author Gulendol
 *
 */
public interface AnySearcher {
	
	/**
	 * 현재 스키마를 반환한다.
	 * @return Schema
	 */
	public Schema getSchema();

	/**
	 * 스키마를 지정한다.
	 * @param schema Schema Schema
	 * @throws AnySearchException
	 */
	public void setSchema(Schema schema) throws AnySearchException;
	
	/**
	 * 레코드(Document)의 총 개수를 반환한다.
	 * @return
	 */
	public int getTotalRecords();

	/**
	 * 요약문의 최대 길이를 반환한다.
	 * @return the summaryLength 요약문 최대 길이
	 */
	public int getSummaryLength();

	/**
	 * 요약문의 최대 길이를 설정한다.
	 * @param summaryLength 요약문 최대 길이
	 */
	public void setSummaryLength(int summaryLength);

	/**
	 * 한 페이지 당 출력하는 레코드의 개수를 반환한다.
	 * @return the hitsPerPage
	 */
	public int getHitsPerPage();

	/**
	 * 한 페이지 당 출력하는 레코드의 개수를 설정한다.
	 * @param hitsPerPage 레코드의 개수
	 */
	public void setHitsPerPage(int hitsPerPage);

	/**
	 * PrimaryKey를 반환한다.
	 * 와일드카드('*', '?')가 사용된 PrimaryKey를 사용할 수 있다.
	 * 검색범위를 
	 * @return the primaryKey
	 */
	public PrimaryKey getPrimaryKey();

	/**
	 * PrimaryKey를 지정하여 검색범위를 줄인다.<pre>
	 * 와일드카드('*', '?')가 사용된 PrimaryKey를 사용할 수 있다.
	 * KeyPattern이 "groupId:boardId:articleNo" 일 경우
	 *     PrimaryKey 값으로 "site:notice:*" 지정하면
	 *     "site" 그룹의 "notice" 게시판의 모든 글에서 검색할 것이다.
	 * @param primaryKey the primaryKey to set
	 */
	public void setPrimaryKey(PrimaryKey primaryKey);
	
	/**
	 * Query Parser Syntax 문법의 종류
	 * amateur: 질의문에 기본 연산자(AND, OR, NOT)만 사용이 가능
	 * expert: 루씬에서 제공하는 Query Parser Syntax 문법을 사용
	 * @param expertQueryMode the isExpertQueryMode to set
	 */
	//public void setExpertQueryMode(boolean expertQueryMode);

	/**
	 * 정렬 컬럼을 지정한다.
	 * 해제할때는 null을 입력하자.<pre>
	 * 주의사항
	 *     컬럼의 속성에서 IsTokenized 옵션이 true 인 경우
	 *     즉, tokenized fields인 경우는 정렬이 될 수 없음을 기억하자.
	 *     IsIndexed 옵션이 false 인 경우도 정렬이 될 수 없다.</pre>
	 * @param columnName 컬럼명
	 * @param reverse 역순 정렬 여부
	 * @throws AnySearchException
	 */
	public void setSortColumn(SortColumn sortColumn) throws AnySearchException;
	
	/**
	 * 질의 가능 대상 컬럼을 추가한다.
	 * 별도로 지정하지 않을 경우 기본 질의 컬럼에서 검색한다.
	 * 기본 질의 컬럼 외에 별도로 특정 컬럼의 값을 검색할때 별도로 컬럼을 추가한다.
	 * 별도로 컬럼을 추가할시 기본 질의 컬럼은 무시된다.
	 * @param columnName 컬럼명
	 * @throws AnySearchException
	 */
	public void addQueryColumn(String columnName) throws AnySearchException;
	
	/**
	 * 필터 컬럼을 추가한다.<pre>
	 * 필터 컬럼으로 지정할 수 있는 컬럼은 다음 조건을 충족해야 한다.
	 * - 색인컬럼(Indexed)
	 * - 토큰분리컬럼(Tokened)<pre>
	 * @param columnName 컬럼명
	 * @throws AnySearchException
	 */
	public void addFilterColumn(String columnName, String keyword, boolean isEssentialClause)
	throws AnySearchException;

	/**
	 * PrimaryKey를 필터 컬럼으로 추가한다.
	 * PrimaryKey는 필수조건이 된다.
	 * @param primaryKey PrimaryKey
	 * @throws AnySearchException
	 */
	public void addFilterColumn(PrimaryKey primaryKey)
	throws AnySearchException;

	/**
	 * 컬럼별 Summarizer 지정한다.
	 * @param columnName 컬럼명
	 * @param summarizer
	 * @throws AnySearchException
	 */
	public void addSummarizer(String columnName, Summarizer summarizer)
	throws AnySearchException;
	
	/**
	 * 질의 컬럼을 모두 해제한다.
	 * 이후부터 기본 질의 컬럼으로 검색된다.
	 */
	public void clearQueryColumns();
	
	/**
	 * 필터 컬럼을 모두 해제한다.
	 */
	public void clearFilterColumns();
	
	/**
	 * 컬럼명이 맞는지 조회한다.
	 * @param columnName 컬럼명
	 * @return true or false
	 */
	public boolean isColumnName(String columnName);

	/**
	 * 질의문을 의한 검색을 수행한다.
	 * 별도로 페이지 번호를 지정하지 않으면 1 페이지를 반환한다. 
	 * @param queryString
	 * @return
	 * @throws AnySearchException
	 */
	 public Record[] search(String queryString) throws AnySearchException;
	
	/**
	 * 질의문을 의한 검색을 수행한다.
	 * 필터 컬럼만 지정했을 경우 사용한다.
	 * @param queryString
	 * @return
	 * @throws AnySearchException
	 */
	public Record[] search(int pageNo) throws AnySearchException;
	
	/**
	 * 질의문을 의한 검색을 수행한다.
	 * 빈 질의문을 지정하거나, 페이지 범위를 벗어난 페이지 번호를 지정할 경우 null을 반환한다.
	 * 검색 결과가 없으면 Record[0]을 반환한다.
	 * 색인 저장소(디렉토리)가 생성되어 있지 않으면 null을 반환하고,
	 * 빈 디렉토리 즉, 디렉토리 내에 세그먼트 파일이 존재하지 않으면 예외를 발생한다. 
	 * @param queryString 사용자가 입력한 질의문
	 * @param pageNo 페이지 번호
	 * @return hitsPerPage 개수 만큼의 Record를 반환한다.
	 * @throws AnySearchException
	 */
	public Record[] search(String queryString, int pageNo) throws AnySearchException;
	
	/**
	 * 최근 레코드 또는 랜덤 레코드를 검색한다.
	 * 검색 범위를 한정하기 위해서는 필터컬럼을 추가해야 한다.
	 * 검색 범위를 한정하지 않으면 모든 레코드에 대해 검색한다.
	 * @return hitsPerPage 개의 레코드
	 * @throws AnySearchException
	 */
	public Record[] random() throws AnySearchException;
	
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
	public Record[] random(String queryString) throws AnySearchException;

	/**
	 * 등록된 순서에 따라 도큐먼트를 페이징 한다.
	 * 엔진(루씬)의 특성상 과거에 등록된 도큐먼트가 수정되면 가장 최근 등록시간 정보를 가진다. 
	 * 색인 저장소(디렉토리)가 생성되어 있지 않으면 null을 반환하고,
	 * 빈 디렉토리 즉, 디렉토리 내에 세그먼트 파일이 존재하지 않으면 예외를 발생한다. 
	 * @param start 시작 지점
	 * @param maxRecords 레코드 개수
	 * @param reverse 역정렬 여부
	 * @return
	 * @throws AnySearchException
	 */
	public Record[] seek(int start, int maxRecords, boolean reverse) throws AnySearchException;
	
}
