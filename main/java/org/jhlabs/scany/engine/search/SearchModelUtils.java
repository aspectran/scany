/**
 * 
 */
package org.jhlabs.scany.engine.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

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
	
}
