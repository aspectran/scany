/**
 * 
 */
package org.jhlabs.scany.engine.search.extract;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.jhlabs.scany.engine.entity.Record;
import org.jhlabs.scany.engine.entity.RecordList;
import org.jhlabs.scany.engine.index.RecordKeyException;
import org.jhlabs.scany.engine.search.SearchModel;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 21. 오후 12:31:44</p>
 *
 */
public class SequentialRecordExtractor extends RecordExtractor {

	public SequentialRecordExtractor(SearchModel searchModel) {
		super(searchModel);
	}

	@Override
	public void extract(IndexReader indexReader, ScoreDoc[] docs) throws RecordKeyException, CorruptIndexException, IOException {
		if(docs == null)
			return;
		
		RecordList recordList = new RecordList(searchModel.getTotalRecords());

		int n = 0;
		int start = searchModel.getStartRecord();
		int end = start + searchModel.getHitsPerPage();
		
		if(searchModel.isReverse()) {
			for(int i = indexReader.maxDoc() - 1; i >= 0; i--) {
				if(!indexReader.isDeleted(i)) {					
					if(n >= start && n < end) {
						int doc = docs[i].doc;
						Record record = searchModel.createRecord(indexReader.document(doc));
						recordList.add(record);
					}
					
					n++;
				}
			}
		} else {
			for(int i = 0; i < indexReader.maxDoc(); i++) {
				if(!indexReader.isDeleted(i)) {					
					if(n >= start && n < end) {
						int doc = docs[i].doc;
						Record record = searchModel.createRecord(indexReader.document(doc));
						recordList.add(record);
					}
					
					n++;
				}
			}
		}

		if(n == 0)
			recordList = null;
			
		this.recordList = recordList;
	}
	
}
