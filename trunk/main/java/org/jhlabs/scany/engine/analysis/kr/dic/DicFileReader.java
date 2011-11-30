/**
 * 
 */
package org.jhlabs.scany.engine.analysis.kr.dic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jhlabs.scany.util.Resources;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 29. 오후 10:06:43</p>
 *
 */
public class DicFileReader {

	private static final String COMMENT = "//";
	
	private BufferedReader br = null;

	public DicFileReader(String resource, String encoding) throws IOException {
		this.br = new BufferedReader(new InputStreamReader(Resources.getResourceAsStream(resource), encoding));
	}

	public DicFileReader(File file, String encoding) throws IOException {
		this.br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
	}
	
	public String readLine() throws IOException {
		String line = br.readLine();
		
		if(line == null)
			return null;
		
		line = line.trim();
		
		if(line.length() == 0 || line.startsWith(COMMENT))
			return readLine();
		
		return line;
	}

	public void close() throws IOException {
		if(br != null) {
			br.close();
			br = null;
		}
	}
}
