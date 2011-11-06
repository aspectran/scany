package org.jhlabs.scany.engine.analysis.syllabic;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

/**
 * 한글 자모 분석기
 * @author Gulendol
 *
 */
public class SyllabicAnalyzer extends Analyzer {
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new SyllabicTokenizer(reader);
	}
	
	public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
		Tokenizer tokenizer = (Tokenizer)getPreviousTokenStream();
		
		if(tokenizer == null) {
			tokenizer = new SyllabicTokenizer(reader);
			setPreviousTokenStream(tokenizer);
		} else
			tokenizer.reset(reader);
		
		return tokenizer;
	}
}
