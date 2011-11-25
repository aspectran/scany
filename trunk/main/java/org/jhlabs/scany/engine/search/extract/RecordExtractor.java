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
public abstract class RecordExtractor {

	protected SearchModel searchModel;

	protected RecordList recordList;

	public RecordExtractor(SearchModel searchModel) {
		this.searchModel = searchModel;
	}

	public abstract void extract(IndexReader indexReader, ScoreDoc[] docs) throws RecordKeyException,
			CorruptIndexException, IOException;

	public RecordList getRecordList() {
		return recordList;
	}

}
