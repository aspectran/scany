package org.jhlabs.scany.analysis.bigram;

import org.jhlabs.scany.analysis.bigram.BigramTokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;

/**
 * 한글 분석을 위해 추가된다.
 * (CJKTokennizer의 버그 수정판)
 * <p>
 * Created: 2007. 01. 20 오전 3:01:38
 * </p>
 * 
 * @author Gulendol
 */
public final class BigramAnalyzer extends Analyzer {
	/**
	 * An array containing some common English words that are not usually useful
	 * for searching.
	 * ------------------------------------------------------------------------
	 * "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if",
	 * "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that",
	 * "the", "their", "then", "there", "these", "they", "this", "to", "was",
	 * "will", "with"
	 */
	public final static String[] STOP_WORDS = StopAnalyzer.ENGLISH_STOP_WORDS;

	private Set stopSet;

	/** Default maximum allowed token length */
	public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

	/**
	 * Builds an analyzer which removes words in {@link #STOP_WORDS}.
	 */
	public BigramAnalyzer() {
		this(STOP_WORDS);
	}

	/** Builds an analyzer with the given stop words. */
	public BigramAnalyzer(Set stopWords) {
		stopSet = stopWords;
	}

	/**
	 * Builds an analyzer which removes words in the provided array.
	 * 
	 * @param stopSet stop word array
	 */
	public BigramAnalyzer(String[] stopWords) {
		stopSet = StopFilter.makeStopSet(stopWords);
	}

	/**
	 * get token stream from input
	 * 
	 * @param fieldName lucene field name
	 * @param reader input reader
	 * @return TokenStream
	 */
	public TokenStream tokenStream(String fieldName, Reader reader) {
		TokenStream tokenStream = new BigramTokenizer(reader);
		tokenStream = new StandardFilter(tokenStream);
		tokenStream = new StopFilter(tokenStream, stopSet);
		// LowerCaseFilter 적용 불필요함

		return tokenStream;
	}

	public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
		SavedStreams streams = (SavedStreams)getPreviousTokenStream();
		
		if(streams == null) {
			streams = new SavedStreams();
			setPreviousTokenStream(streams);
			streams.tokenStream = new BigramTokenizer(reader);
			streams.filteredTokenStream = new StandardFilter(streams.tokenStream);
			streams.filteredTokenStream = new StopFilter(streams.filteredTokenStream, stopSet);
			// LowerCaseFilter 적용 불필요함
		} else {
			streams.tokenStream.reset(reader);
		}

		return streams.filteredTokenStream;
	}

	/**
	 * Set maximum allowed token length. If a token is seen that exceeds this
	 * length then it is discarded. This setting only takes effect the next time
	 * tokenStream or reusableTokenStream is called.
	 */
	public void setMaxTokenLength(int length) {
		maxTokenLength = length;
	}

	/**
	 * @see #setMaxTokenLength
	 */
	public int getMaxTokenLength() {
		return maxTokenLength;
	}

	private static final class SavedStreams {
		BigramTokenizer tokenStream;
		TokenStream filteredTokenStream;
	}

}
