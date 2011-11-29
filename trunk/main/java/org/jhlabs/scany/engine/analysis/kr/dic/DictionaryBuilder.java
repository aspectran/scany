/**
 * 
 */
package org.jhlabs.scany.engine.analysis.kr.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 29. 오후 10:06:43</p>
 *
 */
public class DictionaryBuilder {

	BufferedReader br = null;

	public DictionaryBuilder(String fileName) throws UnsupportedEncodingException {
		this.br = new BufferedReader(new InputStreamReader(super.getClass().getResourceAsStream(fileName), "UTF-8"));
	}

	public String readLine() throws IOException {
		return this.br.readLine();
	}

	public void cleanup() throws IOException {
		if(this.br == null)
			return;
		this.br.close();
	}
}
