/**
 * 
 */
package org.jhlabs.scany.engine.analysis.kr.dic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.jhlabs.scany.engine.analysis.kr.ma.WordEntry;
import org.jhlabs.scany.engine.analysis.kr.util.Trie;
import org.jhlabs.scany.util.Resources;
import org.jhlabs.scany.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 29. 오후 10:06:43</p>
 *
 */
public class DictionaryInstance {

	private static final Logger logger = LoggerFactory.getLogger(DictionaryInstance.class);

	public static final String SYLLABLE_FEATURE_DIC = "syllable.dic";
	
	public static final String TOTAL_WORDS_DIC = "total.dic";	
	
	public static final String EXTENSION_WORDS_DIC = "extension.dic";

	public static final String COMPOUNDS_DIC = "compounds.dic";	
	
	public static final String UNCOMPOUNDS_DIC = "uncompounds.dic";

	public static final String JOSA_DIC = "josa.dic";
	
	public static final String EOMI_DIC = "eomi.dic";
	
	public static final String PREFIX_DIC = "prefix.dic";
	
	public static final String SUFFIX_DIC = "suffix.dic";	
		
	public static final String CJ_DIC = "cj.dic";
	
	public static final String DIC_PROPERTIES = "org/jhlabs/scany/engine/analysis/kr/res/dic.properties";
	
	private String dictionaryLocation;

	private String encoding = "UTF-8";
	
	private Properties dicProperties;
	
	private List<char[]> syllables;
	
	private Trie<String, WordEntry> words;

	private Map<String, WordEntry> uncompounds;

	private Set<String> josas;

	private Set<String> eomis;

	private Set<String> prefixs;

	private Set<String> suffixs;

	private Map<String, String> cjwords;

	protected DictionaryInstance(String dictionaryLocation, String encoding) {
		this.dictionaryLocation = dictionaryLocation;
		if(encoding != null && encoding.length() > 0)
			this.encoding = encoding;

		logger.trace("DictionaryInstance.dictionaryLocation: {}", this.dictionaryLocation);
		logger.trace("DictionaryInstance.characterEncoding: {}", this.encoding);

		try {
			loadDictionary();
		} catch(Exception e) {
			logger.error("Dictionary loading failed.", e);
			throw new RuntimeException("Dictionary loading failed.", e);
		}
	}

	public List<char[]> getSyllables() {
		return syllables;
	}

	public Trie<String, WordEntry> getWords() {
		return words;
	}

	public Map<String, WordEntry> getUncompounds() {
		return uncompounds;
	}

	public Set<String> getJosas() {
		return josas;
	}

	public Set<String> getEomis() {
		return eomis;
	}

	public Set<String> getPrefixs() {
		return prefixs;
	}

	public Set<String> getSuffixs() {
		return suffixs;
	}

	public Map<String, String> getCjwords() {
		return cjwords;
	}
	
	public void clear() {
		syllables.clear();
		words.clear();
		uncompounds.clear();
		josas.clear();
		eomis.clear();
		prefixs.clear();
		suffixs.clear();
		cjwords.clear();
		logger.debug("Dictionary data has been cleared.");
	}

	private void loadDictionary() {
		Timer timer = new Timer();

		try {
			timer.start();
			lookupDicFiles();
			loadSyllable();
			loadWords();
			loadUncompounds();
			loadJosa();
			loadEomi();
			loadPrefix();
			loadSuffix();
			loadCJ();
			timer.stop("Dictionary Loading Time");
		} catch(Exception e) {
			logger.error("", e);
		}
	}

	private void lookupDicFiles() throws IOException {
		if(dicProperties == null)
			dicProperties = new Properties();
		else
			dicProperties.clear();
		
		InputStream is = Resources.getResourceAsStream(DIC_PROPERTIES);
		dicProperties.load(is);
		
		if(dictionaryLocation != null && dictionaryLocation.length() > 0) {
			final List<String> dicList = new ArrayList<String>();
			dicList.add(SYLLABLE_FEATURE_DIC);
			dicList.add(TOTAL_WORDS_DIC);
			dicList.add(EXTENSION_WORDS_DIC);
			dicList.add(COMPOUNDS_DIC);
			dicList.add(UNCOMPOUNDS_DIC);
			dicList.add(JOSA_DIC);
			dicList.add(EOMI_DIC);
			dicList.add(PREFIX_DIC);
			dicList.add(SUFFIX_DIC);
			dicList.add(CJ_DIC);
			
			for(String dic : dicList) {
				File file = new File(dictionaryLocation, dic);
				
				if(file.isFile() && file.canRead()) {
					dicProperties.put(dic, file);
				}
			}
		}
	}
	
	private void loadSyllable() throws IOException {
		syllables = new ArrayList<char[]>();
		DictionaryLoader loader = new DictionaryLoader(dicProperties, encoding);
		loader.loadSyllable(SYLLABLE_FEATURE_DIC, syllables);
	}
	
	private void loadWords() throws IOException {
		words = new Trie<String, WordEntry>(true);
		DictionaryLoader loader = new DictionaryLoader(dicProperties, encoding);
		loader.loadWords(TOTAL_WORDS_DIC, words);
		loader.loadWords(EXTENSION_WORDS_DIC, words);
		loader.loadCompounds(COMPOUNDS_DIC, words);
	}
	
	private void loadUncompounds() throws IOException {
		uncompounds = new HashMap<String, WordEntry>();
		DictionaryLoader loader = new DictionaryLoader(dicProperties, encoding);
		loader.loadUncompounds(UNCOMPOUNDS_DIC, uncompounds);
	}
	
	private void loadJosa() throws IOException {
		josas = new HashSet<String>();
		DictionaryLoader loader = new DictionaryLoader(dicProperties, encoding);
		loader.loadSet(JOSA_DIC, josas);
	}
	
	private void loadEomi() throws IOException {
		eomis = new HashSet<String>();
		DictionaryLoader loader = new DictionaryLoader(dicProperties, encoding);
		loader.loadSet(EOMI_DIC, eomis);
	}
	
	private void loadPrefix() throws IOException {
		prefixs = new HashSet<String>();
		DictionaryLoader loader = new DictionaryLoader(dicProperties, encoding);
		loader.loadSet(PREFIX_DIC, prefixs);
	}
	
	private void loadSuffix() throws IOException {
		suffixs = new HashSet<String>();
		DictionaryLoader loader = new DictionaryLoader(dicProperties, encoding);
		loader.loadSet(SUFFIX_DIC, suffixs);
	}
	
	private void loadCJ() throws IOException {
		cjwords = new HashMap<String, String>();
		DictionaryLoader loader = new DictionaryLoader(dicProperties, encoding);
		loader.loadMap(CJ_DIC, cjwords);
	}

}
