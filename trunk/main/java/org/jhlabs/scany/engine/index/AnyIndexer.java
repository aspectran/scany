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
package org.jhlabs.scany.engine.index;

import org.jhlabs.scany.engine.entity.RecordKey;
import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.Relation;

/**
 * 색인 추가(insert), 색인 갱신(update), 색인 삭제(delete) 기능을 담당한다.
 * 
 * @author gulendol
 * 
 */
public interface AnyIndexer {

	/**
	 * 스키마를 반환한다.
	 * @throws AnyIndexerException
	 */
	public Relation getRelation() throws AnyIndexerException;

	/**
	 * 색인등록
	 * @param record
	 * @throws AnyIndexerException
	 */
	public void insert(Record record) throws AnyIndexerException;

	/**
	 * 색인수정.
	 * primaryKey에 해당하는 레코드를 삭제하고, 새로운 record를 insert한다.
	 * 
	 * @param recordKey 갱신 대상 레코드의 키
	 * @param record 수정 대상 레코드
	 * @throws AnyIndexerException
	 */
	public void update(Record record) throws AnyIndexerException;

	/**
	 * 색인등록
	 * @param record
	 * @param duplicable Key 중복 허용 여부
	 * @throws AnyIndexerException
	 */
	public void merge(Record record) throws AnyIndexerException;

	/**
	 * 색인삭제.
	 * 와일드카드(*)를 사용한 하위 key 일괄 삭제 가능.
	 * @param primaryKey 삭제 대상 레코드의 키
	 * @param isAutoOptimizeOff Auto Optimize 기능을 강제로 끌지 여부.
	 * @throws AnyIndexerException
	 */
	public void delete(RecordKey primaryKey) throws AnyIndexerException;
	
	/**
	 * 발생한 트랜잭션에 대해 Optimize를 수행한다.
	 */
	public void optimize() throws AnyIndexerException;
	
//	/**
//	 * 색인DB를 완전히 삭제한다.
//	 * 
//	 * @throws AnyIndexerException
//	 */
//	public void destroy() throws AnyIndexerException;
	
	/**
	 * 색인 작업을 종료한다.
	 */
	public void close() throws AnyIndexerException;
	
	/**
	 * 해당 Key가 존재하는 여부를 반환.
	 * 와일드카드 문자가 포함된 primaryKey를 지정해봐야 못 찾는다.
	 * @param primaryKey 레코드의 키
	 * @return
	 * @throws AnyIndexerException
	 */
	public boolean exists(RecordKey primaryKey) throws AnyIndexerException;
	
}
