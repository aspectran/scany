package org.jhlabs.scany.analysis.syllabic;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

/**
 * 한글의 초성, 중성, 종성을 분석한다.
 *
 *  초성 19개:   ㄱ   ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ
 *  중성 21개:   ㅏ   ㅐ ㅑ ㅒ ㅓ ㅔ ㅕ ㅖ ㅗ ㅘ ㅙ ㅚ ㅛ ㅜ ㅝ ㅞ ㅟ ㅠ ㅡ ㅢ ㅣ
 *  종성 28개:  Fill  ㄱ ㄲ ㄳ ㄴ ㄵ ㄶ ㄷ ㄹ ㄺ ㄻ ㄼ ㄽ ㄾ ㄿ ㅀ ㅁ ㅂ ㅄ ㅅ ㅆ ㅇ ㅈ ㅊ ㅋ ㅌ ㅍ ㅎ
 *
 *  유니코드 2.0 한글의 범위
 *      AC00(가) ~ D7A3(히ㅎ) *
 * 
 * <p>Created: 2008. 03. 13 오후 12:41:52</p>
 * 
 * @author Gulendol
 *
 */
public class SyllabicTokenizer extends Tokenizer {

	private int offset = 0;

	private int bufferIndex = 0;

	private int dataLen = 0;

	private static final int MAX_WORD_LEN = 255;

	private static final int IO_BUFFER_SIZE = 1024;

	private final char buffer[] = new char[MAX_WORD_LEN];

	private final char ioBuffer[] = new char[IO_BUFFER_SIZE];

	public SyllabicTokenizer(Reader input) {
		super(input);
	}

	public final Token next() throws IOException {
		int length = 0;
		int start = offset;
		
		label0:
		do {
			do {
				offset++;
				
				if(bufferIndex >= dataLen) {
					dataLen = input.read(ioBuffer);
					bufferIndex = 0;
				}
				
				if(dataLen == -1) {
					if(length <= 0)
						return null;
					break;
				}
				
				char c = ioBuffer[bufferIndex++];
				
				if(!isTokenChar(c))
					continue label0;
				
				if(length == 0)
					start = offset - 1;
				
				if(isSyllables(c)) {
					char[] chars = syllabify(c);
					
					for(int k = 0; k < chars.length; k++) {
						if(length == MAX_WORD_LEN) {
							bufferIndex--;
							length -= k;
							continue label0;
						}
						buffer[length++] = chars[k];
					}
				} else {
					buffer[length++] = normalize(c);
				}
			} while(length != MAX_WORD_LEN);
			
			break;
		} while(length <= 0);

		Token token = new Token(new String(buffer, 0, length), start, start + length);
		
		//System.out.println("Sylabic Token: " + token.toString());
		
		return token;
	}

	protected boolean isTokenChar(char c) {
		if(c != ' ' && Character.isWhitespace(c))
			return false;
		
		return true;
	}

	protected char normalize(char c) {
		return Character.toLowerCase(c);
	}

	/**
	 * 한글의 초성, 중성, 종성을 순서대로 분리해서 문자 배열로 반환한다.
	 * @param c
	 * @return
	 */
	protected static char[] syllabify(char c) {
		int n, n1, n2, n3;
		char[] chars = new char[3];
		
		n = (int)(c & 0xFFFF);
		n = n - 0xAC00;
		n1 = n / (21 * 28);
		n = n % (21 * 28);
		n2 = n / 28;
		n3 = n % 28;
		
		chars[0] = CHOSEONG_TABLE[n1];
		chars[1] = JUNGSEONG_TABLE[n2];

		if(n3 > 0)
			chars[2] = JONGSEONG_TABLE[n3 - 1];
		
		return chars;
	}
	
	/**
	 * 자모 분리가 가능한 문자인지 여부를 반환한다.
	 * @param c
	 * @return
	 */
	protected boolean isSyllables(char c) {
		int n = (int)(c & 0xFFFF);
		
		return (n >= 0xAC00 && n <= 0xD7A3);	
	}
	
	private static char[] CHOSEONG_TABLE; 
	private static char[] JUNGSEONG_TABLE;
	private static char[] JONGSEONG_TABLE;

	static {
		//{ 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' }
		//{ 'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ' }
		//{ 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' }
		String CHOSEONG ="\u3131\u3132\u3134\u3137\u3138\u3139\u3141\u3142\u3143\u3145\u3146\u3147\u3148\u3149\u314A\u314B\u314C\u314D\u314E";
		String JUNGSEONG ="\u314F\u3150\u3151\u3152\u3153\u3154\u3155\u3156\u3157\u3158\u3159\u315A\u315B\u315C\u315D\u315E\u315F\u3160\u3161\u3162\u3163";
		String JONGSEONG ="\u0020\u3131\u3132\u3133\u3134\u3135\u3136\u3137\u3139\u313A\u313B\u313C\u313D\u313E\u313F\u3140\u3141\u3142\u3144\u3145\u3146\u3147\u3148\u314A\u314B\u314C\u314D\u314E";
		
		CHOSEONG_TABLE = CHOSEONG.toCharArray(); 
		JUNGSEONG_TABLE = JUNGSEONG.toCharArray();
		JONGSEONG_TABLE = JONGSEONG.toCharArray();
	}
}