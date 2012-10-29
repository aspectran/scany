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
public class RandomRecordExtractor extends RecordExtractor {

	public RandomRecordExtractor(SearchModel searchModel) {
		super(searchModel);
	}

	@Override
	public void extract(IndexReader indexReader, ScoreDoc[] docs) throws RecordKeyException, CorruptIndexException, IOException {
		if(docs == null)
			return;
		
		recordList = new RecordList(searchModel.getTotalRecords());
		
		if(searchModel.getHitsPerPage() < searchModel.getTotalRecords()) {
			int[] rdocs = generateRandomDocs(searchModel.getTotalRecords(), searchModel.getHitsPerPage());

			for(int i = 0; i < rdocs.length; i++) {
				if(rdocs[i] != -1) {
					int doc = docs[rdocs[i]].doc;
					Record record = searchModel.createRecord(indexReader.document(doc));
					recordList.add(record);
				}
			}
		} else {
			// SortColumn이 지정되었을 경우 0번부터 차례대로
			if(searchModel.getSortAttributeList() != null && searchModel.getSortAttributeList().size() > 0) {
				for(int i = 0; i < searchModel.getTotalRecords(); i++) {							
					int doc = docs[i].doc;
					Record record = searchModel.createRecord(indexReader.document(doc));
					recordList.add(record);
				}
				
			// SortColumn이 지정되지 않았을 경우 document 번호를 역순으로
			// 즉 최신 데이터를 먼저 내 보낸다.
			} else {
				for(int i = searchModel.getTotalRecords() - 1; i >= 0; i--) {
					int doc = docs[i].doc;
					Record record = searchModel.createRecord(indexReader.document(doc));
					recordList.add(record);
				}
			}
		}
	}
	
	/**
	 * 전체 Document 개수에서 랜덤하게 maxDocs개 만큼 Document 번호를 반환한다.
	 * 랜덤 Document 번호가 maxDocs 개에 못 미칠 경우 나머지는 -1로 채워진다.
	 * @param totalDocs 총 레코드 수
	 * @param maxDocs 가져올 최대 Document 번호 개수
	 * @return
	 */
	public static int[] generateRandomDocs(int totalDocs, int maxDocs) {
		int[] docs = new int[maxDocs];
		int docNo = -1;
		int dupCnt = 0;
		
		int cnt = 0;

		while(cnt < maxDocs) {
			if(dupCnt >= maxDocs)
				break;
			
			docNo = (int)(Math.random() * totalDocs);
			
			for(int n = 0; n < cnt; n++) {
				if(docs[n] == docNo) {
					dupCnt++;					
					continue;
				}
			}
			
			docs[cnt++] = docNo;
		}
		
		for(int i = cnt; i < maxDocs; i++) {
			docs[i] = -1;
		}
		
		// 정렬
		int buf = 0;

		for(int i = 0; i < docs.length - 1; i++) {
			for(int j = 1; j < docs.length; j++) {
				if(docs[i] > docs[j]) {
					buf = docs[i];
					docs[i] = docs[j];
					docs[j] = buf;
				}
			}
		}

		return docs;
	}
	
}
