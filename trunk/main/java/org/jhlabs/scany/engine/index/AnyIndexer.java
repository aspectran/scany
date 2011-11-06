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

import org.jhlabs.scany.engine.entity.PrimaryKey;
import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.Schema;

/**
 * 색인 추가(insert), 색인 갱신(update), 색인 삭제(delete) 기능을 담당한다.
 * 
 * @author gulendol
 * 
 */
public interface AnyIndexer {

	/**
	 * 스키마를 반환한다.
	 * @throws AnyIndexException
	 */
	public Schema getSchema() throws AnyIndexException;

	/**
	 * 색인등록
	 * @param record
	 * @param duplicable Key 중복 허용 여부
	 * @throws AnyIndexException
	 */
	public void merge(Record record) throws AnyIndexException;
	
	/**
	 * 색인등록
	 * @param record
	 * @throws AnyIndexException
	 */
	public void insert(Record record) throws AnyIndexException;

	/**
	 * 색인삭제.
	 * 와일드카드(*)를 사용한 하위 key 일괄 삭제 가능.
	 * @param primaryKey 삭제 대상 레코드의 키
	 * @param isAutoOptimizeOff Auto Optimize 기능을 강제로 끌지 여부.
	 * @throws AnyIndexException
	 */
	public void delete(PrimaryKey primaryKey) throws AnyIndexException;
	
	/**
	 * 발생한 트랜잭션에 대해 Optimize를 수행한다.
	 */
	public void optimize() throws AnyIndexException;
	
	/**
	 * 색인DB를 완전히 삭제한다.
	 * 
	 * @throws AnyIndexException
	 */
	public void destroy() throws AnyIndexException;
	
	/**
	 * 색인 작업을 종료한다.
	 */
	public void close() throws AnyIndexException;
	
	/**
	 * 해당 Key가 존재하는 여부를 반환.
	 * 와일드카드 문자가 포함된 primaryKey를 지정해봐야 못 찾는다.
	 * @param primaryKey 레코드의 키
	 * @return
	 * @throws AnyIndexException
	 */
	public boolean exists(PrimaryKey primaryKey) throws AnyIndexException;
	
}
