package org.jhlabs.scany.engine.analysis.kr;

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

import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;
import org.jhlabs.scany.context.ScanyContext;
import org.jhlabs.scany.engine.analysis.kr.snowball.SnowballFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filters {@link StandardTokenizer} with {@link StandardFilter}, {@link
 * LowerCaseFilter} and {@link StopFilter}, using a list of English stop words.
 *
 * @version $Id: KoreanAnalyzer.java,v 1.4 2009/12/22 01:48:56 smlee0818 Exp $
 */
public final class KoreanAnalyzer extends Analyzer {
	
	private static final Logger logger = LoggerFactory.getLogger(KoreanAnalyzer.class);
	
	private static final Version matchVersion = ScanyContext.LUCENE_VERSION;

	/** Default maximum allowed token length */
	public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	/** An array containing some common English words that are usually not
	useful for searching. */
	//	  public static final String[] STOP_WORDS = StopAnalyzer.ENGLISH_STOP_WORDS;
	//	  public static final String[] KOR_STOP_WORDS = new String[]{"이","그","저","것","수","등","들"};
	public static final Set<?> ENGLISH_STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET;

	/** An unmodifiable set containing some common Korean words that are not usually useful
	  for searching.*/
	public static final Set<?> KOR_STOP_WORDS_SET;

	private Set<?> stopWordsSet;

	public static String characterEncoding = "UTF-8";

	public static String dictionaryLocation;

	private boolean bigrammable = true;

	private boolean hasOrigin = true;
	
	static {
		final List<String> korStopWords = Arrays.asList("이", "그", "저", "것", "수", "등", "들");
		final CharArraySet stopSet = new CharArraySet(matchVersion, korStopWords.size(), false);
		stopSet.addAll(korStopWords);
		KOR_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
	}

	public KoreanAnalyzer() {
		final CharArraySet stopWordsSet = new CharArraySet(matchVersion, KOR_STOP_WORDS_SET.size() + ENGLISH_STOP_WORDS_SET.size(), false);
		stopWordsSet.addAll(KOR_STOP_WORDS_SET);
		stopWordsSet.addAll(ENGLISH_STOP_WORDS_SET);
		this.stopWordsSet = stopWordsSet;
	}
	
	public TokenStream tokenStream(String fieldName, Reader reader) {
		final KoreanTokenizer src = new KoreanTokenizer(matchVersion, reader);
		TokenStream tok = new KoreanFilter(src, bigrammable, hasOrigin);
		tok = new LowerCaseFilter(matchVersion, tok);
		tok = new StopFilter(matchVersion, tok, stopWordsSet);
		tok = new SnowballFilter(tok);
		return tok;
	}

	/**
	 * determine whether the bigram index term is returned or not if a input word is failed to analysis
	 * If true is set, the bigram index term is returned. If false is set, the bigram index term is not returned.
	 * @param bigrammable
	 */
	public void setBigrammable(boolean bigrammable) {
		this.bigrammable = bigrammable;
	}
	
	/**
	 * determin whether the original term is returned or not if a input word is analyzed morphically.
	 * @param hasOrigin
	 */
	public void setHasOrigin(boolean hasOrigin) {
		this.hasOrigin = hasOrigin;
	}

	public void setDictionaryLocation(String dictionaryLocation) {
		KoreanAnalyzer.dictionaryLocation = dictionaryLocation;
		logger.trace("KoreanAnalyzer.dictionaryLocation: {}", KoreanAnalyzer.dictionaryLocation);
	}

	public static void setCharacterEncoding(String characterEncoding) {
		KoreanAnalyzer.characterEncoding = characterEncoding;
		logger.trace("KoreanAnalyzer.characterEncoding: {}", KoreanAnalyzer.characterEncoding);
	}
	
}
