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
package org.jhlabs.test;

import org.jhlabs.scany.client.ScanyClient;
import org.jhlabs.scany.engine.entity.RecordKey;
import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.index.AnyIndexer;

import org.apache.lucene.index.IndexReader;

public class ScanyIndexerDemo {

	public static void main(String[] args) {

		AnyIndexer anyIndexer = null;
		
		try {
			// 스키마 구분
			/*
			 * gbbs: Gulendol 보드 
			*/

			ScanyClient scanyClient = ScanySearcherDemo.getScanyClient();
			
			anyIndexer = scanyClient.getIndexer("notice");

			Relation schema = anyIndexer.getSchema();
			
			// Primary Key 생성
			RecordKey primaryKey = new RecordKey(schema);
			primaryKey.setKeyValue("boardId", "notice");
			primaryKey.setKeyValue("articleNo", "2");
			
			// 레코드 생성
			Record record = new Record();
			record.setPrimaryKey(primaryKey);
			record.addColumnValue("category", "free");
			record.addColumnValue("group", "cm");
			record.addColumnValue("title", "감 오리하이 Apple scany +fff ");
//			record.addColumnValue("content", "gulendol@aaa.com");
//			record.addColumnValue("title", "aaa");
			record.addColumnValue("content", "bbb");
			record.addColumnValue("tag", "사과,감,오렌지,파인애플,ccc,Orange");
//			record.addColumnValue("title", "오늘 록된 공지사항입니다. apple scany");
//			record.addColumnValue("content", "금수 내내 강산2 members 오늘 록된 공지사항입니다.");
			record.addColumnValue("writer", "홍길동");
			record.addColumnValue("url", "url");
//			record.addColumnValue("date", Datez.format(new Date(), "yyyyMMddHHmmss"));
			System.out.println(primaryKey.combine(schema.getKeyPattern()));
			System.out.println();
			
			int result = 1;
			
			// 색인등록
			anyIndexer.insert(record);
			
			// 색인 수정
			//anyIndexer.update(primaryKey, record);
			
			// 색인 삭제
			//result = scanyIndexer.delete(primaryKey);
			
			System.out.println();
			System.out.println("Affected: " + result);
			
			anyIndexer.optimize();
			
			anyIndexer.close();
			anyIndexer = null;
			
			IndexReader reader = IndexReader.open(schema.getRepository());
			
			System.out.println("전체: " + reader.maxDoc());
			System.out.println("유용: " + reader.numDocs());
			System.out.println();
			
/*
			for(int i = 0; i < reader.numDocs(); i++) {
				
				if(reader.isDeleted(i)) continue;
				
				Document doc = reader.document(i);
				System.out.println(doc.toString());
				System.out.println();
				
				//System.out.println(reader.document(i));
			}
*/			
			reader.close();
			
	    	// 검색
			//----------------------------------------------
/*
			WhereverSearcher searcher = new WhereverSearcher();
	    	
	    	FieldInfo[] fieldInfo = searcher.search("blog", "굴른돌", 10);

	    	for(int i = 0; i < fieldInfo.length; i++) {
	    		System.out.print("Resouce ID: ");
	    		System.out.println(fieldInfo[i].getResourceId());
	    		System.out.print("제목: ");
	    		System.out.println(fieldInfo[i].getTitle());
	    		System.out.println(fieldInfo[i].getContent());
	    		System.out.println("---------------------------------------------------------");
	    	}
*/			
   	
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(anyIndexer != null) {
				try {
					anyIndexer.close();
				} catch(Exception e2) {
					e2.printStackTrace();
				}
			}			
		}
	
	}
 
}
