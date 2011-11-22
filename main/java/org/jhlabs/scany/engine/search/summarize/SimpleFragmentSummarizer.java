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
package org.jhlabs.scany.engine.search.summarize;

import org.jhlabs.scany.engine.search.query.QueryStringParser;
import org.jhlabs.scany.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 검색단어의 출현 빈도에 따라 텍스트 내용에서 요약글을 추출한 후
 * 하이라이팅 처리를 담당한다.
 * 
 * <p>Created: 2007. 01. 19 오전 1:41:39</p>
 * 
 * @author Gulendol
 *
 */
public class SimpleFragmentSummarizer implements Summarizer {
	
	/**
	 * 요약문의 최대 길이
	 */
	private int summarizeLength = 250;
	
	/**
	 * 요약문 내의 단문 최대 길이
	 */
	private int contextLength = 80;
	
	/**
	 * 줄임말
	 */
	private String ellipsisTag = "<strong> ... </strong>";
	
	/**
	 * 기본 하이라이트 태그
	 */
	private String[] highlightTags = { "<strong>", "</strong>" };
	
	/**
	 * 생성자
	 */
	public SimpleFragmentSummarizer() {
	}
	
	/**
	 * 요약문장 내의 짧은 문장의 길이를 반환한다.
	 * @return the contextLength 요약문에서 단문의 최대 길이
	 */
	public int getContextLength() {
		return contextLength;
	}

	/**
	 * 요약문에서 단문의 길이를 설정한다.
	 * @param contextLength 요약문에서 단문의 최대 길이
	 */
	public void setContextLength(int contextLength) {
		this.contextLength = contextLength;
	}

	/**
	 * 요약문의 최대 길이를 반환한다.
	 * @return the summarizeLength 요약문의 최대 길이
	 */
	public int getSummarizeLength() {
		return summarizeLength;
	}

	/**
	 * 요약문의 최대 길이를 설정한다.
	 * @param summarizeLength 요약문의 최대 길이
	 */
	public void setSummarizeLength(int summarizeLength) {
		this.summarizeLength = summarizeLength;
	}
	
	/**
	 * 줄임문자열을 지정한다.
	 * @param ellipsisTag
	 */
	public void setEllipsisTag(String ellipsisTag) {
		this.ellipsisTag = ellipsisTag;
	}

	/**
	 * 하이라이팅 처리 스타일을 지정한다.
	 * @param prefixTag &lt;b&gt;
	 * @param suffixTag &lt;/b&gt;
	 */
	public void setHighlightTags(String prefixTag, String suffixTag) {
		this.highlightTags[0] = prefixTag;
		this.highlightTags[1] = suffixTag;
	}
	
	/**
	 * 하이라이팅 처리 스타일을 지정한다.
	 * @param prefixTag &lt;b&gt;
	 * @param suffixTag &lt;/b&gt;
	 */
	public void setHighlightCloseTag(String closeTag) {
		this.highlightTags[1] = closeTag;
	}
	
	/**
	 * 문단나눔 정보 클래스
	 * @author Gulendol
	 *
	 */
	private class Fragment implements Comparable<Fragment> {
		public int start = 0;
		public int end = 0;
		public int score = 1;
		public boolean use = true;
		
		public int length() {
			return end - start;
		}
		
		public void take(Fragment f2, int maxContextLen) {
			int min = Math.min(this.start, f2.start);
			int max = Math.max(this.end, f2.end);
			
			if(max - min < maxContextLen) {
				this.start = min;
				this.end = max;
				this.score++;
				f2.use = false;
			}
		}
		
		public int compareTo(Fragment obj) {
			Fragment f = obj;
			
			if(this.score > f.score) return 1;
			if(this.score < f.score) return -1;
			return 0;
		}
	}
	
	public String summarize(String queryString, String content) {
		String[] keywords = extractKeywords(queryString);
		
		return summarize(keywords, content);
	}

	/**
	 * 하이라이팅 처리된 요약문장을 반환한다.
	 * @param content 내용
	 * @return 요약문
	 */
	public String summarize(String[] keywords, String content) {
		String[] terms = keywords;
		
		if(terms != null) {
			for(int i = 0; i < terms.length; i++)
				terms[i] = terms[i].toLowerCase();

			List fragments = fragmentize(terms, content);
			
			// Frag Context 조합
			StringBuffer sb = new StringBuffer();
			
			int totalLen = 0;
	
			for(int i = 0; i < fragments.size(); i++) {
				Fragment f1 = (Fragment)fragments.get(i);
				
				if(!f1.use)
					continue;
			
				totalLen += f1.length();
				
				if(totalLen <= summarizeLength) {
					if(i == 0 && f1.start > 0)
						sb.append(ellipsisTag);
					
					sb.append(content.substring(f1.start, f1.end).trim());
					
					if(f1.end < content.length())
						sb.append(ellipsisTag);
				}
				
				// for Debug			
				//* 
				System.out.println(f1.start + " ~ " + f1.end + " : " + (f1.end - f1.start));	
				System.out.println("≪" + content.substring(f1.start, f1.end) + "≫");
				System.out.println();
				System.out.println(sb.toString());
				System.out.println();
				//*/
			}
	
			if(sb.length() > 0) {
				// Highlight
				return highlight(terms, sb).toString();
			}
		}
		
		// 찾는 키워드가 본문에 없을 경우
		String result = null;

		if(content.length() > summarizeLength) {
			result = content.substring(0, summarizeLength).trim();

			if(result.length() > 0)
				result += ellipsisTag;
		} else
			result = content.trim();
		
		return result;
	}

	/**
	 * 하이라이팅 처리 후 결과를 반환한다.
	 * @param content
	 * @return StringBuffer
	 */
	protected StringBuffer highlight(String[] terms, StringBuffer content) {
		if(StringUtils.isEmpty(highlightTags[0]) && StringUtils.isEmpty(highlightTags[1]))
			return content;
		
		String lowercaseContent = content.toString().toLowerCase();

		int offset;
		int add;
		
		for(int i = 0; i < terms.length; i++) {
			offset = 0;
			add = 0;

			while((offset = lowercaseContent.indexOf(terms[i], offset)) != -1) {
				if(highlightTags[0] != null) {
					content.insert(offset + add, highlightTags[0]);
					add += highlightTags[0].length();
				}

				offset += terms[i].length();

				if(highlightTags[1] != null) {
					content.insert(offset + add, highlightTags[1]);
					add += highlightTags[1].length();
				}
			}
			
			lowercaseContent = content.toString().toLowerCase();
		}

		return content;
	}

	/**
	 * 하이라이트 처리를 후 결과를 반환한다.
	 * @param content 내용
	 * @return
	 */
	public String highlight(String[] terms, String content) {
		StringBuffer sb = new StringBuffer(content);
		return highlight(terms, sb).toString();
	}

	/**
	 * 내용에서 Fragment 추출한다.
	 * @param content
	 * @return
	 */
	private List fragmentize(String[] terms, String content) {
		String lowercaseContent = content.toLowerCase();
		
		int termLength = (terms == null) ? 0 : terms.length;
		
		List[] fragments = new ArrayList[termLength]; 
		List contexts = new ArrayList();

		// 키워드 찾아서 Fragment 생성
		if(terms != null) {
			for(int i = 0; i < termLength; i++) {
				fragments[i] = new ArrayList();
				
				int offset = 0;
				
				while((offset = lowercaseContent.indexOf(terms[i], offset)) != -1) {
					Fragment f1 = new Fragment();
					f1.start = offset;
					f1.end = offset + terms[i].length();
	
					fragments[i].add(f1);
					
					offset += terms[i].length();
				}
			}
		}

		// 키워드 이웃 통합 : 전체 Fragment 비교
		for(int i = 0; i < fragments.length - 1; i++) {
			for(int j = 0; j < fragments[i].size(); j++) {
				Fragment f1 = (Fragment)fragments[i].get(j);
				
				if(f1.use) {
					for(int k = 0; k < fragments[i + 1].size(); k++) {
						Fragment f2 = (Fragment)fragments[i + 1].get(k);
						
						if(f2.use)
							f1.take(f2, summarizeLength);
					}
				}
			}
		}
		
		// 키워드 이웃 통합 : 동일 키워드의 Fragment 끼리 한 번 더
		for(int i = 0; i < fragments.length; i++) {
			for(int j = 0; j < fragments[i].size() - 1; j++) {
				Fragment f1 = (Fragment)fragments[i].get(j);
				
				if(f1.use) {
					Fragment f2 = (Fragment)fragments[i].get(j + 1);
					
					if(f2.use)
						f1.take(f2, summarizeLength);
				}
			}
		}
		
		// 사용할 Frag 추출
		for(int i = 0; i < fragments.length; i++) {
			for(int j = 0; j < fragments[i].size(); j++) {
				Fragment f1 = (Fragment)fragments[i].get(j);
				
				if(f1.use)
					contexts.add(f1);
			}
		}		
		
		// 키워드 포함 빈도 순위로 정렬
		Collections.sort(contexts);
		Collections.reverse(contexts);
		
		for(int i = 0; i < contexts.size(); i++) {
			Fragment f1 = (Fragment)contexts.get(i);
			
			// 줄임규칙 적용: 기본 단락 길이 보다 작을 경우 
			if(f1.length() < contextLength) {
				int oldStart = f1.start;
				int oldEnd = f1.end;
				
				f1.start -= (contextLength - f1.length()) / 2;
				f1.end += (contextLength - f1.length()) / 2;
				
				if(f1.start < 0) 
					f1.start = 0;
				if(f1.end > content.length()) 
					f1.end = content.length();
				
				// 공백문자에 의한 앞단 분리
				if(f1.start > 0 && f1.start < oldStart) {
					for(int j = f1.start; j <= oldStart; j++) {
						f1.start = j;
						char ch = content.charAt(j);
						
						if(Character.isWhitespace(ch)) {
							f1.start++;
							break;
						}
					}
				}
				
				// 공백문자에 의한 뒷단 분리
				if(f1.end < content.length() && f1.end >= oldEnd) {
					for(int j = f1.end; j >= oldEnd; j--) {
						f1.end = j;
						char ch = content.charAt(j);
						
						if(Character.isWhitespace(ch)) {
							break;
						}
					}
				}
				
			// 늘임규칙 적용: 기본 단락 길이 보다 클 경우
			} else {
				// 공백문자에 의한 앞단 분리
				boolean spaced = false; 
				
				for(int j = f1.start - 1; j >= 0; j--) {
					char ch = content.charAt(j);
					
					if(Character.isWhitespace(ch)) {
						if(!spaced) {
							spaced = true;
							continue;
						}
						
						f1.start = j + 1;
						break;
					}
				}
				
				// 공백문자에 의한 뒷단 분리
				for(int j = f1.end; j < content.length() - 1; j++) {
					char ch = content.charAt(j);
					
					if(Character.isWhitespace(ch)) {
						if(!spaced) {
							spaced = true;
							continue;
						}
						
						f1.end = j;
						break;
					}
				}
			}
		}

		// Fragment에 변화 감지
		boolean refresh = false;
		
		// 중복 Frag 제거 및 이웃 통합
		for(int i = 0; i < contexts.size() - 1; i++) {
			Fragment f1 = (Fragment)contexts.get(i);
			
			if(f1.use) {
				for(int j = i + 1; j < contexts.size(); j++) {
					Fragment f2 = (Fragment)contexts.get(j);
					
					if(f2.use) {
						f1.take(f2, summarizeLength);
						
						if(!f2.use)
							refresh = true;
					}
				}
			}
		}
		
		// Fragment에 변화 재정렬
		if(refresh && contexts.size() > 1) {
			Collections.sort(contexts);
			Collections.reverse(contexts);
		}
		
		return contexts;
	}
	
	/**
	 * 질의문에서 키워드를 분리한다.
	 * @param queryString
	 * @return
	 */
	private String[] extractKeywords(String queryString) {
		QueryStringParser parser = new QueryStringParser();
		parser.parse(queryString);
		
		return parser.getKeywords();
	}
	
/*	
	private String[] getTokens(String text) throws IOException {
		List result = new ArrayList();
		TokenStream ts = ANALYZER.tokenStream("title", new StringReader(text));
		
		for(Token token = ts.next(); token != null; token = ts.next()) {
			result.add(token.termText());
		}
		
		return (String[])result.toArray(new String[result.size()]);
	}
*/	

	public static void main(String argv[]) {
/*
		// ScanyIndexingDemo arglist
		if(argv.length < 2) {
			System.out.println("Usage: java wherever.searcher.Summarizer <textfile> <queryStr>");
			return;
		}
		
		//
		// Parse the args
		//
		File textFile = new File(argv[0]);
		StringBuffer queryBuf = new StringBuffer();
		
		for(int i = 1; i < argv.length; i++) {
			queryBuf.append(argv[i]);
			queryBuf.append(" ");
		}
		
		//
		// Load the text file into a single string.
		//
		StringBuffer body = new StringBuffer();
		BufferedReader in = new BufferedReader(new FileReader(textFile));
		
		try {
			//System.out.println("About to read " + textFile + " from " + in);
			
			String str = in.readLine();
			
			while(str != null) {
				body.append(str);
				str = in.readLine();
			}
		} finally {
			in.close();
		}
		
		//System.out.println(body.toString());
*/		
//		Summarizer s = new Summarizer("나인 abcd");
//		
//		//System.out.print(s.getSummary(body.toString()));
//		System.out.print(s.summarize(" abcd 나인 1969년 샘 페킨파(Sam Peckinpah)가 연출하고 윌리엄 홀던(William Holden ), 어네스트 보그나인(Ernest Borgnine), 로버트 라이언(Robert Ryan) 등이 출연하였다. 기존의 서부극이 영웅적인 주인공과 악한의 대결구도를 강조하면서 주인공의 행동을 정당화시키는1969년 샘 페킨파(Sam Peckinpah)가 연출하고 윌리엄 홀던(William Holden ), 어네스트 보그나인(Ernest Borgnine), 로버트 라이언(Robert Ryan) 등이 출연하였다. 기존의 서부극이 영웅적인 주인공과 악한 나인의 대결구도를 강조하면서 주인공의 행동을 정당화시키는"));
		
		SimpleFragmentSummarizer s = new SimpleFragmentSummarizer();
		
		//System.out.print(s.getSummary(body.toString()));
		System.out.println(s.summarize("하이 오리 apple", "감 오리하이 Apple scany"));
		
		
		//System.out.println("Summary: '" + s.getSummary(body.toString()) + "'");
	}

}
