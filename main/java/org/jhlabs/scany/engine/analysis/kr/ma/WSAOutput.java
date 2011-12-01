package org.jhlabs.scany.engine.analysis.kr.ma;

import java.util.ArrayList;
import java.util.List;

public class WSAOutput {

	private String source;

	private List<AnalysisOutput> results;

	public WSAOutput() {
		this(null);
	}

	public WSAOutput(String src) {
		source = src;
		results = new ArrayList<AnalysisOutput>();
	}

	public WSAOutput(String src, List<AnalysisOutput> list) {
		source = src;
		results = list;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<AnalysisOutput> getResults() {
		return results;
	}

	public void setResults(List<AnalysisOutput> results) {
		this.results = results;
	}

	public void addNounResults(String word) {
		addNounResults(word, null);
	}

	public void addNounResults(String word, String end) {
		addNounResults(word, end, AnalysisOutput.SCORE_ANALYSIS);
	}

	public void addNounResults(String word, String end, int score) {
		AnalysisOutput output = new AnalysisOutput(word, end, null, PatternConstants.PTN_NJ);
		
		if(end == null)
			output.setPatn(PatternConstants.PTN_N);

		output.setPos(PatternConstants.POS_NOUN);
		output.setScore(score);

		this.results.add(output);
	}

}
