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
import org.jhlabs.scany.engine.summarize.SimpleFragmentSummarizer;
import org.jhlabs.scany.util.StringUtils;

import java.util.Iterator;

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
public class AnySmartSearcher extends AnyExpertSearcher implements AnySearcher {
	
	/**
	 * 생성자
	 * 검색하기 전에 반드시 스키마를 지정해야 한다.
	 */
	public AnySmartSearcher() {
		super();
	}

	/**
	 * 생성자
	 * @param schema 스키마
	 * @throws ScanySearchException
	 */
	public AnySmartSearcher(Table schema) throws AnySearchException {
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
		SmartQueryParser parser = new SmartQueryParser(getQueryColumns());

		Record[] records = super.search(parser.parse(queryString), pageNo);
		
		if(getSummarizers() != null)
			records = summarize(parser.getKeywords(), records);
		
		return records;
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
		SmartQueryParser parser = new SmartQueryParser(getQueryColumns());

		Record[] records = super.random(parser.parse(queryString));

		if(getSummarizers() != null)
			records = summarize(parser.getKeywords(), records);
		
		return records;
	}
	
	protected Record[] summarize(String[] keywords, Record[] records) {
		Iterator it = (Iterator)summarizers.keySet().iterator();
		
		while(it.hasNext()) {
			String columnName = (String)it.next();
			SimpleFragmentSummarizer summarizer = (SimpleFragmentSummarizer)summarizers.get(columnName);
			summarizer.setKeywords(keywords);
			
			for(int i = 0; i < records.length; i++) {
				String content = records[i].getColumnValue(columnName);
				
				if(!StringUtils.isEmpty(content)) {
					records[i].addColumnValue(columnName, summarizer.summarize(content));
				}
			}
		}
		
		return records;
	}

}
