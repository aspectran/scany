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
package org.jhlabs.scany.engine.search;

import org.jhlabs.scany.engine.entity.Attribute;


/**
 * 
 * <p>Created: 2007. 01. 22 오전 3:07:50</p>
 * 
 * @author Gulendol
 *
 */
public class FilterAttribute {
	
	private FilterType filterType;
	
	private String attributeName;
	
	private Attribute attribute;
	
	private Object equalValue;
	
	private Object lowerValue;
	
	private Object upperValue;
	
	private boolean includeLower;
	
	private boolean includeUpper;
	
	private boolean essential = true;
	
	public FilterAttribute(String attributeName, Object equalValue, boolean essential) {
		this.filterType = FilterType.EQUAL;
		this.attributeName = attributeName;
		this.equalValue = equalValue;
		this.essential = essential;
	}
	
	public FilterAttribute(FilterType filterType, String attributeName, Object lowerValue, Object upperValue, boolean includeLower, boolean includeUpper, boolean essential) {
		this.filterType = FilterType.EQUAL;
		this.attributeName = attributeName;
		this.lowerValue = lowerValue;
		this.upperValue = upperValue;
		this.includeLower = includeLower;
		this.includeUpper = includeUpper;
		this.essential = essential;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public String getAttributeName() {
		return attributeName;
	}

	protected Attribute getAttribute() {
		return attribute;
	}

	protected void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public Object getEqualValue() {
		return equalValue;
	}

	public Object getLowerValue() {
		return lowerValue;
	}

	public Object getUpperValue() {
		return upperValue;
	}

	public boolean isIncludeLower() {
		return includeLower;
	}

	public boolean isIncludeUpper() {
		return includeUpper;
	}

	public boolean isEssential() {
		return essential;
	}

}
