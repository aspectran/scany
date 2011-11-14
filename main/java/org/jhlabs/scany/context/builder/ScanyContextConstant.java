package org.jhlabs.scany.context.builder;

public class ScanyContextConstant {

	// 문자상수
	public static final String YES = "yes";
	public static final String NO = "no";
	public static final String COMPRESS = "no";
	public static final String TOKENIZE = "tokenize";
	public static final String PREFIX = "prefix";
	public static final String EXPERT = "expert";

	// 분석기 별칭
	public static final String BIGRAM_ANALYZER = "bigram";
	public static final String CSV_ANALYZER = "csv";
	public static final String SYLLABIC_ANALYZER = "syllabic";

	// Performance 기본 값
	public static final int DEFAULT_MERGE_FACTOR = 10;
	public static final int DEFAULT_MAX_MERGE_DOCS = Integer.MAX_VALUE;
	
	public static final String KEY_DELIMITER = "$";
	
	public static final String PRIMARY_KEY = "_p_key_";

	public static final String[] WILDCARDS = { "?", "*" };

}
