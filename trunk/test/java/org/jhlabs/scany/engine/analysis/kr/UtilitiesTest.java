package org.jhlabs.scany.engine.analysis.kr;

import junit.framework.TestCase;

import org.jhlabs.scany.engine.analysis.kr.ma.rule.VerbRule;

public class UtilitiesTest extends TestCase {

	public void testEndsWithVerbSuffix() throws Exception {
		String str = "말하";
		int i = VerbRule.endsWithVerbSuffix(str);
		if(i==-1) return;
		assertEquals("하", str.substring(i));
		System.out.println(i+":"+str.substring(i));
	}

	public void testEndsWithXVerb() throws Exception {
		String str = "피어오르";
		int i = VerbRule.endsWithXVerb(str);
		if(i==-1) return;
		assertEquals("오르", str.substring(i));
		System.out.println(i+":"+str.substring(i));
	}
}
