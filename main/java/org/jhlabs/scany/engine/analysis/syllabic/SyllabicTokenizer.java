package org.jhlabs.scany.engine.analysis.syllabic;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.CharacterUtils;
import org.apache.lucene.util.CharacterUtils.CharacterBuffer;
import org.jhlabs.scany.context.ScanyContext;

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

	private int finalOffset = 0;

	private static final int MAX_WORD_LEN = 255;

	private static final int IO_BUFFER_SIZE = 1024;

	private final CharacterBuffer ioBuffer = CharacterUtils.newCharacterBuffer(IO_BUFFER_SIZE);

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);;

	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);

	private final CharacterUtils charUtils;

	public SyllabicTokenizer(Reader input) {
		super(input);
		charUtils = CharacterUtils.getInstance(ScanyContext.LUCENE_VERSION);
	}

	@Override
	public final boolean incrementToken() throws IOException {
		clearAttributes();
		int length = 0;
		int start = -1; // this variable is always initialized
		char[] buffer = termAtt.buffer();
		while(true) {
			if(bufferIndex >= dataLen) {
				offset += dataLen;
				if(!charUtils.fill(ioBuffer, input)) { // read supplementary char aware with CharacterUtils
					dataLen = 0; // so next offset += dataLen won't decrement offset
					if(length > 0) {
						break;
					} else {
						finalOffset = correctOffset(offset);
						return false;
					}
				}
				dataLen = ioBuffer.getLength();
				bufferIndex = 0;
			}
			// use CharacterUtils here to support < 3.1 UTF-16 code unit behavior if the char based methods are gone
			final int c = charUtils.codePointAt(ioBuffer.getBuffer(), bufferIndex);
			bufferIndex += Character.charCount(c);

			if(!isTokenChar(c) && length > 0)
				break;
			
			if(isSyllables(c)) {
				char[] chars = syllabify(c);
				if(length == 0) { // start of token
					assert start == -1;
					start = offset + bufferIndex - 1;
				} else if(length + chars.length >= buffer.length - 1) { // check if a supplementary could run out of bounds
					buffer = termAtt.resizeBuffer(2 + length + chars.length); // make sure a supplementary fits in the buffer
				}
				for(int k = 0; k < chars.length; k++) {
					//System.out.println(chars[k]);
					length += Character.toChars(chars[k], buffer, length);
					if(length >= MAX_WORD_LEN)
						break;
				}
			} else {
				if(length == 0) { // start of token
					assert start == -1;
					start = offset + bufferIndex - 1;
				} else if(length >= buffer.length - 1) { // check if a supplementary could run out of bounds
					buffer = termAtt.resizeBuffer(2 + length); // make sure a supplementary fits in the buffer
				}
				length += Character.toChars(normalize(c), buffer, length); // buffer it, normalized
				if(length >= MAX_WORD_LEN) // buffer overflow! make sure to check for >= surrogate pair could break == test
					break;
			}
		}
		termAtt.setLength(length);
		assert start != -1;
		offsetAtt.setOffset(correctOffset(start), finalOffset = correctOffset(start + length));
		return true;
	}

	@Override
	public final void end() {
		// set final offset
		offsetAtt.setOffset(finalOffset, finalOffset);
	}

	@Override
	public void reset(Reader input) throws IOException {
		super.reset(input);
		bufferIndex = 0;
		offset = 0;
		dataLen = 0;
		finalOffset = 0;
		ioBuffer.reset(); // make sure to reset the IO buffer!!
	}

	protected boolean isTokenChar(int c) {
		if(c != ' ' && Character.isWhitespace(c))
			return false;

		return true;
	}

	protected int normalize(int c) {
		return Character.toLowerCase(c);
	}

	/**
	 * 한글의 초성, 중성, 종성을 순서대로 분리해서 문자 배열로 반환한다.
	 * @param c
	 * @return
	 */
	protected static char[] syllabify(int c) {
		int n, n1, n2, n3;
		char[] chars = new char[3];

		n = c;
		n = n - 0xAC00;
		n1 = n / (21 * 28);
		n = n % (21 * 28);
		n2 = n / 28;
		n3 = n % 28;

		chars[0] = CHOSEONG_TABLE[n1];
		chars[1] = JUNGSEONG_TABLE[n2];

		if(n3 > 0)
			chars[2] = JONGSEONG_TABLE[n3];
		else
			chars[2] = ' ';

		return chars;
	}

	/**
	 * 자모 분리가 가능한 문자인지 여부를 반환한다.
	 * @param c
	 * @return
	 */
	protected boolean isSyllables(int c) {
		return (c >= 0xAC00 && c <= 0xD7A3);
	}

	private static char[] CHOSEONG_TABLE;

	private static char[] JUNGSEONG_TABLE;

	private static char[] JONGSEONG_TABLE;

	static {
		//{ 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' }
		//{ 'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ' }
		//{ 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' }
		String CHOSEONG = "\u3131\u3132\u3134\u3137\u3138\u3139\u3141\u3142\u3143\u3145\u3146\u3147\u3148\u3149\u314A\u314B\u314C\u314D\u314E";
		String JUNGSEONG = "\u314F\u3150\u3151\u3152\u3153\u3154\u3155\u3156\u3157\u3158\u3159\u315A\u315B\u315C\u315D\u315E\u315F\u3160\u3161\u3162\u3163";
		String JONGSEONG = "\u0020\u3131\u3132\u3133\u3134\u3135\u3136\u3137\u3139\u313A\u313B\u313C\u313D\u313E\u313F\u3140\u3141\u3142\u3144\u3145\u3146\u3147\u3148\u314A\u314B\u314C\u314D\u314E";

		CHOSEONG_TABLE = CHOSEONG.toCharArray();
		JUNGSEONG_TABLE = JUNGSEONG.toCharArray();
		JONGSEONG_TABLE = JONGSEONG.toCharArray();
	}
}