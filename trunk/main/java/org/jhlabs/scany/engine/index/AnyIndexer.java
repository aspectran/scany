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

import java.util.List;

import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.search.FilterAttribute;

/**
 * 색인 추가(insert), 색인 갱신(update), 색인 삭제(delete) 기능을 담당한다.
 * 
 * @author gulendol
 * 
 */
public interface AnyIndexer {

	public void insert(Record record) throws AnyIndexerException;

	public void update(Record record) throws AnyIndexerException;

	public void merge(Record record) throws AnyIndexerException;

	public void delete(Record record) throws AnyIndexerException;
	
	public void delete(String keyName, String keyValue) throws AnyIndexerException;
	
	public void delete(List<FilterAttribute> filterAttributeList) throws AnyIndexerException;
	
	public void optimize() throws AnyIndexerException;
	
	public void close() throws AnyIndexerException;
	
	public void commit() throws AnyIndexerException;
	
	public void rollback() throws AnyIndexerException;
	
	public boolean exists(String keyName, String keyValue) throws AnyIndexerException;
	
}
