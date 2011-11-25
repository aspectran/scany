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

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.jhlabs.scany.context.ScanyContext;
import org.jhlabs.scany.engine.entity.Attribute;
import org.jhlabs.scany.engine.entity.AttributeMap;
import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.engine.search.FilterAttribute;
import org.jhlabs.scany.engine.search.query.LuceneQueryBuilder;

/**
 * 색인 추가(insert), 색인 갱신(update), 색인 삭제(delete) 기능을 담당한다.
 * 
 * @author gulendol
 */
public class LuceneIndexer implements AnyIndexer {

	private Relation relation;

	private Directory directory;

	private IndexWriter indexWriter;
	
	private Analyzer analyzer;

	/**
	 * 생성자
	 * 
	 * @param relation Schema
	 * @throws MultipartRequestzException
	 */
	public LuceneIndexer(Relation relation) throws AnyIndexerException {
		this.relation = relation;
		initialize();
	}

	protected Relation getRelation() {
		return relation;
	}

	private void initialize() throws AnyIndexerException {
		try {
			directory = relation.openDirectory();
			
			IndexWriterConfig conf = new IndexWriterConfig(ScanyContext.LUCENE_VERSION, relation.getAnalyzer());  
			
			indexWriter = new IndexWriter(directory, conf);

			// Performance 설정
			//indexWriter.setMergeFactor(relation.getMergeFactor());
			//indexWriter.setMaxMergeDocs(relation.getMaxMergeDocs());
			
			if(relation.getPerFieldAnalyzer() != null)
				analyzer = relation.getPerFieldAnalyzer();
			else
				analyzer = relation.getAnalyzer();

		} catch(IOException e) {
			throw new AnyIndexerException("색인기(AnyIndexer)를 초기화할 수 없습니다.", e);
		}
	}

	public void insert(Record record) throws AnyIndexerException {
		String keyName = relation.getRecordKeyPattern().getKeyName();
		String keyValue = relation.getRecordKeyPattern().combine(record);
		
		if(exists(keyName, keyValue))
			throw new RecordAlreadyExistsException(keyName, keyValue);

		try {
			Document document = createDocument(keyName, keyValue, record); 
			indexWriter.addDocument(document, analyzer);
		} catch(Exception e) {
			throw new AnyIndexerException("색인 등록(insert)에 실패했습니다.", e);
		}
	}

	public void merge(Record record) throws AnyIndexerException {
		String keyName = relation.getRecordKeyPattern().getKeyName();
		String keyValue = relation.getRecordKeyPattern().combine(record);

		if(exists(keyName, keyValue))
			update(record);
		else
			insert(record);
	}
	
	private void update(String keyName, String keyValue, Record record) throws AnyIndexerException {
		try {
			Document document = createDocument(keyName, keyValue, record); 
			Term term = new Term(keyName, keyValue);
			indexWriter.updateDocument(term, document, analyzer);
		} catch(Exception e) {
			throw new AnyIndexerException("색인 수정(update)에 실패했습니다.", e);
		}
	}
	
	public void update(Record record) throws AnyIndexerException {
		String keyName = relation.getRecordKeyPattern().getKeyName();
		String keyValue = relation.getRecordKeyPattern().combine(record);

		update(keyName, keyValue, record);
	}
	
	public void delete(Record record) throws AnyIndexerException {
		String keyName = relation.getRecordKeyPattern().getKeyName();
		String keyValue = relation.getRecordKeyPattern().combine(record);

		delete(keyName, keyValue);
	}

	public void delete(String keyName, String keyValue) throws AnyIndexerException {
		try {
			Term term = new Term(keyName, keyValue);
			indexWriter.deleteDocuments(term);
		} catch(Exception e) {
			throw new AnyIndexerException("색인 삭제(delete)에 실패했습니다.", e);
		}
	}
	
	public void delete(List<FilterAttribute> filterAttributeList) throws AnyIndexerException {
		try {
			LuceneQueryBuilder queryBuilder = new LuceneQueryBuilder();
			queryBuilder.addQuery(filterAttributeList);
			Query query = queryBuilder.build();
				
			indexWriter.deleteDocuments(query);
		} catch(Exception e) {
			throw new AnyIndexerException("색인 삭제(delete)에 실패했습니다.", e);
		}
	}

	/**
	 * 발생한 트랜잭션에 대해 Optimize를 수행한다.
	 */
	public void optimize() throws AnyIndexerException {
		try {
			indexWriter.optimize();
		} catch(IOException e) {
			throw new AnyIndexerException("색인 최적화(optimize) 작업에 실패했습니다.", e);
		}
	}
//
//	/**
//	 * 색인DB를 완전히 삭제한다.
//	 * 
//	 * @throws AnyIndexerException
//	 */
//	public void destroy() throws AnyIndexerException {
//		initialize();
//	}
//	
	/**
	 * 색인 작업을 종료한다.
	 */
	public void close() throws AnyIndexerException {
		try {
			indexWriter.close();
		} catch(IOException e) {
			throw new AnyIndexerException("색인 작업 종료에 실패했습니다.", e);
		}
	}

	/**
	 * Commit.
	 *
	 * @throws AnyIndexerException the any indexer exception
	 */
	public void commit() throws AnyIndexerException {
		try {
			indexWriter.commit();
		} catch(IOException e) {
			throw new AnyIndexerException("색인 작업 종료에 실패했습니다.", e);
		}
	}
	
	/**
	 * Rollback.
	 *
	 * @throws AnyIndexerException the any indexer exception
	 */
	public void rollback() throws AnyIndexerException {
		try {
			indexWriter.rollback();
		} catch(IOException e) {
			throw new AnyIndexerException("색인 작업 종료에 실패했습니다.", e);
		}
	}
	
	public boolean exists(String keyName, String keyValue) throws AnyIndexerException {
		IndexReader indexReader = null;
		
		try {
			indexReader = IndexReader.open(indexWriter, false);
			
			Term term = new Term(keyName, keyValue);

			TermDocs termDocs = indexReader.termDocs(term);
			
			return termDocs.next();

		} catch(Exception e) {
			throw new AnyIndexerException("색인 존재여부 확인에 실패했습니다.", e);
		} finally {
			try {
				if(indexReader != null)
					indexReader.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 레코드(Record)를 도큐먼트(Document)로 전환 후 반환한다.
	 * @param record
	 * @return Record
	 * @throws AnyIndexerException
	 */
	private Document createDocument(String keyName, String keyValue, Record record) throws AnyIndexerException {
		try {
			AttributeMap attributeMap = relation.getAttributeMap();

			if(attributeMap.size() == 0)
				throw new IllegalArgumentException("Attributes are not defined.");

			Document document = new Document();

			// Record Key
			Field field = new Field(keyName, keyValue, Field.Store.YES, Field.Index.NOT_ANALYZED);
			document.add(field);

			Field.Index index = null;
			Field.Store store = null;
			
			for(Attribute attribute : attributeMap.values()) {
				if(keyName.equals(attribute.getName()))
					continue;
				
				String value = record.getValue(attribute.getName());

				if(!attribute.isNullable() && value == null)
					throw new NullAttributeException(attribute);

				if(value != null) {
					store = attribute.isStorable() ? Field.Store.YES : Field.Store.NO;
	
					// 색인여부, 토큰분리 여부
					if(attribute.isAnalyzable()) {
						index = Field.Index.ANALYZED; //Field.Index.TOKENIZED;
					} else {
						//index = attribute.isIndexable() ? Field.Index.UN_TOKENIZED : Field.Index.NO;
						index = attribute.isIndexable() ? Field.Index.NOT_ANALYZED : Field.Index.NO;
					}
	
					field = new Field(attribute.getName(), value, store, index, (Field.TermVector)attribute.getWithTermVector().getType());
					
					// boost factor
					field.setBoost(attribute.getBoost());
	
					document.add(field);
				}
			}

			return document;
		} catch(Exception e) {
			throw new AnyIndexerException("Failed to create Record.", e);
		}
	}
}
