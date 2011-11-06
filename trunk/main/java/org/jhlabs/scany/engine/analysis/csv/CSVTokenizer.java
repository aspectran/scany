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
package org.jhlabs.scany.engine.analysis.csv;

import java.io.Reader;

/**
 * A CSVTokenizer is a tokenizer that divides text at comma character.
 * 
 * <p>Created: 2008. 03. 06 오후 6:44:02</p>
 * 
 * @author Gulendol
 *
 */
public class CSVTokenizer extends CharTokenizer {

	private static final char SEPARATOR = ',';
	private static final char SPACE = ' ';

	public CSVTokenizer(Reader in) {
		super(in);
	}

	/**
	 * 소문자로 변환
	 */
	protected char normalize(char c) {
		return Character.toLowerCase(c);
	}
	
	/**
	 * Collects only characters which do not satisfy
	 * {@link Character#isSpaceChar(char)}.
	 */
	protected boolean isTokenChar(char c) {
		if(c == SEPARATOR)
			return false;
		else if(c != SPACE && Character.isSpaceChar(c))
			return false;
		
		return true;
	}

}
