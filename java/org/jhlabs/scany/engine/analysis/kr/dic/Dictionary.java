package org.jhlabs.scany.engine.analysis.kr.dic;

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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jhlabs.scany.engine.analysis.kr.KoreanAnalyzer;
import org.jhlabs.scany.engine.analysis.kr.ma.MorphException;
import org.jhlabs.scany.engine.analysis.kr.ma.WordEntry;
import org.jhlabs.scany.engine.analysis.kr.util.Trie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dictionary {

	private static final Logger logger = LoggerFactory.getLogger(Dictionary.class);

	private static String dictionaryLocation;

	private static String characterEncoding;

	private static DictionaryInstance instance;

	private static boolean isLoading;

	private static List<char[]> syllables;

	private static Trie<String, WordEntry> words;

	private static Set<String> josas;

	private static Set<String> eomis;

	private static Set<String> prefixs;

	private static Set<String> suffixs;

	private static Map<String, WordEntry> uncompounds;

	private static Map<String, String> cjwords;

	static {
		if(!isLoading && instance == null) {
			isLoading = true;
			load();
			isLoading = false;
		}
	}

	private static void load() {
		isLoading = true;
		dictionaryLocation = KoreanAnalyzer.dictionaryLocation;
		characterEncoding = KoreanAnalyzer.characterEncoding;
		logger.trace("Dictionary.dictionaryLocation: {}", Dictionary.dictionaryLocation);
		logger.trace("Dictionary.characterEncoding: {}", Dictionary.characterEncoding);
		instance = new DictionaryInstance(dictionaryLocation, characterEncoding);
		syllables = instance.getSyllables();
		words = instance.getWords();
		josas = instance.getJosas();
		eomis = instance.getEomis();
		prefixs = instance.getPrefixs();
		suffixs = instance.getSuffixs();
		uncompounds = instance.getUncompounds();
		cjwords = instance.getCjwords();
	}

	public static void reload() {
		if(!isLoading && instance != null) {
			logger.info("Dictionary reloading.");
			isLoading = true;
			instance.clear();
			instance = null;
			load();
			isLoading = false;
		} else {
			logger.error("Dictionary reloading failed.");
		}
	}

	public static Iterator findWithPrefix(String prefix) {
		return words.getPrefixedBy(prefix);
	}

	public static WordEntry getWord(String key) {
		if(key.length() == 0)
			return null;

		return (WordEntry)words.get(key);
	}

	public static WordEntry getWordExceptVerb(String key) {
		WordEntry entry = getWord(key);
		if(entry == null)
			return null;

		if(entry.getFeature(WordEntry.IDX_NOUN) == '1' || entry.getFeature(WordEntry.IDX_BUSA) == '1')
			return entry;
		return null;
	}

	public static WordEntry getNoun(String key) {
		WordEntry entry = getWord(key);
		if(entry == null)
			return null;

		if(entry.getFeature(WordEntry.IDX_NOUN) == '1')
			return entry;
		return null;
	}

	public static WordEntry getCNoun(String key) {
		WordEntry entry = getWord(key);
		if(entry == null)
			return null;

		if(entry.getFeature(WordEntry.IDX_NOUN) == '1' || entry.getFeature(WordEntry.IDX_NOUN) == '2')
			return entry;
		return null;
	}

	public static WordEntry getVerb(String key) {
		WordEntry entry = getWord(key);
		if(entry == null)
			return null;

		if(entry.getFeature(WordEntry.IDX_VERB) == '1') {
			return entry;
		}
		return null;
	}

	public static WordEntry getAdverb(String key) {
		WordEntry entry = getWord(key);
		if(entry == null)
			return null;

		if(entry.getFeature(WordEntry.IDX_BUSA) == '1')
			return entry;
		return null;
	}

	public static WordEntry getBusa(String key) {
		WordEntry entry = getWord(key);
		if(entry == null)
			return null;

		if(entry.getFeature(WordEntry.IDX_BUSA) == '1' && entry.getFeature(WordEntry.IDX_NOUN) == '0')
			return entry;
		return null;
	}

	public static WordEntry getIrrVerb(String key, char irrType) {
		WordEntry entry = getWord(key);
		if(entry == null)
			return null;

		if(entry.getFeature(WordEntry.IDX_VERB) == '1' && entry.getFeature(WordEntry.IDX_REGURA) == irrType)
			return entry;
		return null;
	}

	public static WordEntry getBeVerb(String key) {
		WordEntry entry = getWord(key);
		if(entry == null)
			return null;

		if(entry.getFeature(WordEntry.IDX_BEV) == '1')
			return entry;
		return null;
	}

	public static WordEntry getDoVerb(String key) {
		WordEntry entry = getWord(key);
		if(entry == null)
			return null;

		if(entry.getFeature(WordEntry.IDX_DOV) == '1')
			return entry;
		return null;
	}

	public static WordEntry getUncompound(String key) {
		return uncompounds.get(key);
	}

	public static String getCJWord(String key) {
		return cjwords.get(key);
	}

	public static boolean existJosa(String str) {
		return josas.contains(str);
	}

	public static boolean existEomi(String str) {
		return eomis.contains(str);
	}

	public static boolean existPrefix(String str) {
		return prefixs.contains(str);
	}

	public static boolean existSuffix(String str) {
		return suffixs.contains(str);
	}

	/**
	 * 인덱스 값에 해당하는 음절의 특성을 반환한다.
	 * 영자 또는 숫자일 경우는 모두 해당이 안되므로 가장 마지막 글자인 '힣' 의 음절특성을 반환한다.
	 * 
	 * @param idx '가'(0xAC00)이 0부터 유니코드에 의해 한글음절을 순차적으로 나열한 값
	 * @return
	 * @throws Exception 
	 */
	public static char[] getSyllableFeature(int idx) throws MorphException {
		if(idx < 0)
			return syllables.get(syllables.size() - 1);
		else if(idx < syllables.size())
			return syllables.get(idx);

		return syllables.get(syllables.size() - 1);
	}

	/**
	 * 각 음절의 특성을 반환한다.
	 * @param syl  음절 하나
	 * @return
	 * @throws Exception 
	 */
	public static char[] getSyllableFeature(char syl) throws MorphException {
		int idx = syl - 0xAC00;
		return getSyllableFeature(idx);
	}

	/**
	 * ㄴ,ㄹ,ㅁ,ㅂ과 eomi 가 결합하여 어미가 될 수 있는지 점검한다.
	 * @param s
	 * @param end
	 * @return
	 */
	public static String combineAndEomiCheck(char s, String eomi) {
		if(eomi == null)
			eomi = "";

		if(s == 'ㄴ')
			eomi = "은" + eomi;
		else if(s == 'ㄹ')
			eomi = "을" + eomi;
		else if(s == 'ㅁ')
			eomi = "음" + eomi;
		else if(s == 'ㅂ')
			eomi = "습" + eomi;
		else
			eomi = s + eomi;

		if(existEomi(eomi))
			return eomi;

		return null;
	}
}
