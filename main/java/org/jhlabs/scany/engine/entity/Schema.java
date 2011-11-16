package org.jhlabs.scany.engine.entity;

import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.jhlabs.scany.engine.search.summarize.Summarizer;

public class Schema {

	private Map<String, Analyzer> analyzerMap;
	
	private Map<String, Summarizer> summarizerMap;
	
	private Map<String, Relation> relationMap;

	public Map<String, Analyzer> getAnalyzerMap() {
		return analyzerMap;
	}

	public void setAnalyzerMap(Map<String, Analyzer> analyzerMap) {
		this.analyzerMap = analyzerMap;
	}

	public Map<String, Summarizer> getSummarizerMap() {
		return summarizerMap;
	}

	public void setSummarizerMap(Map<String, Summarizer> summarizerMap) {
		this.summarizerMap = summarizerMap;
	}

	public Map<String, Relation> getRelationMap() {
		return relationMap;
	}

	public void setRelationMap(Map<String, Relation> relationMap) {
		this.relationMap = relationMap;
	}
	
	public Analyzer getAnalyzer(String analyzerId) {
		if(analyzerMap == null)
			return null;
		
		return analyzerMap.get(analyzerId);
	}
	
	public Summarizer getSummarizer(String summarizerId) {
		if(summarizerMap == null)
			return null;
		
		return summarizerMap.get(summarizerId);
	}
	
	public Relation getRelation(String relationId) {
		if(relationMap == null)
			return null;
		
		return relationMap.get(relationId);
	}
	
}
