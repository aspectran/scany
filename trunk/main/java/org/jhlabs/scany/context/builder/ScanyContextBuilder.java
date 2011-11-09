/*******************************************************************************
 * Copyright (c) 2008 Jeong Ju Ho.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Jeong Ju Ho - initial API and implementation
 ******************************************************************************/
package org.jhlabs.scany.context.builder;

import org.jhlabs.scany.context.ScanyContextException;
import org.jhlabs.scany.engine.analysis.bigram.BigramAnalyzer;
import org.jhlabs.scany.engine.analysis.csv.CSVAnalyzer;
import org.jhlabs.scany.engine.analysis.syllabic.SyllabicAnalyzer;
import org.jhlabs.scany.engine.entity.Attribute;
import org.jhlabs.scany.engine.entity.Relation;
import org.jhlabs.scany.util.MultipleXReader;
import org.jhlabs.scany.util.NumberUtils;
import org.jhlabs.scany.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;

/**
 * Scany의 전반적인 설정정보를 담고 있다.
 * 
 * @author gulendol
 *
 */
public class ScanyContextBuilder {
	
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

	
	public static Map analyzers;
	
	static {
		analyzers = new HashMap();
		analyzers.put(BIGRAM_ANALYZER, new BigramAnalyzer());
		analyzers.put(CSV_ANALYZER, new CSVAnalyzer());
		analyzers.put(SYLLABIC_ANALYZER, new SyllabicAnalyzer());
	}

	/**
	 * 분석기명에 해당하는 분석기를 반환한다.
	 * @param analyzerName 분석기명 또는 외부분석기의 클래스
	 * @return
	 */
	private Analyzer getAnalyzer(String analyzerName) {
		if(analyzerName == null)
			return null;
		
		Analyzer analyzer = (Analyzer)analyzers.get(analyzerName);
		
		// 외부 분석기
		if(analyzer == null) {
			try {
				Class type = Class.forName(analyzerName);
				
				Object bean = type.newInstance();
				
				analyzer = (Analyzer)bean;
				
				analyzers.put(analyzerName, analyzer);
			} catch(Exception e) {
				throw new ScanyContextException("외부 분석기(Analyzer) 생성에 실패했습니다. (" + analyzerName + ")", e);
			}
		}
		
		return analyzer;
	}
	
	/**
	 * 스키마 리소스 정보를 불러온다.
	 * @return
	 * @throws CommonException
	 */
	/*
	public MultipleXReader.Record loadServerConfig(String scanyHome) throws CommonException {
		String[] columnNames = {
				"RepositoryHome",
				"CharacterSet",
				"Port",
				"MaxThreads",
				"User",
				"Password"
		};
		
		try {
			MultipleXReader xreader = new MultipleXReader();
			xreader.setMetadata("GulendolScanyServer", "Server", columnNames);
			
			File file = new File(scanyHome + "conf", "server.xml");

			List list = xreader.read(file);

			if(list == null || list.size() == 0)
				throw new CommonException("Scany Server 설정정보가 올바르지 않습니다.");
			
			return (MultipleXReader.Record)list.get(0);

		} catch(Exception e) {
			throw new CommonException("Scany Server 설정정보를 읽을 수 없습니다.", e);
		}
	}
	*/

	/**
	 * 스키마 리소스 정보를 불러온다.
	 * @return
	 * @throws CommonException
	 */
	public MultipleXReader.Record loadClientConfig(InputStream is, String serviceName) throws ScanyContextException {
		String[] columnNames = {
				"ScanyHome",
				"RopositoryHome",
				"ScanyHome",
				"CharacterSet"
		};

		try {
			MultipleXReader xreader = new MultipleXReader();
			xreader.setMetadata("ScanyClient", "Service", columnNames);
			
			List list = xreader.read(is);

			if(list == null || list.size() == 0)
				throw new IllegalArgumentException("Scany Client 설정정보가 올바르지 않습니다.");
			
			for(int i = 0; i < list.size(); i++) {
				MultipleXReader.Record record = (MultipleXReader.Record)list.get(i);
				String tempServiceName = record.getAttributeValue("name");
				
				if(tempServiceName.equals(serviceName))
					return record;
			}

			throw new IllegalArgumentException("서비스명 \"" + serviceName + "\" 이 존재하지 않습니다.");

		} catch(Exception e) {
			throw new ScanyContextException("Scany Client 설정정보를 읽을 수 없습니다.", e);
		}
	}
	
	/**
	 * 스키마 설정정보를 로딩한다.
	 * @param resource
	 * @return
	 * @throws CommonException
	 */
	public Map loadSchemas(String schemasXml, String repositoryHome) throws ScanyContextException {
		String[] columnNames = {
				"KeyPattern",
				"Repository",
				"Description",
				"Analyzer",
				"DefaultQueryMode",
				"MergeFactor",
				"MaxMergeDocs",
				"Column"
		};
		
		try {
			Map schemas = new HashMap();
			
			MultipleXReader xreader = new MultipleXReader();
			xreader.setMetadata("ScanySchemas", "Schema", columnNames);

			File schemasFile = new File(schemasXml);
			
			List list = xreader.read(schemasFile);
			
			if(list != null) {
				for(int i = 0; i < list.size(); i++) {
					MultipleXReader.Record record = (MultipleXReader.Record)list.get(i);

					Relation schema = new Relation();
					schema.setSchemaId(record.getAttributeValue("name"));

					// 중복되는 스키마 ID가 있는지 검사
					if(schemas.get(schema.getSchemaId()) != null)
						throw new IllegalArgumentException("중복된 Schema ID가 존재합니다. - (" + schema.getSchemaId() + ")");

					// 키패턴
					schema.setKeyPattern(record.getColumnValue(columnNames[0]));

					// 저장소
					String repository = record.getColumnValue(columnNames[1]);
					
					if(repository == null)
						throw new IllegalArgumentException("데이터 저장소가 지정되지 않았습니다. - (" + schema.getSchemaId() + ")");
						
					if(repository.indexOf("{RepositoryHome}") != -1) {
						if(!StringUtils.isEmpty(repositoryHome)) {
							String temp = repositoryHome;
							
							if(repositoryHome.endsWith(File.separator))
								temp = repositoryHome.substring(0, repositoryHome.length() - 1);
							else
								temp = repositoryHome;
	
							repository = StringUtils.replace(repository, "{RepositoryHome}", temp);
						}
					}
					
					schema.setRepository(repository);
					
					// 참고설명
					schema.setDescription(record.getColumnValue(columnNames[2]));
					
					// 기본 분석기
					Analyzer defaultAnalyzer = getAnalyzer(record.getColumnValue(columnNames[3]));

					if(defaultAnalyzer == null)
						throw new IllegalArgumentException("기본 분석기(analyzer)를 지정할 수 없습니다.");

					/**
					 * This analyzer is used to facilitate scenarios where different
					 * fields require different analysis techniques.
					 */
					PerFieldAnalyzerWrapper analyzerWrapper = new PerFieldAnalyzerWrapper(defaultAnalyzer);
					
					schema.setAnalyzer(analyzerWrapper);
					
					// 전문가용 Query Parser Syntax 문법의 사용여부
					schema.setExpertQueryMode(EXPERT.equals(record.getColumnValue(columnNames[4])));
					
					// Perfomance
					schema.setMergeFactor(NumberUtils.parseInt(record.getColumnValue(columnNames[5]), DEFAULT_MERGE_FACTOR));
					schema.setMergeFactor(NumberUtils.parseInt(record.getColumnValue(columnNames[6]), DEFAULT_MAX_MERGE_DOCS));
					
					// 컬럼
					MultipleXReader.Column[] xcolumns = record.getColumns(columnNames[7]);
					
					Attribute[] columns = new Attribute[xcolumns.length];
					
					for(int j = 0; j < xcolumns.length; j++) {
						columns[j] = new Attribute();
						columns[j].setName(xcolumns[j].getAttributeValue("name"));
						columns[j].setStorable(YES.equals(xcolumns[j].getAttributeValue("storable")));
						columns[j].setCompressable(COMPRESS.equals(xcolumns[j].getAttributeValue("storable")));
						columns[j].setIndexable(YES.equals(xcolumns[j].getAttributeValue("indexable")));
						columns[j].setTokenizable(TOKENIZE.equals(xcolumns[j].getAttributeValue("indexable")));
						columns[j].setQueryable(YES.equals(xcolumns[j].getAttributeValue("queryable")));
						columns[j].setPrefixQueryable(PREFIX.equals(xcolumns[j].getAttributeValue("queryable")));
						columns[j].setDescription(xcolumns[j].getValue());
						columns[j].setBoost(NumberUtils.parseFloat(xcolumns[j].getAttributeValue("boost"), 1.0f));
						
						// 토큰이 가능한 컬럼은 색인도 가능
						if(columns[j].isTokenizable())
							columns[j].setIndexable(true);
						
						// Smart질의 가능한 컬럼은 그냥 질의도 가능
						if(columns[j].isPrefixQueryable())
							columns[j].setQueryable(true);
						
						// 컬럼별 분석기
						String analyzerName = xcolumns[j].getAttributeValue("analyzer");
						
						if(!StringUtils.isEmpty(analyzerName)) {
							Analyzer analyzer = getAnalyzer(analyzerName);
							
							if(analyzer == null)
								throw new IllegalArgumentException("알 수 없는 컬럼(Column)의 분석기(analyzer)입니다.");

							analyzerWrapper.addAnalyzer(columns[j].getName(), analyzer);
							columns[j].setAnalyzer(analyzer);
						} else {
							columns[j].setAnalyzer(defaultAnalyzer);
						}
					}

					schema.setColumns(columns);
					
					schemas.put(schema.getSchemaId(), schema);
					
//					System.out.println(schema.getSchemaId());
//					System.out.println(schema.getRepository());
				}
			}
			
			return schemas;
			
		} catch(Exception e) {
			throw new ScanyContextException("Scany 스키마 설정정보를 불러 올 수 없습니다.", e);
		}
	}
	
}
