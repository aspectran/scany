package org.jhlabs.scany.engine.analysis.bigram;

import java.io.Reader;
import java.util.Arrays;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.Version;
import org.jhlabs.scany.context.ScanyContext;

/**
 * An {@link Analyzer} that tokenizes text with {@link BigramAnalyzer} and
 * filters with {@link StopFilter}
 *
 */
public final class BigramAnalyzer extends StopwordAnalyzerBase {
	//~ Static fields/initializers ---------------------------------------------

	/**
	 * An array containing some common English words that are not usually
	 * useful for searching and some double-byte interpunctions.
	 */
	public final static String[] STOP_WORDS = { "a", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in",
			"into", "is", "it", "no", "not", "of", "on", "or", "s", "such", "t", "that", "the", "their", "then",
			"there", "these", "they", "this", "to", "was", "will", "with", "", "www" };

	//~ Instance fields --------------------------------------------------------

	/**
	 * Returns an unmodifiable instance of the default stop-words set.
	 * @return an unmodifiable instance of the default stop-words set.
	 */
	public static Set<?> getDefaultStopSet() {
		return DefaultSetHolder.DEFAULT_STOP_SET;
	}

	private static class DefaultSetHolder {
		static final Set<?> DEFAULT_STOP_SET = CharArraySet.unmodifiableSet(new CharArraySet(ScanyContext.LUCENE_VERSION,
				Arrays.asList(STOP_WORDS), false));
	}

	//~ Constructors -----------------------------------------------------------

	/**
	 * Builds an analyzer which removes words in {@link #getDefaultStopSet()}.
	 */
	public BigramAnalyzer() {
		this(ScanyContext.LUCENE_VERSION, DefaultSetHolder.DEFAULT_STOP_SET);
	}

	/**
	 * Builds an analyzer with the given stop words
	 * 
	 * @param matchVersion
	 *          lucene compatibility version
	 * @param stopwords
	 *          a stopword set
	 */
	public BigramAnalyzer(Version matchVersion, Set<?> stopwords) {
		super(matchVersion, stopwords);
	}

	//~ Methods ----------------------------------------------------------------

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		final Tokenizer source = new BigramTokenizer(reader);
		return new TokenStreamComponents(source, new StopFilter(matchVersion, source, stopwords));
	}
}
