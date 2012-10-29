/**
 * 
 */
package org.jhlabs.scany.engine.search.extract;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
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
public class PagingRecordExtractor extends RecordExtractor {

	public PagingRecordExtractor(SearchModel searchModel) {
		super(searchModel);
	}

	@Override
	public void extract(IndexReader indexReader, ScoreDoc[] docs) throws RecordKeyException, CorruptIndexException, IOException {
		if(docs == null)
			return;
		
		recordList = new RecordList(searchModel.getTotalRecords());

		if(searchModel.getPage() == 1) {
			recordList = searchModel.populateRecordList(indexReader, docs, 0, searchModel.getHitsPerPage());
		} else {
			int start = searchModel.getHitsPerPage() * searchModel.getPage() - searchModel.getHitsPerPage();
			int end = Math.min(start + searchModel.getHitsPerPage() - 1, searchModel.getTotalRecords() - 1);

			recordList = searchModel.populateRecordList(indexReader, docs, start, end);
		}
	}
	
}
