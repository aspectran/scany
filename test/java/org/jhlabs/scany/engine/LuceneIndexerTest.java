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
import org.jhlabs.scany.engine.analysis.kr.dic.Dictionary;
import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.RecordList;
import org.jhlabs.scany.engine.index.AnyIndexer;
import org.jhlabs.scany.engine.index.AnyIndexerException;
import org.jhlabs.scany.engine.search.AnySearcher;
import org.jhlabs.scany.engine.search.AnySearcherException;
import org.jhlabs.scany.engine.search.SortFieldType;
import org.jhlabs.scany.engine.transaction.AnyTransaction;
import org.jhlabs.scany.service.AnyService;
import org.junit.Test;

public class LuceneIndexerTest {

	//@Test
	public void reloadTest() {
		Dictionary.reload();
	}
	
	//@Test
	public void transactionTest() {
		AnyService service = ScanyServiceProviderFactory.getService();
		AnyTransaction tran = null;

		try {
			tran = service.getTransaction("relation01");
			Record r = createRecord();
			tran.merge(r);
			r.setValue("articleId", "2");
			r.setValue("title", "동해물과 백두산이 마르고 닳도록~");
			tran.merge(r);
			tran.commit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void indexerTest() throws AnyIndexerException {
		AnyService service = ScanyServiceProviderFactory.getService();
		AnyIndexer indexer = null;
		
		try {
			indexer = service.getIndexer("relation01");
			Record r = createRecord();
			indexer.delete(createRecord());
			indexer.commit();
			r.setValue("title", "동해물과 백두산이 마르고 닳도록~");
			indexer.insert(r);
			indexer.optimize();
			//indexer.delete(createRecord());
			//indexer.insert(createRecord());
			//indexer.insert(createRecord());
			//indexer.rollback();
		} catch(Exception e) {
			indexer.rollback();
			
			try {
				if(indexer != null)
					indexer.close();
			} catch(AnyIndexerException e1) {
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		} finally {
			try {
				if(indexer != null)
					indexer.close();
			} catch(Exception ignored) {}
		}
	}

	//@Test
	public void searchTest() throws AnySearcherException {
		AnyService service = ScanyServiceProviderFactory.getService();
		
		try {
			AnySearcher searcher = service.getSearcher("relation01");
			searcher.addSortAttribute("articleId", SortFieldType.INT, false);
			RecordList recordList = searcher.search("감자 and (or 백두)");
			
			for(Record record : recordList) {
				System.out.println("articleId: " + record.getValue("articleId"));
				System.out.println("title: " + record.getValue("title"));
				System.out.println("content: " + record.getValue("content"));
				System.out.println("tag: " + record.getValue("tag"));
			}
		} catch(Exception e) {
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
		record.setValue("content", "010-2742-2595 a112,  N.T.N. Boy's bags. you're beatifuly girls. justipied beatifule beautify 잘 할 수 있는 우리나라 대나무감자오리김치 gulendol@aaa.com 하니코대");
		record.setValue("tag", "aaa,bbb,ccc");
		record.setValue("writer", "작성자");
		record.setValue("date", "20111122170410");
		record.setValue("url", "http://www.jhlabs.org");

		return record;
	}
 
}
