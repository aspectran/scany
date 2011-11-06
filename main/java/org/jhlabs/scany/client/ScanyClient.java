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
package org.jhlabs.scany.client;

import org.jhlabs.scany.engine.entity.Schema;
import org.jhlabs.scany.engine.index.AnyIndexer;
import org.jhlabs.scany.engine.search.AnySearcher;

/**
 * 
 * <p>Created: 2008. 01. 07 오전 12:52:46</p>
 * 
 * @author Gulendol
 *
 */
public interface ScanyClient {

	/**
	 * 스키마(Schema)를 가져온다.
	 * @param schemaId
	 * @return
	 */
	public Schema getSchema(String schemaId);
	
	/**
	 * Scany 검색기(AnySearcher)를 생성후 반환한다.
	 * @param schemaId 스키마 ID
	 * @return
	 */
	public AnySearcher getSearcher(String schemaId);
	
	/**
	 * Scany 검색기(AnySearcher)를 생성후 반환한다.
	 * @param schema 스키마
	 * @return
	 */
	public AnySearcher getSearcher(Schema schema);
	
	/**
	 * Scany 색인기(AnyIndexer)를 생성후 반환한다.
	 * @param schemaId 스키마 ID
	 * @return
	 */
	public AnyIndexer getIndexer(String schemaId);
	
	/**
	 * Scany 색인기(AnyIndexer)를 생성후 반환한다.
	 * @param schema 스키마
	 * @return
	 */
	public AnyIndexer getIndexer(Schema schema);

	/**
	 * Scany Home Directory를 반환한다.
	 * @return the scanyHome
	 */
	public String getScanyHome();
	
	/**
	 * 색인 저장소 경로를 반환한다.
	 */
	public String getRepositoryHome();

}
