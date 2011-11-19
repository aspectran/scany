package org.jhlabs.scany.engine.search.paging;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.util.PriorityQueue;

/**
 * The {@link PagingCollector} allows for paging through lucene hits.
 * @author Aaron McCurry
 */
public class PagingCollector extends TopDocsCollector<ScoreDoc> {

	private ScoreDoc pqTop;

	private int docBase;

	private Scorer scorer;

	private ScoreDoc previousPassLowest;

	private int numHits;

	public PagingCollector(int numHits) {
		// creates an empty score doc so that i don't have to check for null
		// each time.
		this(numHits, new ScoreDoc(-1, Float.MAX_VALUE));
	}

	public PagingCollector(int numHits, ScoreDoc previousPassLowest) {
		super(new HitQueue(numHits));
		this.pqTop = pq.top();
		this.numHits = numHits;
		this.previousPassLowest = previousPassLowest;
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}

	@Override
	public void collect(int doc) throws IOException {
		float score = scorer.score();
		totalHits++;
		doc += docBase;
		
		if(score > previousPassLowest.score) {
			// this hit was gathered on a previous page.
			return;
		} else if(score == previousPassLowest.score && doc <= previousPassLowest.doc) {
			// if the scores are the same and the doc is less than or equal to
			// the
			// previous pass lowest hit doc then skip because this collector
			// favors
			// lower number documents.
			return;
		} else if(score < pqTop.score || (score == pqTop.score && doc > pqTop.doc)) {
			return;
		}
		
		pqTop.doc = doc;
		pqTop.score = score;
		pqTop = pq.updateTop();
	}

	@Override
	public void setNextReader(IndexReader reader, int docBase) throws IOException {
		this.docBase = docBase;
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;
	}

	public ScoreDoc getLastScoreDoc(TopDocs topDocs) {
		return topDocs.scoreDocs[(totalHits < numHits ? totalHits : numHits) - 1];
	}

	public ScoreDoc getLastScoreDoc(ScoreDoc[] scoreDocs) {
		return scoreDocs[(totalHits < numHits ? totalHits : numHits) - 1];
	}

	static final class HitQueue extends PriorityQueue<ScoreDoc> {

		/**
		 * Creates a new instance with <code>size</code> elements. If
		 * <code>prePopulate</code> is set to true, the queue will pre-populate itself
		 * with sentinel objects and set its {@link #size()} to <code>size</code>. In
		 * that case, you should not rely on {@link #size()} to get the number of
		 * actual elements that were added to the queue, but keep track yourself.<br>
		 * <b>NOTE:</b> in case <code>prePopulate</code> is true, you should pop
		 * elements from the queue using the following code example:
		 * 
		 * <pre>
		 * PriorityQueue pq = new HitQueue(10, true); // pre-populate.
		 * ScoreDoc top = pq.top();
		 * 
		 * // Add/Update one element.
		 * top.score = 1.0f;
		 * top.doc = 0;
		 * top = (ScoreDoc) pq.updateTop();
		 * int totalHits = 1;
		 * 
		 * // Now pop only the elements that were *truly* inserted.
		 * // First, pop all the sentinel elements (there are pq.size() - totalHits).
		 * for (int i = pq.size() - totalHits; i &gt; 0; i--) pq.pop();
		 * 
		 * // Now pop the truly added elements.
		 * ScoreDoc[] results = new ScoreDoc[totalHits];
		 * for (int i = totalHits - 1; i &gt;= 0; i--) {
		 *   results[i] = (ScoreDoc) pq.pop();
		 * }
		 * </pre>
		 * 
		 * <p><b>NOTE</b>: This class pre-allocate a full array of
		 * length <code>size</code>.
		 * 
		 * @param size
		 *          the requested size of this queue.
		 * @param prePopulate
		 *          specifies whether to pre-populate the queue with sentinel values.
		 * @see #getSentinelObject()
		 */
		protected HitQueue(int size) {
			initialize(size);
		}

		// Returns null if prePopulate is false.
		@Override
		protected ScoreDoc getSentinelObject() {
			// Always set the doc Id to MAX_VALUE so that it won't be favored by
			// lessThan. This generally should not happen since if score is not NEG_INF,
			// TopScoreDocCollector will always add the object to the queue.
			return new ScoreDoc(Integer.MAX_VALUE, Float.NEGATIVE_INFINITY);
		}

		@Override
		protected final boolean lessThan(ScoreDoc hitA, ScoreDoc hitB) {
			if(hitA.score == hitB.score)
				return hitA.doc > hitB.doc;
			else
				return hitA.score < hitB.score;
		}
	}

}
