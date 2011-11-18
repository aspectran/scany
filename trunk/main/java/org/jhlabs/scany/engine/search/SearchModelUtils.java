/**
 * 
 */
package org.jhlabs.scany.engine.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.jhlabs.scany.engine.entity.Attribute;
import org.jhlabs.scany.engine.entity.AttributeMap;
import org.jhlabs.scany.engine.search.summarize.Summarizer;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 17. 오후 9:03:17</p>
 *
 */
public class SearchModelUtils {

	public static Sort makeSort(List<SortAttribute> sortAttributeList) {
		List<SortField> sortFieldList = new ArrayList<SortField>();
		
		Iterator<SortAttribute> iter = sortAttributeList.iterator();
		
		while(iter.hasNext()) {
			SortAttribute sortAttribute = iter.next();
			
			SortField sortFiled = new SortField(sortAttribute.getAttributeName(), (Integer)sortAttribute.getSortFieldType().getType(), sortAttribute.isReverse());
			
			sortFieldList.add(sortFiled);
		}
		
		SortField[] sortFileds = (SortField[])sortFieldList.toArray(new SortField[sortFieldList.size()]);
		Sort sort = new Sort(sortFileds);

		return sort;
	}
	
	public static List<String> extractDefaultQueryAttributeList(AttributeMap attributeMap) {
		List<String> queryAttributeList = new ArrayList<String>();
		
		Iterator<Attribute> iter = attributeMap.values().iterator();
		
		while(iter.hasNext()) {
			Attribute attribute = iter.next();
			
			if(attribute.isQueryable())
				queryAttributeList.add(attribute.getName());
		}
		
		if(queryAttributeList.size() == 0)
			return null;
		
		return queryAttributeList;
	}
	
	public static Map<String, Analyzer> extractDefaultAnalyzerMap(AttributeMap attributeMap) {
		Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
		
		Iterator<Attribute> iter = attributeMap.values().iterator();
		
		while(iter.hasNext()) {
			Attribute attribute = iter.next();
			
			if(attribute.getAnalyzer() != null)
				analyzerMap.put(attribute.getName(), attribute.getAnalyzer());
		}
		
		if(analyzerMap.size() == 0)
			return null;
		
		return analyzerMap;
	}
	
	public static Map<String, Summarizer> extractDefaultSummarizerMap(AttributeMap attributeMap) {
		Map<String, Summarizer> summarizerMap = new HashMap<String, Summarizer>();
		
		Iterator<Attribute> iter = attributeMap.values().iterator();
		
		while(iter.hasNext()) {
			Attribute attribute = iter.next();
			
			if(attribute.getSummarizer() != null)
				summarizerMap.put(attribute.getName(), attribute.getSummarizer());
		}
		
		if(summarizerMap.size() == 0)
			return null;
		
		return summarizerMap;
	}
	
}
