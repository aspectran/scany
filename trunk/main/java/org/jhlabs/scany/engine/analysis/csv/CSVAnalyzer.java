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

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

/**
 * Comma Separated Value Analyzer.
 * 
 * <p>Created: 2008. 03. 06 오후 6:43:38</p>
 * 
 * @author Gulendol
 *
 */
public final class CSVAnalyzer extends Analyzer {

	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new CSVTokenizer(reader);
	}
	
	public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
		Tokenizer tokenizer = (Tokenizer)getPreviousTokenStream();
		
		if(tokenizer == null) {
			tokenizer = new CSVTokenizer(reader);
			setPreviousTokenStream(tokenizer);
		} else
			tokenizer.reset(reader);
		
		return tokenizer;
	}
}
