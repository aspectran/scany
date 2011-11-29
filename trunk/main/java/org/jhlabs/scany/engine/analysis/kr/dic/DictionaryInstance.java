/**
 * 
 */
package org.jhlabs.scany.engine.analysis.kr.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.jhlabs.scany.engine.analysis.kr.ma.WordEntry;
import org.jhlabs.scany.engine.analysis.kr.util.Trie;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 29. 오후 10:06:43</p>
 *
 */
public class DictionaryInstance {

	private static String dictionaryLocation;
	
	private static Trie<String, WordEntry> dictionary;

	private static HashMap<String, String> josas;

	private static HashMap<String, String> eomis;

	private static HashMap<String, String> prefixs;

	private static HashMap<String, String> suffixs;

	private static HashMap<String, WordEntry> uncompounds;

	private static HashMap<String, String> cjwords;
	
	  protected DictionaryInstance()
	  {
	    Timer timer = new Timer();
	    try {
	      timer.start();
	      loadDic();
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      timer.stop();
	      timer.printMsg("Dictionary Loading Time");
	      System.out.println("Loaded Item " + this.table.size());
	    }
	  }

	  public static void reload()
	  {
	    if ((!(isLoading)) && (dictionary != null)) {
	      Timer timer = new Timer();
	      try {
	        System.out.println("reloading");
	        timer.start();
	        dictionary.clear();
	        dictionary.loadDic();
	      } catch (Exception e) {
	        e.printStackTrace();
	      } finally {
	        timer.stop();
	        timer.printMsg("Dictionary Loading Time");
	        System.out.println("Loaded Item " + dictionary.table.size());
	      }
	    }
	  }

	  public static void reload(List<DicReader> dicReadList)
	  {
	    if ((!(isLoading)) && (dictionary != null)) {
	      Timer timer = new Timer();
	      try {
	        System.out.println("reloading");
	        timer.start();
	        dictionary.clear();
	        for (int i = 0; i < dicReadList.size(); ++i)
	          dictionary.load((DicReader)dicReadList.get(i));
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      } finally {
	        timer.stop();
	        timer.printMsg("Dictionary Loading Time");
	        System.out.println("Loaded Item " + dictionary.table.size());
	      }
	    }
	  }

	  public void clear()
	  {
	    this.table.clear();
	    this.compNounTable.clear();
	    this.verbStemSet.clear();
	    this.maxLen = 0;
	  }

}
