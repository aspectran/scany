/**
 * 
 */
package org.jhlabs.scany.engine.analysis.kr.dic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.jhlabs.scany.engine.analysis.kr.ma.CompoundEntry;
import org.jhlabs.scany.engine.analysis.kr.ma.WordEntry;
import org.jhlabs.scany.engine.analysis.kr.util.Trie;
import org.jhlabs.scany.util.StringUtils;
import org.jhlabs.scany.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 29. 오후 10:06:43</p>
 *
 */
public class DictionaryLoader {

	private static final Logger logger = LoggerFactory.getLogger(DictionaryLoader.class);

	private Properties dicProperties;
	
	private String encoding;

	public DictionaryLoader(Properties dicProperties, String encoding) {
		this.dicProperties = dicProperties;
		this.encoding = encoding;
	}

	public void loadSyllable(String dic, List<char[]> syllables) throws IOException {
		Object value = dicProperties.get(dic);
		DicFileReader reader = null;
		Timer timer = new Timer();
		
		try {
			timer.start();
			
			if(value instanceof File) {
				timer.setStopMessage(((File)value).getAbsolutePath());
				reader = new DicFileReader((File)value, encoding);
			} else {
				timer.setStopMessage(value.toString());
				reader = new DicFileReader(value.toString(), encoding);
			}
			
			String line = null;
			int cnt = 0;

			while((line = reader.readLine()) != null) {
				syllables.add(line.toCharArray());
				cnt++;
			}
			
			timer.setStopMessage(timer.getStopMessage() + "(" + cnt + ")");
		} finally {
			if(reader != null)
				reader.close();
			timer.stop();
		}
	}
	
	public void loadWords(String dic, Trie<String, WordEntry> words) throws IOException {
		Object value = dicProperties.get(dic);
		
		logger.debug("{} - {}", dic, value);
		
		DicFileReader reader = null;
		Timer timer = new Timer();
		
		try {
			timer.start();
			
			if(value instanceof File) {
				timer.setStopMessage(((File)value).getAbsolutePath());
				reader = new DicFileReader((File)value, encoding);
			} else {
				timer.setStopMessage(value.toString());
				reader = new DicFileReader(value.toString(), encoding);
			}
			
			String line = null;
			int cnt = 0;
			
			while((line = reader.readLine()) != null) {
				String[] arr = StringUtils.split(line, ",");
				if(arr.length == 2) {
					WordEntry entry = new WordEntry(arr[0], arr[1].toCharArray());
					words.add(entry.getWord(), entry);
					cnt++;
				}
			}
			
			timer.setStopMessage(timer.getStopMessage() + "(" + cnt + ")");
		} finally {
			if(reader != null)
				reader.close();
			timer.stop();
		}
	}

	public void loadCompounds(String dic, Trie<String, WordEntry> words) throws IOException {
		Object value = dicProperties.get(dic);
		DicFileReader reader = null;
		Timer timer = new Timer();
		
		try {
			timer.start();
			
			if(value instanceof File) {
				timer.setStopMessage(((File)value).getAbsolutePath());
				reader = new DicFileReader((File)value, encoding);
			} else {
				timer.setStopMessage(value.toString());
				reader = new DicFileReader(value.toString(), encoding);
			}
			
			String line = null;
			int cnt = 0;
			
			while((line = reader.readLine()) != null) {
				String[] arr = StringUtils.split(line, ":");
				if(arr.length == 2) {
					WordEntry entry = new WordEntry(arr[0], "20000X".toCharArray());
					entry.setCompounds(createCompoundEntryList(arr[1]));
					words.add(entry.getWord(), entry);
					cnt++;
				}
			}
			
			timer.setStopMessage(timer.getStopMessage() + "(" + cnt + ")");
		} finally {
			if(reader != null)
				reader.close();
			timer.stop();
		}
	}
	
	public void loadUncompounds(String dic, Map<String, WordEntry> uncompounds) throws IOException {
		Object value = dicProperties.get(dic);
		DicFileReader reader = null;
		Timer timer = new Timer();
		
		try {
			timer.start();
			
			if(value instanceof File) {
				timer.setStopMessage(((File)value).getAbsolutePath());
				reader = new DicFileReader((File)value, encoding);
			} else {
				timer.setStopMessage(value.toString());
				reader = new DicFileReader(value.toString(), encoding);
			}
			
			String line = null;
			int cnt = 0;
			
			while((line = reader.readLine()) != null) {
				String[] arr = StringUtils.split(line, ":");
				if(arr.length == 2) {
					WordEntry entry = new WordEntry(arr[0], "90000X".toCharArray());
					entry.setCompounds(createCompoundEntryList(arr[1]));
					uncompounds.put(entry.getWord(), entry);
					cnt++;
				}
			}
			
			timer.setStopMessage(timer.getStopMessage() + "(" + cnt + ")");
		} finally {
			if(reader != null)
				reader.close();
			timer.stop();
		}
	}
	
	public void loadMap(String dic, Map<String, String> map) throws IOException {
		Object value = dicProperties.get(dic);
		DicFileReader reader = null;
		Timer timer = new Timer();
		
		try {
			timer.start();
			
			if(value instanceof File) {
				timer.setStopMessage(((File)value).getAbsolutePath());
				reader = new DicFileReader((File)value, encoding);
			} else {
				timer.setStopMessage(value.toString());
				reader = new DicFileReader(value.toString(), encoding);
			}
			
			String line = null;
			int cnt = 0;
			
			while((line = reader.readLine()) != null) {
				String[] arr = StringUtils.split(line, ":");
				if(arr.length == 2) {
					map.put(arr[0], arr[1]);
					cnt++;
				}
			}
			
			timer.setStopMessage(timer.getStopMessage() + "(" + cnt + ")");
		} finally {
			if(reader != null)
				reader.close();
			timer.stop();
		}
	}
	
	public void loadSet(String dic, Set<String> set) throws IOException {
		Object value = dicProperties.get(dic);
		DicFileReader reader = null;
		Timer timer = new Timer();
		
		try {
			timer.start();
			
			if(value instanceof File) {
				timer.setStopMessage(((File)value).getAbsolutePath());
				reader = new DicFileReader((File)value, encoding);
			} else {
				timer.setStopMessage(value.toString());
				reader = new DicFileReader(value.toString(), encoding);
			}
			
			String line = null;
			int cnt = 0;
			
			while((line = reader.readLine()) != null) {
				set.add(line);
				cnt++;
			}
			
			timer.setStopMessage(timer.getStopMessage() + "(" + cnt + ")");
		} finally {
			if(reader != null)
				reader.close();
			timer.stop();
		}
	}
	
	private static List<CompoundEntry> createCompoundEntryList(String word) {
		List<CompoundEntry> list = new ArrayList<CompoundEntry>();
		String[] arr = StringUtils.split(word, ",");

		for(String str : arr) {
			CompoundEntry ce = new CompoundEntry(word);
			ce.setOffset(word.indexOf(str));
			list.add(ce);
		}
		
		return list;
	}

}
