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
package org.jhlabs.scany.engine;

import org.jhlabs.scany.ScanyServiceProviderFactory;
import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.index.AnyIndexerException;
import org.jhlabs.scany.engine.transaction.AnyTransaction;
import org.jhlabs.scany.service.AnyService;
import org.junit.Before;
import org.junit.Test;

public class LuceneIndexerTest {

	@Before
	public void before(){
		System.out.println("Start.");
	}
	
	@Test
	public void insert() {
		AnyService service = ScanyServiceProviderFactory.getAnyService();
		AnyTransaction tran = null;

		try {
			tran = service.getTransaction("relation01");
			tran.begin();
			tran.merge(createRecord());
			//tran.insert(createRecord());
			tran.commit();
		} catch(Exception e) {
			try {
				if(tran != null)
					tran.rollback();
			} catch(AnyIndexerException e1) {
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		}
	}
	
	public Record createRecord() {
		// 레코드 생성
		Record record = new Record();
		record.setValue("boardId", "100");
		record.setValue("articleId", "1");
		record.setValue("group", "abcdef");
		record.setValue("category", "cate01");
		record.setValue("category", "cate01");
		record.setValue("title", "감 오리하이 Apple scany +fff ");
		record.setValue("content", "감자오리김치 gulendol@aaa.com");
		record.setValue("tag", "aaa,bbb,ccc");
		record.setValue("writer", "작성자");
		record.setValue("date", "20111122170410");
		record.setValue("url", "http://www.jhlabs.org");

		return record;
	}
 
}
