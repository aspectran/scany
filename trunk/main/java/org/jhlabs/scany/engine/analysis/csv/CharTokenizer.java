package org.jhlabs.scany.engine.analysis.csv;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

public abstract class CharTokenizer extends Tokenizer {
	public CharTokenizer(Reader input) {
		super(input);
	}

	private int offset = 0, bufferIndex = 0, dataLen = 0;

	private static final int MAX_WORD_LEN = 255;

	private static final int IO_BUFFER_SIZE = 4096;

	private final char[] ioBuffer = new char[IO_BUFFER_SIZE];

	/**
	 * Returns true iff a character should be included in a token. This
	 * tokenizer generates as tokens adjacent sequences of characters which
	 * satisfy this predicate. Characters for which this is false are used to
	 * define token boundaries and are not included in tokens.
	 */
	protected abstract boolean isTokenChar(char c);

	/**
	 * Called on each token character to normalize it before it is added to the
	 * token. The default implementation does nothing. Subclasses may use this
	 * to, e.g., lowercase tokens.
	 */
	protected char normalize(char c) {
		return c;
	}

	public Token next(Token token) throws IOException {
		token.clear();
		int length = 0;
		int start = bufferIndex;
		char[] buffer = token.termBuffer();
		while(true) {

			if(bufferIndex >= dataLen) {
				offset += dataLen;
				dataLen = input.read(ioBuffer);
				if(dataLen == -1) {
					if(length > 0)
						break;
					else
						return null;
				}
				bufferIndex = 0;
			}

			final char c = ioBuffer[bufferIndex++];

			if(isTokenChar(c)) { // if it's a token char

				if(length == 0) // start of token
					start = offset + bufferIndex - 1;
				else if(length == buffer.length)
					buffer = token.resizeTermBuffer(1 + length);

				buffer[length++] = normalize(c); // buffer it, normalized

				if(length == MAX_WORD_LEN) // buffer overflow!
					break;

			} else if(length > 0) // at non-Letter w/ chars
				break; // return 'em
		}

		token.setTermLength(length);
		token.setStartOffset(start);
		token.setEndOffset(start + length);

		// For Test
		System.out.println("CSV Token: " + token.toString());
		
		return token;
	}

	public void reset(Reader input) throws IOException {
		super.reset(input);
		bufferIndex = 0;
		offset = 0;
		dataLen = 0;
	}
}
